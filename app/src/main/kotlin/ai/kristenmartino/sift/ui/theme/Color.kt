package ai.kristenmartino.sift.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Sift color palette. Mirrors the web's CSS custom properties in
 * sift/app/globals.css — "Newsprint" (light) and "Late Edition" (dark).
 *
 * Values are copied, not computed. Design drift between web and Android
 * should be intentional, not accidental.
 *
 * TODO(v1.2): extract palette to JSON token file in sift/ that both web
 * and Android consume. Same task forward-declared in IOS_APP_PLAN.md.
 */

// Newsprint (light)
val NewsprintPaper = Color(0xFFF5F1E8)
val NewsprintInk = Color(0xFF1E1B16)
val NewsprintMuted = Color(0xFF6B6660)
val NewsprintAccent = Color(0xFFA8341F) // warm vermilion — "Top" category color

// Late Edition (dark)
val LateEditionBg = Color(0xFF1A1612)
val LateEditionInk = Color(0xFFE8E2D5)
val LateEditionMuted = Color(0xFF8E8678)
val LateEditionAccent = Color(0xFFD4502E)

// Category accents — these match sift/lib/constants.ts CATEGORY_COLORS.
// Used as the top-of-card accent bar and category chip color.
val CategoryTop = Color(0xFFA8341F) // vermilion
val CategoryTech = Color(0xFF2E5C9E) // electric blue
val CategoryBusiness = Color(0xFF2F6B3F) // forest green
val CategoryScience = Color(0xFF5A3FA8) // deep violet
val CategoryEnergy = Color(0xFF1F8B89) // teal
val CategoryWorld = Color(0xFFB87333) // amber
val CategoryHealth = Color(0xFFB23E5B) // rose
val CategoryPolitics = Color(0xFF3F47A8) // indigo
val CategorySports = Color(0xFF2E9CB8) // cyan
val CategoryEntertainment = Color(0xFFA82E47) // crimson
