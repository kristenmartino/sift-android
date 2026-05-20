package ai.kristenmartino.sift.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
 * Owns the feed state for one category at a time. v1 keeps category in
 * internal state (default = TOP); a Tabs / HorizontalPager UI in a later PR
 * will drive `selectCategory(...)` from outside.
 *
 * Pattern: every screen here exposes a single `StateFlow<UiState>` and a
 * handful of intent methods. No event channels, no shared mutable state
 * across screens. Simpler to test, simpler to reason about lifecycle.
 */
@HiltViewModel
class FeedViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<FeedUiState>(FeedUiState.Loading(CategoryId.TOP))
    val state: StateFlow<FeedUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun selectCategory(category: CategoryId) {
        if (_state.value.category == category) return
        _state.value = FeedUiState.Loading(category)
        loadFeed(category)
    }

    fun refresh() {
        val current = _state.value
        val category = current.category
        _state.update {
            when (current) {
                is FeedUiState.Content -> current.copy(refreshing = true)
                else -> FeedUiState.Loading(category)
            }
        }
        loadFeed(category)
    }

    private fun loadFeed(category: CategoryId) {
        viewModelScope.launch {
            runCatching { articleRepository.feed(category) }
                .onSuccess { articles ->
                    _state.value = FeedUiState.Content(
                        category = category,
                        articles = articles,
                        refreshing = false,
                    )
                }
                .onFailure { throwable ->
                    _state.value = FeedUiState.Error(
                        category = category,
                        message = throwable.userFacingMessage(),
                    )
                }
        }
    }
}

/**
 * Strip-down of common Retrofit / IO exceptions to one-liners suitable for the
 * UI. Don't surface stack traces or raw exception messages — they leak
 * internal detail and confuse non-engineers.
 */
private fun Throwable.userFacingMessage(): String = when {
    this is java.net.UnknownHostException ->
        "Couldn't reach Sift. Check your connection and try again."
    this is java.net.SocketTimeoutException ->
        "The request timed out. Try again."
    else -> "Something went wrong loading the feed. Try again."
}
