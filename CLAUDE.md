# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
ng serve          # Dev server at localhost:4200
ng build          # Production build
ng build --watch --configuration development  # Incremental dev build
ng test           # Unit tests (Karma/Jasmine)
```

No lint script is configured. No e2e tests are set up despite the README mentioning them.

## Architecture

**Angular 18 standalone application** — no `NgModule` anywhere. Entry point is `src/main.ts` → `bootstrapApplication(AppComponent, appConfig)`.

**Single feature page.** All business logic, UI, and state live in one component:
- `src/app/pages/incident-monitoring/incident-monitoring.component.ts`

The router (`app.routes.ts`) has one real route (`/incident-monitoring`) and a redirect from `/`. `AppComponent` is a thin shell containing only `<router-outlet />`.

**No services, no state management library, no HTTP calls.** All data is hardcoded constants defined inline in the component file. There are no environment files (`src/environments/` does not exist).

## Key Patterns

- **Template syntax:** Uses Angular 17+ built-in control flow (`@if`, `@for`, `@let`) — not structural directives (`*ngIf`, `*ngFor`).
- **Derived state:** `regions`, `areas`, `branches`, and `incidents` are TypeScript `get` accessors that recompute from `allIncidents` on every access. No memoization.
- **Alert simulation:** Clicking the PIC name in the nav reveals a dropdown to toggle simulated incidents. Each active alert runs three timers (1.5s footage-ready delay, 1s elapsed counter, 60s auto-stop). These are cleaned up in `ngOnDestroy`.
- **SCSS tokens:** Component styles define private SCSS variables (`$bg-page`, `$bg-card`, etc.) at the top of the `.scss` file. No CSS custom properties or external token system.
- **Icons:** Inline SVG only — no icon library dependency.

## Assets

CCTV video clips are served from `src/asset/` (mapped to `/asset` in the build output):
- `asap_1.mp4` — Fire/Smoke footage
- `asap_2.mp4` — ATM Loitering footage
- `unatorized_access.mp4` — Unauthorized Access footage

After Hours Anomaly has `videoSrc: null` and always shows the "Waiting for Footage" placeholder.

## Builder

Uses `@angular-devkit/build-angular:application` (Vite-based, Angular 17+), not the legacy Webpack builder. Component stylesheet default is SCSS.
