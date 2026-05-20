package ai.kristenmartino.sift.ui.theme

import androidx.compose.ui.graphics.Color
import ai.kristenmartino.sift.data.model.CategoryId

/**
 * Category → accent color. Single source of truth for the 4dp card bar,
 * the detail-screen accent strip, and any chip that needs the category tint.
 *
 * Mirrors `CATEGORY_COLORS` in sift/lib/constants.ts via the constants in
 * [Color.kt]. If a new category is added there, update this `when` too —
 * the `enum` will tell you (`when` over an enum without `else` is exhaustive).
 */
fun CategoryId.accentColor(): Color = when (this) {
    CategoryId.TOP -> CategoryTop
    CategoryId.TECHNOLOGY -> CategoryTech
    CategoryId.BUSINESS -> CategoryBusiness
    CategoryId.SCIENCE -> CategoryScience
    CategoryId.ENERGY -> CategoryEnergy
    CategoryId.WORLD -> CategoryWorld
    CategoryId.HEALTH -> CategoryHealth
    CategoryId.POLITICS -> CategoryPolitics
    CategoryId.SPORTS -> CategorySports
    CategoryId.ENTERTAINMENT -> CategoryEntertainment
}
