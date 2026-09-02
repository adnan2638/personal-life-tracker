# Personal Life Tracker — Project Context

## Product vision

Personal Life Tracker is an offline-first native Android application for helping its user build consistent habits, support academic progress, maintain a healthy routine, and stay accountable through measurable daily and weekly actions. It covers academic work, fitness, sleep/routine, Islamic practices, digital wellbeing, focus, rewards, accountability, and analytics over time.

The product is not a generic scorekeeper: analytics must represent actual measurable behavior, and user-reported religious activity is never inferred automatically.

## Current project status

The repository began as documentation only. Phase 0 establishes the native Android application foundation and Dashboard shell. No product feature, future data entity, runtime permission, or system integration is implemented in this phase.

## Current MVP phase

**Phase 0 — Android Foundation**

Scope: Android/Compose project setup, design-system theme, dependency injection, Room and DataStore infrastructure, navigation foundation, empty Dashboard shell, test foundation, and project documentation.

## Approved tech stack

| Area | Choice |
| --- | --- |
| Platform / language | Native Android / Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | Feature-oriented packages; MVVM/Clean Architecture principles |
| Persistent data | Room (source of truth) |
| Preferences | DataStore |
| Dependency injection | Hilt |
| Async / state | Coroutines + Flow |
| Navigation | Navigation Compose |
| Background work | WorkManager |
| Precise schedules, when needed | AlarmManager |
| Notifications | Android notification system |
| Digital wellbeing, later | UsageStatsManager |
| Focus blocking, later | Local `VpnService` |
| Authentication / cloud backend | None for MVP |

## Architecture

Features follow this dependency path:

`Compose UI -> ViewModel -> use case/domain logic -> repository -> data source`

UI state is immutable. UI does not directly access Room DAOs or Android system APIs. The initial single `:app` module is organized by feature and shared core packages; extract Gradle modules only when a concrete architectural reason emerges.

### Long-term package/module direction

Potential future modules are `:app`, `:core:common`, `:core:database`, `:core:domain`, `:core:designsystem`, `:core:notifications`, `:core:scheduling`, and feature modules for dashboard, tasks, academic, ML, fitness, sleep, Islamic, wellbeing, focus, rewards, analytics, and settings. These are direction only, not a mandate to create all modules now.

## Database architecture principles

- Room is the source of truth for persistent application data.
- Repositories mediate data access; DAOs remain in the data layer.
- Database schema versions and migrations are explicit and tested as schema changes are introduced.
- Historical records are append/preserve-oriented. Updating current goals or preferences must not rewrite previous records.
- Phase 0 establishes only database infrastructure; it intentionally defines no future feature tables.

## Core business rules and architectural invariants

1. Core MVP functions work without internet.
2. Notifications are derived from application/database state, and delivery is never proof that an activity occurred.
3. Religious activities are recorded only through user action; they are never automatically inferred.
4. Financial penalties are records only and never cause automatic money transfers.
5. Do not use AccessibilityService as a general-purpose content blocker.
6. Future blocking uses local VPN architecture and must not collect browsing history.
7. Do not add authentication, cloud sync, advertisements, unnecessary analytics, or unnecessary permissions without approval.
8. Do not implement future features merely because this document describes them.

## Notification and permission strategy

Notification channels, scheduling abstractions, WorkManager, and AlarmManager are introduced only alongside a concrete feature. Exact alarms are reserved for precise user-facing schedules where appropriate. Phase 0 requests no notification, usage-access, VPN, or exact-alarm permissions.

## Roadmap

1. Phase 0: Android foundation and Dashboard shell.
2. Future approved feature areas: Dashboard, Tasks, Academic, Machine Learning, Fitness, Sleep/Routine, Islamic/Salah, Digital Wellbeing, Focus Mode/Blocking, Rewards, Accountability/Penalties, Analytics, and Settings.

Feature order, schemas, and system permissions for subsequent phases require explicit instruction.

## Completed features

- Product README and Phase 0 project governance documentation.
- Phase 0 application foundation (in progress until verified).

## Deferred features

Tasks and recurring tasks; exams, courses, syllabus and study tracking; math practice; ML sessions/timer; fitness and sleep tracking; Salah, Jama'ah, Qur'an, Hadith, and other Islamic activities; digital wellbeing and UsageStatsManager; focus mode/VPN blocking; rewards; penalties; goal engine; analytics; backup/import; cloud sync.

## Known limitations

The Dashboard is an empty shell. No Room entities, DAOs, repositories, feature use cases, notifications, workers, alarms, runtime permissions, or product data are present in Phase 0.

## Current next feature

Await explicit product direction for the first functional feature. The likely next implementation is a scoped Dashboard feature or the first approved tracker, but no assumption should be treated as approval.

## Testing status

Phase 0 provides unit-test and Android instrumentation-test foundations. Build and test results must be updated from actual verification, not assumed.

## Git workflow

Keep changes focused and Git history honest. Run relevant build/tests before reporting completion. Do not commit unless explicitly requested; when requested, make focused commits only after verification.
