package ai.kristenmartino.sift.ui.article

import ai.kristenmartino.sift.data.model.Article

/**
 * Mirror of `FeedUiState` for the detail screen.
 *
 * v1 detail is read-only and synchronous (lookup against `ArticleStore`), so
 * `Loading` is mostly a placeholder for the first composition before the VM's
 * `init` block runs. If/when the chrome PR adds a server-side primer fetch,
 * `Loading` becomes a real async state.
 */
sealed interface ArticleDetailUiState {
    data object Loading : ArticleDetailUiState

    data class Content(val article: Article) : ArticleDetailUiState

    /** Lookup miss — store doesn't have this id (likely process death). */
    data object Missing : ArticleDetailUiState
}
