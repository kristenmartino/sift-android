package ai.kristenmartino.sift.data.repository

import ai.kristenmartino.sift.data.model.Article
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In-memory cache of articles seen during this process's lifetime.
 *
 * Purpose: when the user taps a feed card we navigate by `articleId` only —
 * the detail screen reads the full [Article] back out of here. Decouples
 * `ArticleDetailViewModel` from `FeedViewModel` so neither screen has to know
 * about the other.
 *
 * v1 is process-scoped on purpose:
 *   - Survives configuration changes (singleton).
 *   - Does NOT survive process death. If the OS kills us and the user comes
 *     back into a detail deep-link, [get] returns null and the detail VM
 *     shows the "Article not available" state. SavedStateHandle restoration
 *     is a v1.1 polish item per STATUS.md.
 *
 * Room migration (week 6) swaps the impl for a Dao + Flow without changing
 * callers — same `put` / `get` shape.
 */
@Singleton
class ArticleStore @Inject constructor() {
    private val byId = ConcurrentHashMap<String, Article>()

    /** Bulk insert; called after every successful feed fetch. */
    fun put(articles: List<Article>) {
        articles.forEach { byId[it.id] = it }
    }

    /** Returns null if the article hasn't been seen this process. */
    fun get(id: String): Article? = byId[id]
}
