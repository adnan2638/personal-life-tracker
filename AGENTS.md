# Repository Development Rules

## Required context

Before modifying this repository, read `PROJECT_CONTEXT.md` in full. It is the approved product and architectural record for the Personal Life Tracker.

## Scope and architecture

- Build features only when they are explicitly requested. Work feature-by-feature; do not implement deferred roadmap items pre-emptively.
- Follow the approved native Android architecture: Kotlin, Jetpack Compose, feature-oriented packages, and MVVM/Clean Architecture principles.
- Keep dependencies flowing as `ViewModel -> use case/domain logic -> repository -> data source`.
- UI code must never directly access Room DAOs or Android system APIs.
- Keep UI state immutable and expose state safely from ViewModels.
- Do not redesign approved architecture, introduce modules, or add new platform capabilities without explicit approval. Report architectural conflicts before making a consequential change.

## Data and business rules

- Room is the source of truth for persistent application data.
- Preserve historical records. Changes to current goals or settings must not rewrite history.
- Notifications are derived from application/database state; delivery is never proof that an activity occurred.
- Never automatically infer user-reported religious activity.
- Financial penalties are records only; never transfer money automatically.
- Core MVP functionality must remain offline-first.

## Privacy and permissions

- Do not introduce authentication, cloud sync, advertisements, unnecessary analytics, or unnecessary permissions.
- Do not use `AccessibilityService` as a general-purpose blocker.
- Any future focus blocking must use the approved local VPN direction and must not collect browsing history.
- Analytics must reflect measurable behavior; do not create a universal life score.

## Quality, verification, and Git

- Run the relevant build and tests before reporting work complete. Report exact results and any inability to verify.
- Never create fake tests, fake data, fake Git history, or claim verification that did not happen.
- Keep Git history honest. Make focused commits only after verification and only when explicitly asked to commit.
