# Personal Life Tracker — Project Context

## Product vision

Personal Life Tracker is an offline-first native Android application for helping its user build consistent habits, support academic progress, maintain a healthy routine, and stay accountable through measurable daily and weekly actions. It covers academic work, fitness, sleep/routine, Islamic practices, digital wellbeing, focus, rewards, accountability, and analytics over time.

The product is not a generic scorekeeper: analytics must represent actual measurable behavior, and user-reported religious activity is never inferred automatically.

## Current project status

Phase 1B adds the Compose Task vertical slice over the Phase 1A Room/domain foundation: task list, add/edit, details, category selection/creation, lifecycle actions, navigation, and a compact Dashboard entry point. No scheduler, recurrence, notification, analytics, goal, reward, or penalty behavior is implemented.

## Current MVP phase

**Phase 1B — Task UI**

Scope: Compose screens and ViewModel state for the existing Task foundation. The UI observes Room-backed repository Flows and delegates mutations to Phase 1A use cases.

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
- Current schema contains only `task` and `task_category`. Category references are enforced through a Room foreign key.
- Task and category deletion is a soft archive operation. Archived records remain available for historical use.

### Task data model and rules

A task has a UUID-style ID, title, optional notes and category, date, optional start/deadline times, priority, optional estimated duration, status, optional completion timing/time, creation/update timestamps, and a soft-delete timestamp. Priority is one of `LOW`, `MEDIUM`, or `HIGH`, and defaults to `MEDIUM`. Supported statuses are `PLANNED`, `IN_PROGRESS`, `COMPLETED`, `PARTIAL`, and `MISSED`.

`completionTiming` is present only on `COMPLETED` tasks. Completing at or before a deadline is `ON_TIME`; completing after it is `LATE`. A task without a deadline never becomes missed automatically. The missed-task evaluator is intentionally deferred. `PARTIAL` is explicit and has no percentage/progress tracking.

A category has a UUID-style ID, name, creation/update timestamps, and archive timestamp. Active names are unique case-insensitively at the database level; archiving clears the internal uniqueness key without deleting the record.

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
2. Phase 1A: Task data/domain foundation.
3. Phase 1B: Task UI (next planned checkpoint).
4. Future approved feature areas: Academic, Machine Learning, Fitness, Sleep/Routine, Islamic/Salah, Digital Wellbeing, Focus Mode/Blocking, Rewards, Accountability/Penalties, Analytics, and Settings.

Feature order, schemas, and system permissions for subsequent phases require explicit instruction.

## Completed features

- Product README and Phase 0 project governance documentation.
- Phase 0 Android/Compose foundation and Dashboard shell.
- Phase 1A task/category Room schema, DAO, repository, domain use cases, validation, and unit-test source.
- Phase 1B Tasks list, add/edit form, details screen, category selection/creation, task lifecycle actions, navigation routes, and Dashboard entry point.

## Deferred features

Recurring tasks, missed-task scheduling, task notifications; exams, courses, syllabus and study tracking; math practice; ML sessions/timer; fitness and sleep tracking; Salah, Jama'ah, Qur'an, Hadith, and other Islamic activities; digital wellbeing and UsageStatsManager; focus mode/VPN blocking; rewards; penalties; goal engine; analytics; backup/import; cloud sync.

## Known limitations

The Dashboard is an empty shell. Task/category storage and domain behavior have no UI yet. No missed-task evaluator, notifications, workers, alarms, runtime permissions, or other product features are implemented.

## Current next feature

Phase 1B — Task UI, when explicitly instructed.

## Testing status

Phase 1A adds meaningful unit-test source for task validation, priority defaulting, completion timing, partial status, category uniqueness, category references, and soft archive behavior. As of this working-tree review, verification is blocked before tests run by Gradle cache/workspace failures during KSP dependency transforms; no passing build or test result is claimed.

## Git workflow

Keep changes focused and Git history honest. Run relevant build/tests before reporting completion. Do not commit unless explicitly requested; when requested, make focused commits only after verification.
