package ai.kristenmartino.sift.ui.feed

import ai.kristenmartino.sift.data.model.Article
import ai.kristenmartino.sift.data.model.CategoryId

/**
 * Single screen state. The ViewModel emits one of these via `StateFlow`;
 * the Composable renders accordingly.
 *
 * We use `Loading` / `Content` / `Error` rather than nullable fields with
 * loading booleans — easier to render exhaustively, harder to forget a case.
 */
sealed interface FeedUiState {
    val category: CategoryId

    data class Loading(override val category: CategoryId) : FeedUiState

    data class Content(
        override val category: CategoryId,
        val articles: List<Article>,
        val refreshing: Boolean = false,
    ) : FeedUiState

    data class Error(
        override val category: CategoryId,
        /** Short, user-facing copy. Surface details to logs, not the UI. */
        val message: String,
    ) : FeedUiState
}
