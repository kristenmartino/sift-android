package ai.kristenmartino.sift.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ai.kristenmartino.sift.data.model.Category
import ai.kristenmartino.sift.data.model.CategoryId
import ai.kristenmartino.sift.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Owns feed state for all 10 categories.
 *
 * Why per-category state (vs one-at-a-time): the [FeedHostScreen] uses a
 * `HorizontalPager` with `beyondBoundsPageCount = 1`, so adjacent pages
 * compose. With one-at-a-time state, swiping triggers a re-fetch every time;
 * with per-category state, already-loaded pages render instantly.
 *
 * Loading strategy: eager-load `TOP` on init (the default landing tab);
 * other categories load on demand when [ensureLoaded] is called from the
 * pager's page-change effect. Subsequent visits within session use the
 * cached state until [refresh] is called.
 */
@HiltViewModel
class FeedViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
) : ViewModel() {

    private val _states: MutableStateFlow<Map<CategoryId, FeedUiState>> = MutableStateFlow(
        Category.ALL.associate { it.id to FeedUiState.Loading(it.id) },
    )
    val states: StateFlow<Map<CategoryId, FeedUiState>> = _states.asStateFlow()

    /**
     * Categories currently being fetched. Prevents double-fetch from
     * concurrent `ensureLoaded` calls (pager + visibility effects can both
     * fire on the same category before the first request resolves).
     */
    private val inFlight: MutableSet<CategoryId> = mutableSetOf()

    init {
        loadFeed(CategoryId.TOP)
    }

    /**
     * Called by the pager when a page becomes selected. Idempotent — only
     * fetches if the category hasn't been loaded yet (or is in Error state,
     * in which case [refresh] handles re-fetch).
     */
    fun ensureLoaded(category: CategoryId) {
        val current = _states.value[category]
        if (current is FeedUiState.Content) return
        if (current is FeedUiState.Error) return // user must tap Retry
        if (category in inFlight) return
        loadFeed(category)
    }

    fun refresh(category: CategoryId) {
        val current = _states.value[category]
        val next = when (current) {
            is FeedUiState.Content -> current.copy(refreshing = true)
            else -> FeedUiState.Loading(category)
        }
        _states.update { it + (category to next) }
        loadFeed(category)
    }

    private fun loadFeed(category: CategoryId) {
        if (!inFlight.add(category)) return
        viewModelScope.launch {
            try {
                val articles = articleRepository.feed(category)
                _states.update { it + (category to FeedUiState.Content(category, articles)) }
            } catch (throwable: Throwable) {
                _states.update {
                    it + (category to FeedUiState.Error(category, throwable.userFacingMessage()))
                }
            } finally {
                inFlight.remove(category)
            }
        }
    }
}

/**
 * Strip-down of common Retrofit / IO exceptions to one-liners suitable for the
 * UI. Don't surface stack traces or raw exception messages — they leak
 * internal detail and confuse non-engineers.
 */
private fun Throwable.userFacingMessage(): String = when (this) {
    is java.net.UnknownHostException ->
        "Couldn't reach Sift. Check your connection and try again."
    is java.net.SocketTimeoutException ->
        "The request timed out. Try again."
    else -> "Something went wrong loading the feed. Try again."
}
