# sift-android — STATUS

**Updated:** 2026-05-20
**Tier:** v1 scaffold (Phase 1 — first commit)
**Velocity:** N/A (just started)

## Active focus

v1 scaffold landed (Phase 1): Gradle setup, Kotlin/Compose module structure, ShareTarget activity stub, Newsprint/Late Edition theme tokens, AndroidManifest with App Links + ACTION_SEND filters. Application code lands in Phase 2 onward.

Canonical decisions: [`sift/docs/ANDROID_APP_v1.md`](https://github.com/kristenmartino/sift/blob/main/docs/ANDROID_APP_v1.md) — KPIs in §2, monetization stance in §3, civic-literacy translation risk called out in §6.

## Open strategic question

**Will the civic-literacy primer + entity chips actually work on a phone screen?**

Biggest design risk from the iOS plan critique, carried forward. The primer panel is ~60 words of prose + 0–4 term cards; the article body has 6+ entity-link chips inline. On a 6.1" portrait screen with thumbs-on-bottom ergonomics, the wall-of-text risk is real.

Resolves with the pre-week-1 design sprint named in `ANDROID_APP_v1.md` §6. Until that lands, every screen design decision in Phase 2 is provisional.

## Next 3

1. **[committed]** Wire `FeedScreen` to `GET https://siftnews.kristenmartino.ai/api/news?category=:id`. Render a `LazyColumn` of `Article` cards. Tier `v1` · `effort-week`.
2. **[committed]** Theme tokens — copy exact hex values from `sift/app/globals.css` for Newsprint + Late Edition (currently placeholder values in `ui/theme/Color.kt`). Verify against the web app on a side-by-side device. Tier `v1` · `effort-day`.
3. **[sketch]** Design sprint output (wireframes for feed, article detail with primer, topic search, share target, settings) — pre-week-1 blocker per the plan. Not a code task but blocks every screen design. Tier `v1` · `effort-week`.

## Blocked-on

- **Design sprint** for civic-literacy mobile translation (named in `ANDROID_APP_v1.md` §6 as "non-negotiable pre-week-1")
- **Google Play Developer account** ($25 one-time, instant) — needed by week 9 for closed beta. Not blocking dev work.
- **FCM project setup** + `google-services.json` — needed by week 8 for push. Not blocking earlier weeks.

## Recent decisions

- **2026-05-20** — **v1 scaffold landed.** Gradle 8.14.3, Kotlin 2.0.21, Compose BOM 2024.12.01, AGP 8.7.3, JDK 17 target. Single `app` module (multi-module split is premature for solo work).
- **2026-05-20** — **Reuse existing Next.js routes** for reads (D33 in `sift/docs/DECISIONS.md`). Only 2 net-new endpoints in sift-api: `/v1/share/sift-this` and `/v1/devices/register`.
- **2026-05-20** — **Material Dynamic Color disabled.** Sift's editorial palette is hand-tuned; system-wallpaper-driven colors would override it. Newsprint / Late Edition tokens copied from web.
- **2026-05-20** — **Single Activity + Compose Navigation.** `ShareTargetActivity` is the only second Activity (handles ACTION_SEND from outside the app).
- **2026-05-20** — **Hilt for DI.** Solo-dev cost is low; compile-time validation pays off as the surface grows. KSP-driven (not kapt).

---

*See also: [`CLAUDE.md`](./CLAUDE.md) (orientation, pre-session ritual), [`BACKLOG.md`](./BACKLOG.md), [`README.md`](./README.md). Canonical decisions: [`sift/docs/ANDROID_APP_v1.md`](https://github.com/kristenmartino/sift/blob/main/docs/ANDROID_APP_v1.md). Sister repos: [`sift`](https://github.com/kristenmartino/sift), [`sift-api`](https://github.com/kristenmartino/sift-api), `sift-mcp`.*
