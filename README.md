# CMP Navigation 3 Example

![Kotlin](https://img.shields.io/badge/Kotlin-2.3.10-7F52FF?logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.10.x-4285F4?logo=jetpackcompose&logoColor=white)
![Navigation 3](https://img.shields.io/badge/Navigation%203-1.0.0--alpha06-orange)
![License](https://img.shields.io/badge/License-MIT-green)

A reference repository demonstrating **Navigation 3** patterns with **Compose Multiplatform (CMP)** across Android, iOS, Desktop (JVM), and Web (WasmJs).

Navigation 3 replaces `NavController`/`NavHost` with a developer-managed `SnapshotStateList<NavKey>` back stack and a `NavDisplay` composable, giving full control over navigation state without framework magic.

---

## Examples

| # | Example | Location | Description |
|---|---------|----------|-------------|
| 1 | **Basic NavDisplay** | `composeApp/.../navigation/` | Routes as `@Serializable NavKey`, `NavDisplay` with `entryProvider`, screens decoupled via lambdas |
| 2 | **Sealed interface per feature module** | `feature-catalog/`, `feature-profile/` | Each module owns its `sealed interface : NavKey`; registered centrally in `:composeApp` |
| 3 | **Bottom navigation with per-tab back stack** | `composeApp/.../navigation/AppNavigation.kt` | Independent `rememberNavBackStack` per tab; navigation state preserved when switching tabs |
| 4 | **Adaptive layout with SceneStrategy** | `composeApp/.../navigation/AppNavigation.kt` | `rememberListDetailSceneStrategy()` — single pane on phone, list-detail side-by-side on tablet/desktop |
| 5 | **ViewModel scoped to nav entry** | `composeApp/.../examples/viewmodel/` | `viewModel { }` + `rememberViewModelStoreNavEntryDecorator()`; ViewModel destroyed when entry is popped |

---

## Project Structure

```
.
├── androidApp/                  # Android application entry point
├── composeApp/                  # Shared UI, navigation, ViewModels
│   └── src/
│       ├── commonMain/
│       │   ├── navigation/      # NavKeys, NavSerializationConfig, AppNavigation
│       │   ├── features/home/   # HomeScreen + HomeViewModel
│       │   └── examples/        # Standalone example screens (basic, viewmodel)
│       ├── androidMain/
│       ├── iosMain/
│       ├── desktopMain/         # Desktop entry point (main.kt)
│       └── wasmJsMain/          # Web entry point (main.kt)
├── feature-catalog/             # Catalog feature module
├── feature-profile/             # Profile feature module
└── iosApp/                      # Xcode project
```

### Gradle Modules

| Module | Purpose | Plugin |
|--------|---------|--------|
| `:composeApp` | Shared UI, navigation, ViewModels, resources | `com.android.kotlin.multiplatform.library` |
| `:androidApp` | Android application entry point | `com.android.application` |
| `:feature-catalog` | Catalog feature with `CatalogRoute` | `com.android.kotlin.multiplatform.library` |
| `:feature-profile` | Profile feature with `ProfileRoute` | `com.android.kotlin.multiplatform.library` |
| `iosApp` | iOS Xcode project | Swift / SwiftUI |

> **AGP 9+ note**: KMP library modules must use `com.android.kotlin.multiplatform.library`.
> The combination of `com.android.library` + `org.jetbrains.kotlin.multiplatform` is not compatible since AGP 9.0.

---

## Tech Stack

| Library | Version | Purpose |
|---------|---------|---------|
| [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) | 2.3.10 | Shared code across platforms |
| [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) | 1.10.x | Declarative UI |
| [Navigation 3](https://developer.android.com/guide/navigation/navigation-3) | 1.0.0-alpha06 | Back stack management |
| [Material3 Adaptive](https://developer.android.com/develop/ui/compose/layouts/adaptive) | 1.3.0-alpha02 | Adaptive layouts (list-detail) |
| [Lifecycle ViewModel Navigation3](https://developer.android.com/topic/libraries/architecture/viewmodel) | 2.10.0-alpha05 | ViewModel scoped to nav entry |
| [Koin](https://insert-koin.io/) | 4.1.1 | Dependency injection |
| [Ktor](https://ktor.io/) | 3.4.1 | HTTP client |
| [Coil 3](https://coil-kt.github.io/coil/) | 3.4.0 | Image loading |
| Kotlin Serialization | 2.3.10 | Route serialization / polymorphic config |
| Gradle | 9.4 | Build system with Version Catalogs |
| AGP | 9.1.0 | Android Gradle Plugin |

---

## Prerequisites

- **JDK** 17 or later
- **Android Studio** Ladybug (2024.2) or newer
- **Xcode** 15+ (iOS builds only)
- **KDoctor** — verify your environment:

```bash
brew install kdoctor && kdoctor
```

---

## Build & Run

### Android

```bash
./gradlew :androidApp:installDebug
```

### iOS

Open `iosApp/iosApp.xcodeproj` in Xcode, select a simulator, and press **Cmd+R**.

Alternatively, use the **Kotlin Multiplatform** plugin in Android Studio and select the `iosApp` run configuration.

### Desktop (JVM)

```bash
./gradlew :composeApp:run
```

### Web (WasmJs)

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

---

## Architecture

### Navigation 3 Core Concepts

| Concept | Description |
|---------|-------------|
| `NavKey` | Marker interface — all routes implement it |
| `@Serializable` | Required on every route for state restoration on iOS and Web |
| `rememberNavBackStack` | Creates a `SnapshotStateList<NavKey>` managed by the developer |
| `NavDisplay` | Composable that renders the top entry of the back stack |
| `entryProvider { }` | DSL that maps each `NavKey` type to its composable |
| `SavedStateConfiguration` | Polymorphic serialization config — **required** for iOS and Web |

### What's Shared vs Platform-Specific

**Shared (`commonMain`)**
- Route definitions (`NavKey` subclasses)
- Serialization config (`SavedStateConfiguration`)
- Back stack logic (`SnapshotStateList<NavKey>`)
- `NavDisplay`, `NavEntry`, and all composable screens
- ViewModels

**Platform-specific**

| Concern | Android | iOS | Desktop | Web |
|---------|---------|-----|---------|-----|
| Back gesture | Predictive back (system) | Edge pan gesture | Esc key | Esc key |
| Deep link registration | `AndroidManifest.xml` | `Info.plist` | CLI args | `window.location` |
| State restoration | `SavedStateHandle` integration | Process re-launch | App restart | Page reload |
| Browser history | N/A | N/A | N/A | Not synced (see note) |

> **Web / Browser history**: Nav3 does not synchronize the back stack with `window.history`.
> See [navigation3-browser](https://github.com/nicendev/navigation3-browser) for an experimental workaround.
> Official support is expected in Nav3 1.1.0.

---

## Common Anti-patterns

### 1. Missing polymorphic serialization

```kotlin
// ❌ Compiles on Android but CRASHES on iOS and Web at runtime
val backStack = rememberNavBackStack(HomeRoute)

// ✅ Always pass SavedStateConfiguration with all routes registered
val backStack = rememberNavBackStack(navSerializationConfig, HomeRoute)
```

### 2. Passing the back stack directly to screens

```kotlin
// ❌ Tightly coupled — screen has direct knowledge of back stack
@Composable
fun HomeScreen(backStack: SnapshotStateList<NavKey>) {
    Button(onClick = { backStack.add(DetailRoute("123")) }) { ... }
}

// ✅ Decoupled — screen receives navigation lambdas only
@Composable
fun HomeScreen(onNavigateToDetail: (String) -> Unit) {
    Button(onClick = { onNavigateToDetail("123") }) { ... }
}
```

### 3. Forgetting to register new routes

Every new route must be added to `NavSerializationConfig.kt`. Missing registrations
cause a **silent runtime crash** on iOS that is difficult to debug.

```kotlin
polymorphic(NavKey::class) {
    subclass(HomeRoute::class, HomeRoute.serializer())
    subclass(NewRoute::class, NewRoute.serializer()) // ← don't forget this
}
```

### 4. Calling `rememberListDetailSceneStrategy()` conditionally

`rememberListDetailSceneStrategy()` must be called **unconditionally** (Compose rules).
Pass it to `NavDisplay` always; entries without `listPane()`/`detailPane()` metadata
simply render as single pane.

---

## References

- [Navigation 3 — Compose Multiplatform Docs](https://kotlinlang.org/docs/multiplatform/compose-navigation-3.html)
- [Navigation 3 — Android Developers](https://developer.android.com/guide/navigation/navigation-3)
- [Nav3 Recipes — GitHub](https://github.com/android/nav3-recipes)
- [Nav3 Browser support — GitHub](https://github.com/nicendev/navigation3-browser)
- [Compose Multiplatform — GitHub](https://github.com/JetBrains/compose-multiplatform)

---

## License

This project is licensed under the [MIT License](LICENSE).
