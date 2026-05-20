package ai.kristenmartino.sift.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Root composable. v1 scaffold renders a placeholder. Phase 2 wires the
 * Compose Navigation graph (Feed, Article detail, Search, Bookmarks, Settings).
 *
 * Reference: docs/ANDROID_APP_v1.md §Architecture (kristenmartino/sift).
 */
@Composable
fun SiftApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Scaffold { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Sift",
                    style = MaterialTheme.typography.displayLarge,
                )
            }
        }
    }
}
