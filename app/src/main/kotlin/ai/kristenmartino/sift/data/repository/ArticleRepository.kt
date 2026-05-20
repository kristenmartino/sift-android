package ai.kristenmartino.sift.data.repository

import ai.kristenmartino.sift.data.api.SiftApi
import ai.kristenmartino.sift.data.model.Article
import ai.kristenmartino.sift.data.model.CategoryId
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The thinnest possible wrapper over [SiftApi].
 *
 * v1 has no offline cache (Room layer lands in week 6 per the plan), so this
 * is mostly a typed seam — useful for testing and for the Room cache to slot
 * in without changing every ViewModel call site.
 */
@Singleton
class ArticleRepository @Inject constructor(
    private val api: SiftApi,
) {
    /**
     * Fetch articles for [category].
     *
     * Errors propagate to the caller. The Retrofit Json instance ignores
     * unknown keys (see NetworkModule), so a backend that adds fields to
     * `Article` won't break us — we just won't render them until we know
     * to look for them.
     */
    suspend fun feed(category: CategoryId): List<Article> = api.getFeed(category.wire()).articles
}
