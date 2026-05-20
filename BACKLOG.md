# sift-android — backlog

Items not committed to a current milestone. Promote to GitHub issues when work is committed; until then, capture here in prose so nothing gets lost.

> **What goes here:** half-formed ideas, deferred features that don't fit the current tier, quirks worth tracking but not urgent. See [`CLAUDE.md`](./CLAUDE.md) for what belongs where.

## Stretch / nice-to-have

Captured per [`sift/docs/ANDROID_APP_v1.md`](https://github.com/kristenmartino/sift/blob/main/docs/ANDROID_APP_v1.md) §Backlog for v1.1+. Reproduced here so the local backlog stays the single source for in-repo decisions.

- **Glance home-screen widget** ("Today on Sift") — small + medium sizes. Deferred from v1 to keep scope honest; revisit at v1.1 once reader engagement signal exists.
- **Native multi-source compare UI** — port from sift-mcp behavior. Today: link to web `/compare` in Custom Tabs.
- **Native civic dossier views** (politicians, orgs, bills, outlets) — today they open in Custom Tabs. Native pages give us deep-link smoothness + offline.
- **Wear OS companion** — glance + complications.
- **Tablet-optimized layout** — three-pane (categories / feed / detail).
- **Live Updates** (Android 15 API 35) — for developing stories. Tooling is immature in early 2026; revisit at v1.2.
- **Daily-briefing audio** — TalkBack-friendly text-to-speech reading of top stories. Pairs well with Auto.
- **Bookmark collections / read-later** — folders, tags.
- **Reading-level toggle** — once `ReadingLevels` field on `Article` is populated by the pipeline (currently forward-declared in `sift/lib/types.ts`).
- **KMP / Compose Multiplatform refactor** — shared `data/model/` + repository layer with iOS when that lands. Reassess at iOS week 0.

## Bugs / quirks to revisit

- *(Empty for now. Add items as they surface during real dev work.)*

## Considered and rejected

Architectural alternatives discussed and chosen against. Captured so we don't re-litigate; reasoning is easy to revisit if circumstances change.

- **Canonical `/v1/*` mobile API in sift-api** *(deferred, May 2026)* — proposed in the original iOS plan; the cross-functional assessment correctly pushed back that it's the right move at maturity, not pre-PMF. Reuse Next.js routes; only 2 net-new endpoints in sift-api. See [`sift/docs/DECISIONS.md#D33`](https://github.com/kristenmartino/sift/blob/main/docs/DECISIONS.md).
- **Multi-module split (data, ui, domain modules)** *(May 2026)* — premature for solo-dev v1. Revisit once we cross ~50k LOC or sustained multi-developer work. Single `app` module compiles fast enough for now.
- **Material Dynamic Color** *(May 2026)* — Sift's editorial palette is hand-tuned; system-wallpaper-driven colors would override Newsprint / Late Edition intent. Disabled.
- **kapt over KSP** *(May 2026)* — KSP is 2× faster + the modern path. Kapt is on its way out for most ann-processors.
- **Glide over Coil** *(May 2026)* — Coil 3 is Kotlin-first, Compose-native, smaller. Glide is fine but older. New project, new image lib.
- **Compose Multiplatform from day 1** *(May 2026)* — rejected for v1 per [`IOS_VS_ANDROID.md`](https://github.com/kristenmartino/sift/blob/main/docs/IOS_VS_ANDROID.md). Native per platform; reassess KMP factoring at iOS week 0.

---

*Promote items to GitHub issues when you commit to working on them. Don't promote speculatively.*
