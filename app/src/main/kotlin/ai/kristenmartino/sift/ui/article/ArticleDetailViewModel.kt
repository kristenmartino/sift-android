package ai.kristenmartino.sift.ui.article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ai.kristenmartino.sift.data.repository.ArticleStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Reads `articleId` out of the nav args and resolves the [ai.kristenmartino.sift.data.model.Article]
 * against [ArticleStore].
 *
 * Synchronous in v1 — the store is in-memory and lookup is constant-time.
 * `Loading` only exists to keep the state machine symmetric with `FeedUiState`
 * and to give the chrome PR (server-fetched primer + entities) a place to
 * hang async work.
 */
@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val store: ArticleStore,
) : ViewModel() {

    private val articleId: String = checkNotNull(savedStateHandle[ARG_ARTICLE_ID]) {
        "ArticleDetailScreen requires '$ARG_ARTICLE_ID' nav arg"
    }

    private val _state: MutableStateFlow<ArticleDetailUiState> = MutableStateFlow(
        store.get(articleId)?.let(ArticleDetailUiState::Content) ?: ArticleDetailUiState.Missing,
    )
    val state: StateFlow<ArticleDetailUiState> = _state.asStateFlow()

    companion object {
        const val ARG_ARTICLE_ID = "articleId"
    }
}
