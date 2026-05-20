package ai.kristenmartino.sift.ui.article

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * Launch [url] in Chrome Custom Tabs with a Sift-themed toolbar.
 *
 * Falls back to the default browser via `CustomTabsIntent` itself — the
 * library handles the no-Chrome-installed case by routing the underlying
 * `ACTION_VIEW` intent. No additional fallback needed.
 *
 * We pass both light and dark color scheme params so the OS picks the right
 * one when the system theme changes mid-session.
 */
fun launchCustomTab(
    context: Context,
    url: String,
    lightToolbarColor: Color,
    darkToolbarColor: Color,
) {
    val intent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setUrlBarHidingEnabled(true)
        .setDefaultColorSchemeParams(
            CustomTabColorSchemeParams.Builder()
                .setToolbarColor(lightToolbarColor.toArgb())
                .build(),
        )
        .setColorSchemeParams(
            CustomTabsIntent.COLOR_SCHEME_DARK,
            CustomTabColorSchemeParams.Builder()
                .setToolbarColor(darkToolbarColor.toArgb())
                .build(),
        )
        .build()
    intent.launchUrl(context, Uri.parse(url))
}
