package ai.kristenmartino.sift.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The 10 category IDs. Mirrors `CategoryId` in sift/lib/types.ts.
 *
 * Wire format (JSON) is lowercase string; UI labels live in [Category].
 * Adding a category requires updates in three places: here, `Category.kt`,
 * and the backend pipeline classifier prompt — keep them in sync.
 */
@Serializable
enum class CategoryId {
    @SerialName("top") TOP,
    @SerialName("technology") TECHNOLOGY,
    @SerialName("business") BUSINESS,
    @SerialName("science") SCIENCE,
    @SerialName("energy") ENERGY,
    @SerialName("world") WORLD,
    @SerialName("health") HEALTH,
    @SerialName("politics") POLITICS,
    @SerialName("sports") SPORTS,
    @SerialName("entertainment") ENTERTAINMENT,
    ;

    /** Lowercase wire value (`"top"`, `"technology"`, ...). Used as `?category=` query param. */
    fun wire(): String = name.lowercase()
}

/**
 * Display-side metadata for a category. Pure UI concern; not over the wire.
 *
 * Labels mirror `CATEGORIES` in sift/lib/constants.ts. Single-character icons
 * (◆, ⬡, △, etc.) are deliberately the same as web so users moving between
 * surfaces see the same visual identity.
 */
data class Category(
    val id: CategoryId,
    val label: String,
    val icon: String,
) {
    companion object {
        val ALL: List<Category> = listOf(
            Category(CategoryId.TOP, "Top Stories", "◆"),
            Category(CategoryId.TECHNOLOGY, "Technology", "⬡"),
            Category(CategoryId.BUSINESS, "Business", "△"),
            Category(CategoryId.SCIENCE, "Science", "◎"),
            Category(CategoryId.ENERGY, "Energy", "⚡"),
            Category(CategoryId.WORLD, "World", "◐"),
            Category(CategoryId.HEALTH, "Health", "✦"),
            Category(CategoryId.POLITICS, "Politics", "⚖"),
            Category(CategoryId.SPORTS, "Sports", "⬢"),
            Category(CategoryId.ENTERTAINMENT, "Entertainment", "▶"),
        )
    }
}
