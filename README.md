# sift-android

Native Android client for [Sift](https://siftnews.kristenmartino.ai) — civic-literacy news reader. Kotlin + Jetpack Compose + Material 3. Pairs with [`kristenmartino/sift`](https://github.com/kristenmartino/sift) (web frontend) and [`kristenmartino/sift-api`](https://github.com/kristenmartino/sift-api) (Python pipeline + write path).

> **Status:** v1 scaffold (Phase 1). Empty UI, working build. Application code lands in follow-up PRs per [`sift/docs/ANDROID_APP_v1.md`](https://github.com/kristenmartino/sift/blob/main/docs/ANDROID_APP_v1.md).

## Quick start

```bash
git clone https://github.com/kristenmartino/sift-android.git
cd sift-android
./gradlew assembleDebug
# or open in Android Studio (Iguana or later) and run on a Pixel 9 emulator (API 35).
```

**Prerequisites:**

- JDK 17 (or later)
- Android Studio Iguana | 2023.2.1 minimum (for Compose Multiplatform compatibility)
- Android SDK with API 35 platform + build-tools 35.0.0

## Tech stack

| Layer | Choice |
|---|---|
| Language | Kotlin 2.0.21 |
| UI | Jetpack Compose + Material 3 |
| Architecture | Single Activity + Compose Navigation, ViewModels with `StateFlow` |
| DI | Hilt (KSP-driven) |
| Networking | Retrofit + OkHttp + kotlinx.serialization |
| Persistence | Room (KSP) + WorkManager |
| Images | Coil 3 |
| Auth | Clerk Android SDK (Phase 2 wire-up) |
| Push | Firebase Cloud Messaging (Phase 2 wire-up) |
| Observability | Sentry Android + PostHog |
| Code style | ktlint via Spotless |
| Build | Gradle 8.14.3, AGP 8.7.3, JDK 17 target |

See [`gradle/libs.versions.toml`](./gradle/libs.versions.toml) for exact versions.

## Project structure

```
sift-android/
├── app/                                    # Application module
│   └── src/main/kotlin/ai/kristenmartino/sift/
│       ├── SiftApplication.kt              # @HiltAndroidApp
│       ├── MainActivity.kt                 # Single activity
│       ├── ui/                             # Screens (Compose)
│       │   ├── SiftApp.kt                  # Root composable
│       │   ├── theme/                      # Newsprint / Late Edition palettes
│       │   └── share/                      # ShareTargetActivity — ACTION_SEND receiver
│       ├── data/                           # API client, Room DB, repositories (Phase 2)
│       ├── di/                             # Hilt modules (Phase 2)
│       ├── nav/                            # NavGraph (Phase 2)
│       ├── push/                           # FCM service (Phase 2)
│       └── work/                           # WorkManager workers (Phase 2)
├── gradle/libs.versions.toml               # Version catalog
└── docs/                                   # (Phase 2 onward)
```

## Cross-repo coordination

This is one of three repos in the Sift family:

| Repo | Role |
|---|---|
| [`kristenmartino/sift`](https://github.com/kristenmartino/sift) | Web frontend (Next.js) — owns user-facing reads + the canonical decisions / planning docs |
| [`kristenmartino/sift-api`](https://github.com/kristenmartino/sift-api) | Python pipeline + LangGraph workflows — owns the write path |
| **`kristenmartino/sift-android`** | This repo. Mobile client. |

When adding features that span repos:

- **API shape changes** → coordinate with sift's `lib/types.ts` + sift-api endpoints
- **New endpoints** → land in sift-api first, then consume here
- **Strategic / cross-product decisions** → record in `sift/docs/DECISIONS.md`

## Status & roadmap

- [`STATUS.md`](./STATUS.md) — Active focus + Open question + Next 3 + Blocked-on + Recent decisions
- [`BACKLOG.md`](./BACKLOG.md) — Deferred / stretch / considered-and-rejected
- [`CLAUDE.md`](./CLAUDE.md) — Agent orientation (pre-session ritual, end-of-PR check)
- [`sift/docs/ANDROID_APP_v1.md`](https://github.com/kristenmartino/sift/blob/main/docs/ANDROID_APP_v1.md) — canonical architecture decisions

## License

Same as the sift family — TBD (likely MIT, mirroring sift's LICENSE).
