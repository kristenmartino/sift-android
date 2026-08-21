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
> **Nothing here is blocking:** the Google Play account ($25) and FCM setup are deferred with the pause. The design sprint is **not** outstanding — it was resolved 2026-05-20 (see Recent decisions); an earlier version of this banner listed it as "deferred", which wrongly implied it was still open.

**Updated:** 2026-07-27 *(paused; content below last updated 2026-05-20)*
**Tier:** v1 (Phase 2 — navigation wired) — **frozen at pause**
**Velocity:** Paused. Was ~2 PRs / day through 2026-05-20.

## Active focus

**None — paused.** State at pause, for whoever picks this up:

Phase 2 underway. **`SiftNavHost`** now wires the two destinations the app has: `feed` (10-category tabs + pager) and `article/{articleId}` (detail screen). Taps on a `FeedHostScreen` card navigate to detail; the detail screen reads the article out of `ArticleStore` (`@Singleton` in `data/repository/`, populated by `ArticleRepository.feed`) — keeps the two ViewModels decoupled and gives Room a drop-in seat at week 6. Source-link CTA opens Chrome Custom Tabs with Sift's Newsprint / Late Edition palette on the toolbar.

Civic-literacy chrome (primer panel + entity chips) on the detail screen was deferred to a follow-up PR pending the design sprint — that sprint resolved 2026-05-20, so the chrome PR was unblocked before the pause, not after it. Hilt + Retrofit + OkHttp + kotlinx.serialization graph in `di/NetworkModule.kt`; data layer in `data/{api,model,repository}/`; UI in `ui/{feed,article}/`.

Canonical decisions: [`sift/docs/ANDROID_APP_v1.md`](https://github.com/kristenmartino/sift/blob/main/docs/ANDROID_APP_v1.md) — KPIs in §2, monetization stance in §3, civic-literacy translation risk called out in §6.

## Open strategic question

**~~Will the civic-literacy primer + entity chips actually work on a phone screen?~~ — answered 2026-05-20.**

Biggest design risk from the iOS plan critique, carried forward. The primer panel is ~60 words of prose + 0–4 term cards; the article body has 6+ entity-link chips inline. On a 6.1" portrait screen with thumbs-on-bottom ergonomics, the wall-of-text risk was real.

**Answer: progressive disclosure with editorial defaults**, from the design sprint at [`sift`#107](https://github.com/kristenmartino/sift/pull/107). Screen designs in Phase 2 are no longer provisional. Final validation was to come from closed beta with 5 web users — that validation has **not** happened and is deferred with the D46 pause, so treat the answer as sound in principle and untested with users.

*This section said "Resolves with the pre-week-1 design sprint… until that lands, every screen design decision in Phase 2 is provisional" for ten weeks after the sprint landed. See [`docs/STATUS_ARCHIVE.md`](docs/STATUS_ARCHIVE.md) (2026-05-20 entry).*

## Next 3 — moved to GitHub

**This section is gone deliberately** (and largely moot while the project is paused).

    gh issue list --state open
    gh pr list --state open

## Blocked-on — moved to GitHub

**Also gone deliberately.**

    gh issue list --state open

(No `blocked` label exists in this repo yet.)

### What this file is for

STATUS.md holds no current state of its own beyond the pause banner above. What has no home in GitHub is the
cross-issue record — an entry below describes what was true on its date and is never edited to stay current.
Architecture-level decisions promote further into
[`sift/docs/DECISIONS.md`](https://github.com/kristenmartino/sift/blob/main/docs/DECISIONS.md) in the sibling repo.

## Recent decisions (last 7 days)

**Entries before 2026-08-13 are archived** in [`docs/STATUS_ARCHIVE.md`](docs/STATUS_ARCHIVE.md).

*(None — the project has been paused since 2026-07-27, and every prior decision predates the 7-day window. See the archive for the full history through the pause.)*

---

*See also: [`CLAUDE.md`](./CLAUDE.md) (orientation, pre-session ritual), [`BACKLOG.md`](./BACKLOG.md), [`README.md`](./README.md). Canonical decisions: [`sift/docs/ANDROID_APP_v1.md`](https://github.com/kristenmartino/sift/blob/main/docs/ANDROID_APP_v1.md). Sister repos: [`sift`](https://github.com/kristenmartino/sift), [`sift-api`](https://github.com/kristenmartino/sift-api), `sift-mcp`.*
