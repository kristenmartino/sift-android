package ai.kristenmartino.sift.di

import ai.kristenmartino.sift.BuildConfig
import ai.kristenmartino.sift.data.api.SiftApi
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Networking graph. Singletons so we don't churn connection pools per request.
 *
 * Base URL is hard-coded to the production Sift frontend for v1. When we add
 * a staging environment + product flavors, this moves to BuildConfig and the
 * release/debug variants pick different defaults.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val SIFT_BASE_URL = "https://siftnews.kristenmartino.ai/"

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        // Tolerate fields the server adds (civic-literacy primer, outlet,
        // entityLinks, etc.) that we don't yet model on the Kotlin side.
        ignoreUnknownKeys = true
        // The server doesn't currently send default values explicitly; keep
        // strict-ness off so a missing optional doesn't blow up decoding.
        coerceInputValues = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingLevel = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BASIC
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = loggingLevel })
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        json: Json,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(SIFT_BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideSiftApi(retrofit: Retrofit): SiftApi = retrofit.create(SiftApi::class.java)
}
