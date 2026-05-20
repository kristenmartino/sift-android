package ai.kristenmartino.sift.data.model

import kotlinx.serialization.Serializable

/**
 * Article — mirror of `Article` in sift/lib/types.ts.
 *
 * v1 scope: core display fields only. The civic-literacy fields
 * (`contextPrimer`, `outlet`, `entityLinks`, `readingLevels`) are intentionally
 * omitted here; they land in a follow-up PR alongside ArticleDetailScreen.
 *
 * The Json decoder is configured with `ignoreUnknownKeys = true` (see
 * `NetworkModule`) so the server can ship those fields without breaking us.
 *
 * Wire format is camelCase JSON; Kotlin property names match exactly.
 */
@Serializable
data class Article(
    val id: String,
    val title: String,
    val summary: String,
    val sourceUrl: String,
    val sourceName: String,
    /** ISO-8601 (e.g. "2026-05-20T07:30:00Z"). Nullable — RSS sometimes omits dates. */
    val publishedDate: String? = null,
    val imageUrl: String? = null,
    val category: CategoryId,
    /** Estimated reading time in minutes. Computed server-side. */
    val readTime: Int,
    val whyItMatters: String? = null,
    /** 0–1 score from the pipeline's ranking node. Higher = more important. */
    val importanceScore: Double? = null,
)
