package ai.kristenmartino.sift.data.model

import kotlinx.serialization.Serializable

/**
 * Top-level shape returned by `GET /api/news?category=:id`.
 *
 * Mirrors `NewsApiResponse` in sift/lib/types.ts. v1 ignores the `stories`
 * array — story threading uses its own data class hierarchy and the first
 * pass only renders the flat `articles` feed. When StoryCard composables
 * land in a later PR, this becomes `articles: List<Article>` +
 * `stories: List<Story>`.
 */
@Serializable
data class NewsApiResponse(
    val articles: List<Article>,
    /** Whether the response came from the API route's hot cache (vs a fresh DB read). */
    val cached: Boolean = false,
    val fetchedAt: String? = null,
)
