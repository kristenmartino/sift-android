package ai.kristenmartino.sift.data.api

import ai.kristenmartino.sift.data.model.NewsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit contract for the Sift backend.
 *
 * Reads currently target the existing Next.js routes at
 * https://siftnews.kristenmartino.ai per [DECISIONS.md#D33](
 * https://github.com/kristenmartino/sift/blob/main/docs/DECISIONS.md
 * ) — we don't proliferate a canonical `/v1/...` API until a third client
 * validates the surface.
 *
 * The two genuinely net-new endpoints we'll add to `sift-api` (per
 * ANDROID_APP_v1.md §API contract) — `POST /v1/share/sift-this` and
 * `POST /v1/devices/register` — will go in a separate interface
 * (`SiftBackendApi`) with a different baseUrl when they land.
 */
interface SiftApi {
    /**
     * Feed by category.
     *
     * Empty `category` is not supported; the route requires the param.
     * `limit` and `after` cursor are forward-declared but unused for v1 —
     * the Next.js route returns ~30 per call by default which is plenty
     * for a first paint.
     */
    @GET("api/news")
    suspend fun getFeed(
        @Query("category") category: String,
        @Query("limit") limit: Int? = null,
        @Query("after") after: String? = null,
    ): NewsApiResponse
}
