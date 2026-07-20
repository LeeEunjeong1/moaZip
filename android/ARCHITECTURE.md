# Android Architecture

The Android app uses Clean Architecture, Jetpack Compose, unidirectional MVI,
and feature-based multi-module boundaries.

## Module graph

```text
app
 ├─ feature:dashboard / assets / recurring / import
 ├─ core:data
 └─ core:ui

feature:* ──> core:domain ──> core:model
core:data ──> core:domain
core:ui ────> core:model
```

- `app`: application entry point, navigation, and dependency wiring.
- `feature:*`: screen UI, feature contract, ViewModel, and navigation entry.
- `core:model`: framework-free business models.
- `core:domain`: repository interfaces and use cases. It does not know data sources.
- `core:data`: repository implementations and data-source coordination.
- `core:ui`: shared Compose UI and MVI primitives.

## Feature convention

Each feature should keep the same public shape:

```text
FeatureContract.kt  Intent + State + Effect
FeatureViewModel.kt business orchestration and state reduction
FeatureScreen.kt    Route (state/effect wiring) + stateless Screen
```

Composables send `Intent` only. ViewModels expose immutable `StateFlow<State>`
and one-off `Effect` events. Repository implementations never leak into a
feature module; features depend on domain interfaces and use cases.

## Next modules

Add `core:database` or `core:network` only when a real source is introduced.
Those source modules should be implementation details of `core:data`. Add a
dedicated feature module when a flow can be developed and tested independently.
