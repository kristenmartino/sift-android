package ai.kristenmartino.sift.ui.share

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ai.kristenmartino.sift.ui.theme.SiftTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Receives ACTION_SEND from Safari, Chrome, Messages, etc.
 *
 * v1 scaffold: extracts the shared URL from the Intent and renders a
 * placeholder UI. Phase 2 wires this to POST /v1/share/sift-this and
 * displays the returned civic-literacy summary.
 *
 * Runs in its own task (theme = Translucent overlay) so we don't disrupt
 * the user's source app. Tapping the result hands off to MainActivity
 * with a `sift://share/:artifactId` deep link.
 */
@AndroidEntryPoint
class ShareTargetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedUrl = when (intent?.action) {
            Intent.ACTION_SEND -> intent.getStringExtra(Intent.EXTRA_TEXT)
            else -> null
        }

        setContent {
            SiftTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        ShareTargetPlaceholder(sharedUrl)
                    }
                }
            }
        }
    }
}

@Composable
private fun ShareTargetPlaceholder(url: String?) {
    Text(
        text = if (url.isNullOrBlank()) {
            "Share extension received nothing (Phase 1 stub)."
        } else {
            "Will Sift this URL in Phase 2:\n\n$url"
        },
        style = MaterialTheme.typography.bodyLarge,
    )
}
