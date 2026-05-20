package ai.kristenmartino.sift.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ai.kristenmartino.sift.ui.feed.FeedHostScreen

/**
 * Root composable. v1 wires [FeedHostScreen] as the sole destination
 * (10-category tabs + pager). Article detail / search / bookmarks /
 * settings join via Compose Navigation in follow-up PRs.
 *
 * Reference: docs/ANDROID_APP_v1.md §Architecture (kristenmartino/sift).
 */
@Composable
fun SiftApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        FeedHostScreen(
            // Article detail screen lands in the next PR. For now, taps are no-ops.
            onArticleClick = { /* TODO(Phase 2): navigate to ArticleDetailScreen */ },
        )
    }
}
