package ai.kristenmartino.sift.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Root composable. Hosts the [SiftNavHost] — `feed` (10-category tabs +
 * pager) and `article/{articleId}` (detail). Search / bookmarks / settings
 * destinations join the same graph in follow-up PRs.
 *
 * Reference: docs/ANDROID_APP_v1.md §Architecture (kristenmartino/sift).
 */
@Composable
fun SiftApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        SiftNavHost()
    }
}
