package ai.kristenmartino.sift

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ai.kristenmartino.sift.ui.SiftApp
import ai.kristenmartino.sift.ui.theme.SiftTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single Activity. All screen navigation happens inside Compose Navigation
 * (`SiftApp()`); we never start a new Activity for in-app navigation.
 *
 * Why single-Activity:
 *   - Compose Navigation handles back stack, deep links, animation transitions
 *   - State preservation across config changes is easier with ViewModels keyed
 *     to nav destinations
 *   - Less Manifest churn as we add screens
 *
 * Exceptions to the single-Activity rule:
 *   - `ShareTargetActivity` — runs in its own task because the launcher needs
 *     to handle ACTION_SEND from outside the app
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SiftTheme {
                SiftApp()
            }
        }
    }
}
