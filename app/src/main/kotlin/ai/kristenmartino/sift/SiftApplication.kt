package ai.kristenmartino.sift

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entrypoint. Hilt scans for @AndroidEntryPoint annotations from
 * here, and any global init (Sentry, PostHog, FCM token registration) goes
 * here.
 *
 * v1 scaffold: Hilt only. Observability + analytics + push init blocks land
 * in follow-up PRs as the corresponding features are wired.
 */
@HiltAndroidApp
class SiftApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // TODO(Phase 2): Sentry.init with DSN from BuildConfig
        // Sentry.init { options ->
        //     options.dsn = BuildConfig.SENTRY_DSN
        //     options.tracesSampleRate = 1.0
        //     options.environment = if (BuildConfig.DEBUG) "debug" else "release"
        // }

        // TODO(Phase 2): PostHog.with(this, ...) when analytics PR lands
    }
}
