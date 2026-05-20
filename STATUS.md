# sift-android — STATUS

**Updated:** 2026-05-20
**Tier:** v1 (Phase 2 — feed wired)
**Velocity:** ~2 PRs / day (just started)

## Active focus

Phase 2 underway. **FeedHostScreen** with 10-category `ScrollableTabRow` + `HorizontalPager` over `FeedScreen`. Each category's state is cached in `FeedViewModel.states: Map<CategoryId, FeedUiState>`, so swiping between already-loaded categories is instant. Hilt + Retrofit + OkHttp + kotlinx.serialization graph live in `di/NetworkModule.kt`; data layer in `data/{api,model,repository}/`; UI in `ui/feed/`.

Canonical decisions: [`sift/docs/ANDROID_APP_v1.md`](https://github.com/kristenmartino/sift/blob/main/docs/ANDROID_APP_v1.md) — KPIs in §2, monetization stance in §3, civic-literacy translation risk called out in §6.

## Open strategic question

**Will the civic-literacy primer + entity chips actually work on a phone screen?**

Biggest design risk from the iOS plan critique, carried forward. The primer panel is ~60 words of prose + 0–4 term cards; the article body has 6+ entity-link chips inline. On a 6.1" portrait screen with thumbs-on-bottom ergonomics, the wall-of-text risk is real.

**Status: provisionally resolved.** Design sprint v0 landed in [`sift/docs/DESIGN_SPRINT.md`](https://github.com/kristenmartino/sift/blob/main/docs/DESIGN_SPRINT.md) — answer is "progressive disclosure with editorial defaults": above the fold = title + source + AI summary + tap-to-expand affordance; primer collapsed by default; term tap → modal bottom sheet → dossier in Custom Tabs. **Final validation at closed beta** (week 10–11 per `ANDROID_APP_v1.md`) — recruit 5 readers from the web user pool, watch them use the article detail screen specifically.

## Next 3

1. **[committed]** `ArticleDetailScreen` — title, summary, source link to Custom Tabs (Chrome Custom Tabs with reader-mode hint). Implements §Screen 1 IA from the design sprint. Civic-literacy chrome (primer panel, term chips, entity chips, cross-spectrum panel) deferred to the next PR after this lands. Tier `v1` · `effort-week`.
2. **[committed]** Compose Navigation — `NavHost` wiring `feed` ↔ `article/{id}`. Currently `onArticleClick` is a no-op; this PR makes taps navigate. Tier `v1` · `effort-day`.
3. **[committed]** Civic-literacy chrome — `PrimerPanel` (expandable), `TermChip` + `EntityChip` + `OutletBadge` + bottom-sheet pattern, `CrossSpectrumPanel`. Implements the progressive-disclosure pattern from design sprint §Screen 1. Component inventory in sprint doc §Component inventory. Tier `v1` · `effort-week`.

## Blocked-on

- **Google Play Developer account** ($25 one-time, instant) — needed by week 9 for closed beta. Not blocking dev work.
- **FCM project setup** + `google-services.json` — needed by week 8 for push. Not blocking earlier weeks.

*(Design sprint resolved 2026-05-20 — see [`sift/docs/DESIGN_SPRINT.md`](https://github.com/kristenmartino/sift/blob/main/docs/DESIGN_SPRINT.md).)*

## Recent decisions

- **2026-05-20** — **Design sprint v0 shipped.** [`sift/docs/DESIGN_SPRINT.md`](https://github.com/kristenmartino/sift/blob/main/docs/DESIGN_SPRINT.md). Six screens (article detail, feed, share target, topic search, bookmarks, settings) with IA + ASCII wireframes + interaction patterns + a 10-item component inventory for Phase 2 extraction. The civic-literacy translation pattern is **progressive disclosure with editorial defaults**: default state is scannable; primer panel + cross-spectrum collapsed by default; term/entity chip tap → modal bottom sheet → "Open full dossier" → Custom Tabs. 11 open questions documented with provisional sprint votes (none block Phase 2 code). Validates at closed beta (week 10–11) with 5 readers from the web user pool.
- **2026-05-20** — **Category tabs + HorizontalPager.** `FeedViewModel` reshaped from one-category-at-a-time to `Map<CategoryId, FeedUiState>` so already-loaded pages render instantly on revisit. `FeedScreen` becomes stateless (parent passes the relevant state + callbacks); `FeedHostScreen` owns the pager + tabs + ViewModel. Loading strategy: eager-load `TOP` on init, others on first selection via `LaunchedEffect(currentPage)`. Error states require explicit Retry (don't auto-refetch on revisit) so a transient failure can be inspected.
- **2026-05-20** — **Two post-merge build fixes on top of PR #2 (feed wired).** Pulled main, build broke; both root-caused and landed direct-to-main:
  - `data/api/SiftApi.kt:13` KDoc contained the literal `` `/v1/*` ``. Kotlin block comments nest per spec, so `/*` opened a nested level that the `*/` on line 20 closed prematurely, leaving the outer KDoc unterminated → "Unclosed comment" at EOF → six cascading misleading Hilt KSP `error.NonExistentClass` failures. Rewrote to `` `/v1/...` `` (commit `c09ba96`). Lesson: avoid `/*` or `*/` sequences inside KDoc backticks.
  - `gradle/libs.versions.toml` had `retrofit-kotlinx-serialization` aliased to `com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0`, but JakeWharton's 1.0.0 release re-packaged everything under `com.jakewharton.retrofit2.converter.kotlinx.serialization` — while `di/NetworkModule.kt` imports `retrofit2.converter.kotlinx.serialization.asConverterFactory` (Square's package). Square took over the converter; it now ships as `com.squareup.retrofit2:converter-kotlinx-serialization` versioned with Retrofit itself. Swapped artifact, dropped the now-unused `retrofitSerialization` version pin (commit `b2c2c04`).
- **2026-05-20** — **First wired screen — FeedScreen → /api/news.** Article + NewsApiResponse data classes mirror sift/lib/types.ts (camelCase, kotlinx.serialization with `ignoreUnknownKeys = true` so server-side civic-literacy field additions don't break decoding). Single `FeedViewModel.state: StateFlow<FeedUiState>` (Loading / Content / Error) — no event channels, no nullable-with-booleans pattern. `ArticleRepository` is a thin Retrofit wrapper for now; Room cache layer slots in at week 6 without changing call sites.
- **2026-05-20** — **First clean app launch on emulator.** Sentry SDK's `SentryInitProvider` ContentProvider runs before `Application.onCreate` and crashes (`IllegalArgumentException: DSN is required`) when no DSN is configured. Disabled auto-init via `<meta-data android:name="io.sentry.auto-init" android:value="false" />` in AndroidManifest. Re-enable / wire manual `Sentry.init()` in `SiftApplication.onCreate` once DSN is provisioned. Same category of scaffold bug as the build-error fixes below.
- **2026-05-20** — **First clean `assembleDebug`.** Three scaffold fixes on top of the initial commit to get a green build:
  - Added `com.google.android.material:material:1.12.0` to `libs.versions.toml` + `app/build.gradle.kts`. `themes.xml` parents `Theme.Material3.DayNight.NoActionBar`, which lives in the Material Components library — `androidx.compose.material3` is Compose-only and doesn't ship XML themes.
  - Flipped `ksp.useKSP2=true` → `false` in `gradle.properties`. Hilt 2.52's bytecode transform that auto-fills `@AndroidEntryPoint`/`@HiltAndroidApp` parent classes runs after KSP2 reads them, so KSP2 sees empty annotations and fails. Revisit when Hilt catches up.
  - Known benign warning: `ui/theme/Theme.kt:55` uses deprecated `Window.statusBarColor`. Tracked for a future cleanup PR (modern path: `WindowCompat` + edge-to-edge).
  - Pinned the Gradle daemon JVM to JDK 21 (JetBrains) via `gradle/gradle-daemon-jvm.properties` + `foojay-resolver-convention` plugin in `settings.gradle.kts`. Both auto-generated by Gradle 8.14 on first run; committing so contributors without JDK 21 get an auto-download fallback rather than a cryptic toolchain error.
- **2026-05-20** — **v1 scaffold landed.** Gradle 8.14.3, Kotlin 2.0.21, Compose BOM 2024.12.01, AGP 8.7.3, JDK 17 target. Single `app` module (multi-module split is premature for solo work).
- **2026-05-20** — **Reuse existing Next.js routes** for reads (D33 in `sift/docs/DECISIONS.md`). Only 2 net-new endpoints in sift-api: `/v1/share/sift-this` and `/v1/devices/register`.
- **2026-05-20** — **Material Dynamic Color disabled.** Sift's editorial palette is hand-tuned; system-wallpaper-driven colors would override it. Newsprint / Late Edition tokens copied from web.
- **2026-05-20** — **Single Activity + Compose Navigation.** `ShareTargetActivity` is the only second Activity (handles ACTION_SEND from outside the app).
- **2026-05-20** — **Hilt for DI.** Solo-dev cost is low; compile-time validation pays off as the surface grows. KSP-driven (not kapt).

---

*See also: [`CLAUDE.md`](./CLAUDE.md) (orientation, pre-session ritual), [`BACKLOG.md`](./BACKLOG.md), [`README.md`](./README.md). Canonical decisions: [`sift/docs/ANDROID_APP_v1.md`](https://github.com/kristenmartino/sift/blob/main/docs/ANDROID_APP_v1.md). Sister repos: [`sift`](https://github.com/kristenmartino/sift), [`sift-api`](https://github.com/kristenmartino/sift-api), `sift-mcp`.*
