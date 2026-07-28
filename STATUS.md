# sift-android — STATUS

> ## ⏸ PAUSED — 2026-07-27
>
> **Android v1 is paused for 90 days.** Decision D46, recorded in [`sift/docs/DECISIONS.md`](https://github.com/kristenmartino/sift/blob/main/docs/DECISIONS.md); full reasoning in [`sift/docs/LAUNCH_DECISION_MEMO.md`](https://github.com/kristenmartino/sift/blob/main/docs/LAUNCH_DECISION_MEMO.md).
>
> **Why:** twelve weeks of build against zero validated demand was the largest resource question on the board. Sift is launched and unadopted — effectively zero users, $0 revenue — and the 90-day plan spends its hours finding out whether anyone wants the product that already exists, not adding a second client for it.
>
> **This work is preserved, not abandoned.** Phase 2 is complete and sound: `SiftNavHost`, the feed with category tabs + pager, article detail via `ArticleStore`, Chrome Custom Tabs with the Sift palette. Nothing below is being reverted. The everything-below section describes the state at pause, not work in progress.
>
> **Un-pause condition:** the Q1 wedge-user question closes with a wedge whose behavior is mobile-shaped, *and* the week-one evidence test in the launch memo draws real replies. Absent both, this stays paused. Do not resume on enthusiasm.
>
> **Nothing here is blocking:** the design sprint, the Google Play account ($25), and FCM setup are all deferred with it.

**Updated:** 2026-07-27 *(paused; content below last updated 2026-05-20)*
**Tier:** v1 (Phase 2 — navigation wired) — **frozen at pause**
**Velocity:** Paused. Was ~2 PRs / day through 2026-05-20.

## Active focus

**None — paused.** State at pause, for whoever picks this up:

Phase 2 underway. **`SiftNavHost`** now wires the two destinations the app has: `feed` (10-category tabs + pager) and `article/{articleId}` (detail screen). Taps on a `FeedHostScreen` card navigate to detail; the detail screen reads the article out of `ArticleStore` (`@Singleton` in `data/repository/`, populated by `ArticleRepository.feed`) — keeps the two ViewModels decoupled and gives Room a drop-in seat at week 6. Source-link CTA opens Chrome Custom Tabs with Sift's Newsprint / Late Edition palette on the toolbar.

Civic-literacy chrome (primer panel + entity chips) on the detail screen is deferred to a follow-up PR pending the design sprint. Hilt + Retrofit + OkHttp + kotlinx.serialization graph in `di/NetworkModule.kt`; data layer in `data/{api,model,repository}/`; UI in `ui/{feed,article}/`.

Canonical decisions: [`sift/docs/ANDROID_APP_v1.md`](https://github.com/kristenmartino/sift/blob/main/docs/ANDROID_APP_v1.md) — KPIs in §2, monetization stance in §3, civic-literacy translation risk called out in §6.

## Open strategic question

**Will the civic-literacy primer + entity chips actually work on a phone screen?**

Biggest design risk from the iOS plan critique, carried forward. The primer panel is ~60 words of prose + 0–4 term cards; the article body has 6+ entity-link chips inline. On a 6.1" portrait screen with thumbs-on-bottom ergonomics, the wall-of-text risk is real.

Resolves with the pre-week-1 design sprint named in `ANDROID_APP_v1.md` §6. Until that lands, every screen design decision in Phase 2 is provisional.

## Next 3

1. **[sketch]** Design sprint output (wireframes for feed, article detail with primer, topic search, share target, settings) — pre-week-1 blocker per the plan. Running in parallel with code work; civic-literacy chrome PR blocks on this. Tier `v1` · `effort-week`.
2. **[committed]** Civic-literacy chrome on detail screen — `whyItMatters` primer panel + entity chips. Blocked on the design sprint; the data is already on the wire (`Article.whyItMatters: String?`). Tier `v1` · `effort-week`.
3. **[committed]** Topic search screen — third route in `SiftNavHost` (`search`). Reuse `ArticleCard`; hit `/api/news?q=`. Tier `v1` · `effort-day`.

## Blocked-on

- **Design sprint** for civic-literacy mobile translation (named in `ANDROID_APP_v1.md` §6 as "non-negotiable pre-week-1")
- **Google Play Developer account** ($25 one-time, instant) — needed by week 9 for closed beta. Not blocking dev work.
- **FCM project setup** + `google-services.json` — needed by week 8 for push. Not blocking earlier weeks.

## Recent decisions

- **2026-05-20** — **ArticleDetailScreen + NavHost + Custom Tabs.** First navigable surface beyond the feed. `SiftNavHost` introduces routes `feed` and `article/{articleId}`. `ArticleStore` (new `@Singleton` in `data/repository/`) is the in-memory bridge between fetch (populated by `ArticleRepository.feed`) and detail (read by `ArticleDetailViewModel`); swap to Room at week 6 without touching call sites. Process-death fallback: in-memory store returns `null` after kill → detail VM emits `Missing` state with explanatory copy + back button (SavedStateHandle restoration is a v1.1 polish item). Custom Tabs over `Intent.ACTION_VIEW` so source links keep the Sift palette on the toolbar — `NewsprintPaper` light / `LateEditionBg` dark. Civic-literacy chrome (primer + entity chips) deferred to the next PR pending the design sprint. `accentColor()` extracted from `ArticleCard`'s private helper to `ui/theme/CategoryAccent.kt` so detail screen + future chips can share it.
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
