# CLAUDE.md — sift-android (Kotlin + Jetpack Compose)

Orientation for Claude Code sessions. Keep this short and current — if it grows past one screen, split the long bits into real docs.

## Pre-session ritual

Before doing real work in a session:

1. Read [`STATUS.md`](./STATUS.md) — Active focus, Open question, Next 3, Blocked-on, Recent decisions. *(SessionStart hook auto-loads it; no manual `cat` needed when the hook fires.)*
2. List open PRs + issues (`gh pr list`, `gh issue list`, or `mcp__github__list_pull_requests` / `list_issues`).
3. Read [`sift/docs/ANDROID_APP_v1.md`](https://github.com/kristenmartino/sift/blob/main/docs/ANDROID_APP_v1.md) for canonical decisions if the work touches scope / tier choices.

If `STATUS.md` is older than ~3 days during active development, flag the staleness to the user before starting.

## End-of-PR doc-impact check

Before opening the PR:

- Did this change anything in `STATUS.md`'s Next 3, Blocked-on, or Open question? Update it.
- Did this make or close a strategic decision? Add a `## Recent decisions` entry in `STATUS.md` and (if substantial) a row in [`sift/docs/DECISIONS.md`](https://github.com/kristenmartino/sift/blob/main/docs/DECISIONS.md) — the cross-product ADR log.
- Did this change a public contract (API call, deep link, data model)? Update [`README.md`](./README.md) + verify `data/api/` matches `sift/lib/types.ts`.
- Did this change how the app boots / runs locally? Update the Quick Start in `README.md`.
- Added a new dependency? It must go through `gradle/libs.versions.toml` — never inline `implementation("group:artifact:1.2.3")`.

## Architecture conventions

Codified once so PRs don't drift. If a PR breaks a convention, the convention either gets updated here (with a `## Recent decisions` entry) or the PR adapts.

1. **Single Activity** + Compose Navigation. `ShareTargetActivity` is the only exception (Android requires a separate Activity for external `ACTION_SEND` receiver).
2. **MVI-ish ViewModel pattern**: each screen has a ViewModel that exposes a single `StateFlow<UiState>` representing the screen state. UI emits events; ViewModel reduces them.
3. **Hilt DI** (KSP). Every Repository / Service / ViewModel is `@HiltViewModel` or `@Inject`-constructed. No service locators, no manual graphs.
4. **Networking: Retrofit + OkHttp + kotlinx.serialization**. Domain models in `data/model/` mirror `sift/lib/types.ts` (TypeScript is the source of truth).
5. **Persistence: Room** for bookmarks + offline article cache. WorkManager for periodic sync. No raw SQLite.
6. **Theme**: all colors / typography go through `MaterialTheme.colorScheme` / `MaterialTheme.typography`. Never hard-code `Color(0xFF...)` in screens — add to `ui/theme/Color.kt`.
7. **Composables**: PascalCase, `@Composable`-annotated, ktlint's `function-naming` rule is disabled for this project (Compose convention overrides Kotlin).
8. **Tests** — for ViewModels, use `app.cash.turbine.Turbine` to test Flow emissions. For Compose UI, use `androidx.compose.ui.test.junit4`.

## Cross-repo coordination

| Repo | Role | Touches sift-android via |
|---|---|---|
| [`kristenmartino/sift`](https://github.com/kristenmartino/sift) | Web frontend; canonical decisions doc | `lib/types.ts` (data shapes), `app/globals.css` (theme tokens), `docs/ANDROID_APP_v1.md` (decisions) |
| [`kristenmartino/sift-api`](https://github.com/kristenmartino/sift-api) | Python pipeline + write path | Two net-new endpoints we'll consume: `POST /v1/share/sift-this`, `POST /v1/devices/register` |
| `kristenmartino/sift-mcp` | MCP server | None directly. Distinct ship cadence. |

Commits do **not** cross repos. If a change requires coordinated landings, open paired PRs and call out the dependency in both descriptions.

## See also

- [`README.md`](./README.md) — quick start, tech stack, project structure
- [`STATUS.md`](./STATUS.md) — active state
- [`BACKLOG.md`](./BACKLOG.md) — deferred / stretch
- [`sift/docs/ANDROID_APP_v1.md`](https://github.com/kristenmartino/sift/blob/main/docs/ANDROID_APP_v1.md) — canonical architecture decisions
- [`sift/docs/HOW_IT_WORKS.md`](https://github.com/kristenmartino/sift/blob/main/docs/HOW_IT_WORKS.md) — full system end-to-end
- [`sift/docs/DECISIONS.md`](https://github.com/kristenmartino/sift/blob/main/docs/DECISIONS.md) — cross-product ADR log (D1–D34+)
