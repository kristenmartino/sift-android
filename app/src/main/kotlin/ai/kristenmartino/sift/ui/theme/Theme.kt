package ai.kristenmartino.sift.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Newsprint (light) + Late Edition (dark) theming, mirroring sift/app/globals.css.
 *
 * NOT using Material Dynamic Color — Sift's brand palette is hand-tuned for
 * editorial warmth; dynamic-color would override that with system wallpaper.
 */

private val NewsprintScheme = lightColorScheme(
    primary = NewsprintAccent,
    onPrimary = NewsprintPaper,
    background = NewsprintPaper,
    onBackground = NewsprintInk,
    surface = NewsprintPaper,
    onSurface = NewsprintInk,
    surfaceVariant = NewsprintPaper,
    onSurfaceVariant = NewsprintMuted,
)

private val LateEditionScheme = darkColorScheme(
    primary = LateEditionAccent,
    onPrimary = LateEditionBg,
    background = LateEditionBg,
    onBackground = LateEditionInk,
    surface = LateEditionBg,
    onSurface = LateEditionInk,
    surfaceVariant = LateEditionBg,
    onSurfaceVariant = LateEditionMuted,
)

@Composable
fun SiftTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) LateEditionScheme else NewsprintScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SiftTypography,
        content = content,
    )
}
