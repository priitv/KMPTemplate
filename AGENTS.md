# AGENTS.md

This document provides guidance for AI coding agents contributing to this repository.

## 1. Project Overview

You are an expert mobile developer working on a modern, modular Kotlin Multiplatform (KMP) application targeting **Android and iOS**. The project shares business logic, data layer, and UI across both platforms using Compose Multiplatform.

### Core Technologies
- **UI**: **Compose Multiplatform (JetBrains)**. All composables live in `commonMain` and run on both Android and iOS. Do not suggest XML-based layouts.
- **Architecture**: Clean, modular MVVM with Use Cases. ViewModels expose UI state as `StateFlow`.
- **Asynchronicity**: Kotlin Coroutines and Flow. Use `kotlinx-coroutines-core` in shared source sets.
- **Dependency Injection**: **Koin** with DSL (`module { }`, `viewModelOf`, `single`, `factory`). No annotation processing in multi-module KMP — Koin annotations cannot read across compiled klibs.
- **Database**: **Room KMP** (v2.7+). Entities and DAOs in `commonMain`. Platform-specific builders in `androidMain`/`iosMain`. The `databaseModule` is an `expect/actual` Koin module.
- **Navigation**: **Compose Multiplatform Navigation** (`org.jetbrains.androidx.navigation:navigation-compose`). Same `NavHost`/`composable` API on both platforms.
- **Data Layer**: Repository pattern. Repository interfaces in `business/contract/`, implementations in `data/`.

---

## 2. Project Structure

The project is organized into modules by feature. Each feature is split into four layers: **app**, **business**, **data**, and **di**.

### Source Set Convention
Each KMP module has:
```
src/
├── commonMain/kotlin/     ← shared code (all platforms)
├── androidMain/kotlin/    ← Android-only platform code
├── iosMain/kotlin/        ← iOS-only platform code
├── commonTest/kotlin/     ← shared unit tests
└── androidInstrumentedTest/kotlin/  ← Android instrumented tests
```

### Module Layout
```
{project-name}/
├── buildSrc/                   # Convention plugins — shared build logic for feature modules
│   └── src/main/kotlin/
│       ├── feature.kmp.gradle.kts          # Base: KMP targets, jvmToolchain, commonTest deps, android config
│       ├── feature.kmp.compose.gradle.kts  # feature.kmp + Compose Multiplatform + instrumented test setup
│       ├── feature.kmp.ksp.gradle.kts      # feature.kmp + KSP plugin
│       └── KspHelpers.kt                   # kspAllTargets() — adds KSP compiler to all targets at once
├── app/                        # Android Application entry point
│   └── src/main/kotlin/{package}/
│       ├── App.kt              # startKoin { modules(appModules) } initialization
│       └── app/
│           └── MainActivity.kt
├── shared/
│   └── app/                    # KMP shared app-level module — navigation + iOS entry point
│       └── src/
│           ├── commonMain/kotlin/{package}/shared/app/
│           │   ├── AppNavigation.kt    # @Composable fun AppNavigation() — theme + NavHost
│           │   └── AppModules.kt       # val appModules: List<Module> — all Koin modules
│           └── iosMain/kotlin/{package}/shared/app/
│               └── MainViewController.kt  # iOS entry: ComposeUIViewController + KoinApplication
├── iosApp/                     # iOS Xcode project (Swift bootstrap)
│   └── iosApp/
│       ├── iOSApp.swift        # @main SwiftUI entry point
│       └── ContentView.swift   # Calls MainViewController() from shared_app framework
├── core/
│   ├── database/               # Room KMP — entities, DAOs, expect/actual databaseModule
│   │   └── src/
│   │       ├── commonMain/kotlin/{package}/core/database/
│   │       │   ├── AppDatabase.kt          # @Database, @ConstructedBy
│   │       │   ├── {Entity}.kt             # @Entity data class + @Dao interface
│   │       │   └── DatabaseModule.kt       # expect val databaseModule: Module
│   │       ├── androidMain/kotlin/{package}/core/database/
│   │       │   └── DatabaseModule.kt       # actual — Room.databaseBuilder + Dispatchers.IO
│   │       └── iosMain/kotlin/{package}/core/database/
│   │           └── DatabaseModule.kt       # actual — BundledSQLiteDriver + Dispatchers.Default
│   └── ui/                     # Compose Multiplatform theme (commonMain + platform actuals)
└── feature/
    └── {feature-model}/
       ├── app/                 # UI layer
       │   └── src/
       │       └── commonMain/kotlin/{package}/feature/{feature-model}/app/
       │           ├── {FeatureModel}Screen.kt
       │           ├── {FeatureModel}ViewModel.kt
       ├── business/            # Use cases + repository interfaces
       │   └── src/commonMain/kotlin/{package}/feature/{feature-model}/business/
       │       ├── contract/
       │       │   └── {FeatureModel}Repository.kt  # interface
       │       ├── Get{FeatureModel}UseCase.kt       # interface + impl in same file
       │       └── Add{FeatureModel}UseCase.kt       # interface + impl in same file
       ├── data/                # Repository implementations
       │   └── src/commonMain/kotlin/{package}/feature/{feature-model}/data/
       │       └── {FeatureModel}RepositoryImpl.kt
       └── di/                  # Koin module wiring — business, data, and ViewModels
           └── src/commonMain/kotlin/{package}/feature/{feature-model}/di/
               ├── {FeatureModel}Module.kt     # single<Repo> { Impl(get()) }; factory<UseCase> { ... }
               └── {FeatureModel}AppModule.kt  # viewModelOf(::{FeatureModel}ViewModel)
```

### Layer Responsibilities & Dependency Rules

- `feature/{x}/business` — pure Kotlin, no platform dependencies. Defines use case interfaces and repository contracts. Always `commonMain`.
- `feature/{x}/data` — implements repository interfaces. Depends on `core:database` and `feature/{x}/business`. Always `commonMain`.
- `feature/{x}/di` — All Koin DSL wiring: binds `data` implementations to `business` interfaces, and registers ViewModels. Depends on `app`, `business`, and `data`. Always `commonMain`.
- `feature/{x}/app` — Compose screens and ViewModels. `commonMain` only. Depends on `business` (Use Cases only). No dependency on `di` — Koin resolves ViewModels at runtime via `koinViewModel()`.
- `shared/app` — app-level KMP library. Owns `AppNavigation` (shared NavHost), `appModules` (all Koin modules), and `MainViewController` (iOS entry point). Depends on all feature `di` modules (which bring `app` transitively), `core:ui`, `core:database`.
- `core/database` — Room entities and DAOs in `commonMain`; platform DB builders in `androidMain`/`iosMain`.
- `core/ui` — Theme in `commonMain`; platform status bar effect via `expect/actual`.
- `app/` — Android-only `com.android.application` entry point. Initializes Koin with `startKoin { modules(appModules) }`. Depends on `shared:app`.
- `iosApp/` — Swift/SwiftUI iOS entry point. Calls `MainViewController()` from the `shared_app` KMP framework.

**Important:** ViewModels must never depend on Repository interfaces or Data layer classes directly. Always use Use Cases.

---

## 3. Build and Test Commands

```bash
# Build the project
./gradlew build

# Run unit tests (all platforms)
./gradlew test

# Run Android instrumented tests
./gradlew connectedAndroidTest

# Build iOS framework (for Xcode)
./gradlew :shared:app:linkDebugFrameworkIosSimulatorArm64
```

### Convention Plugins (buildSrc)

Feature module `build.gradle.kts` files use convention plugins to avoid boilerplate:

| Plugin                      | Use in layer       |
|-----------------------------|--------------------|
| `id("feature.kmp")`         | `di`               |
| `id("feature.kmp.ksp")`     | `business`, `data` |
| `id("feature.kmp.compose")` | `app`              |

Minimal feature module template:
```kotlin
plugins {
    id("com.android.library")
    id("feature.kmp.compose")   // or feature.kmp / feature.kmp.ksp
}

android { namespace = "..." }

kotlin {
    sourceSets {
        commonMain.dependencies { /* feature-specific deps only */ }
    }
}
```

**Plugin declarations:** Because `buildSrc` puts plugin JARs on the main build classpath, all `build.gradle.kts` files must declare those plugins without a version using `id("plugin-id")` — not `alias(libs.plugins.xxx)`. This applies to: `com.android.library`, `com.android.application`, `com.android.test`, `org.jetbrains.kotlin.multiplatform`, `org.jetbrains.kotlin.android`, `org.jetbrains.kotlin.plugin.compose`, `org.jetbrains.compose`, `com.google.devtools.ksp`.

---

## 4. Coding Style and Conventions

- **Language**: All code in Kotlin. Swift only for the iOS bootstrap in `iosApp/`.
- **Formatting**: Official Kotlin style guide.
- **Immutability**: Prefer `val` over `var`.
  - **Functions**:
    - Prefer expression body (`=`) over block body (`{ return ... }`).
    - After `=`, the first call, value, or class name starts on the **same line** — never a bare newline after `=`.
    - Each chained `.call()` goes on its own indented line below the first.
    - If a single argument makes the first line too long, it goes on its own indented line.
    - Multiple parameters (in declarations or call sites) always go one per line.
    - Single-line expression bodies are only acceptable when short and immediately readable.
    ```kotlin
    // simple expression body
    fun myModels(): Flow<List<String>> = dao.getAll()

    // builder chain — first call on = line, each .call() below
    fun createDatabase(): AppDatabase = Room
        .databaseBuilder<AppDatabase>(name = dbPath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default)
        .build()

    // single long argument — own line
    fun createDatabase(
        documentsDirPath: String = "MyModel.db"
    ): AppDatabase = Room
        .databaseBuilder<AppDatabase>(
            name = resolveDocumentsDirPath(documentsDirPath)
        )
        .setDriver(BundledSQLiteDriver())
        .build()

    // multiple parameters — always one per line
    class MyViewModel(
        private val getItems: GetItems,
        private val addItem: AddItem,
    ) : ViewModel()
    ```
- **State Management**: `StateFlow` in ViewModels. In composables: `collectAsState()` in `commonMain` (not `collectAsStateWithLifecycle()` — that is Android-only).
- **Error Handling**: Wrap repository calls in a `Result<T>` sealed class or handle in the ViewModel with a sealed UI state (`Loading`, `Success`, `Error`).
- **Coroutines**: Use `kotlinx-coroutines-core` in `commonMain`. Never use `kotlinx-coroutines-android` in shared source sets.
- **DI**:
  - Use Koin DSL (`module { }`) for all module definitions. Do not use `@Module`/`@ComponentScan` annotations in multi-module KMP — they cannot read annotations from compiled klibs on iOS.
  - Inject ViewModels with `viewModelOf(::ViewModel)` from `org.koin.core.module.dsl`.
  - Use `koinViewModel()` in composables for ViewModel injection.
- **Database**:
  - DAOs must use `suspend` for write operations and `Flow` for reactive reads.
  - Always call `.setQueryCoroutineContext(Dispatchers.IO)` (Android) or `.setQueryCoroutineContext(Dispatchers.Default)` (iOS) on the Room builder.
  - iOS requires the full filesystem path to the DB file — use `NSFileManager` to resolve the Documents directory.
- **expect/actual**: Use only for true platform divergence (e.g., DB builder, platform UI effects). Never for business logic.
- **Comments**: Only comment the *why* behind non-obvious decisions.

### Functional Interfaces & Naming

Every class exposes **one function** via `operator fun invoke(...)`. This applies to both business and gateway/repository layers.

Naming rules:
- **`FeatureName`** — the repository/gateway-level interface (defined in `business`, implemented in `repository` or `gateway`)
- **`FeatureNameUseCase`** — the use case interface
- **`FeatureNameUseCaseImpl`** — the use case implementation (lives in the same file as its interface)
- **`FeatureNameImpl`** — the repository/gateway implementation

### Business Layer

`FeatureName` — repository-level interface, declared in `business`:
```kotlin
interface FeatureName {
    operator fun invoke(str: String): Result<Unit>
}
```

`FeatureNameUseCase` — use case interface and implementation in the **same file** (contract and implementation are easy to find together):
```kotlin
interface FeatureNameUseCase {
    operator fun invoke(input: String): Result<Unit> // one line if small and readable enough
}

class FeatureNameUseCaseImpl( // constructor parameters on separate lines
    private val featureName: FeatureName
) : FeatureNameUseCase {
    override fun invoke(
        input: String
    ) = featureName(input) // return type omitted — interface above is the contract
}
```

### Repository / Gateway Layer

`FeatureNameImpl` — implements the `FeatureName` interface from `business`:
```kotlin
class FeatureNameImpl : FeatureName {
    override fun invoke(
        str: String
    ): Result<Unit> = when (str) {
        "success" -> Result.success(Unit)
        else -> Result.failure(IllegalStateException("Error happened"))
    }
}
```

---

## 5. Testing Guidelines

- **Unit Tests** (`commonTest` for platform-agnostic, `androidUnitTest` for JVM-only):
  - Use Kotest matchers (`shouldBe`, `shouldBeTrue`) for assertions. Use `@Test` from `kotlin-test`.
  - Use Turbine for Flow testing.
  - Mock dependencies using MockK. Prefer inline setup with `mockk<T> { every { ... } returns ... }`.
  - ViewModel tests cover state changes.

- **Android Instrumented Tests** (`androidInstrumentedTest`):
  - Use `ComposeTestRule`.
  - Use `KoinTestRule.create { }` from `koin-test-junit4` to replace Koin modules with mock overrides.
  - Use `camelCase` test method names — backtick names with spaces can cause issues with Android's instrumentation runner.

### Unit Test Structure

Test class name matches the class under test (e.g. `FeatureNameUseCaseImplTest`).

```kotlin
class FeatureNameUseCaseImplTest {
    // mock dependencies — inline default behavior if it is always the same
    private val featureName = mockk<FeatureName> {
        every { invoke(any()) } returns Result.success(Unit)
    }

    // expose useCase as a val to mirror how it is injected in production code
    private val useCase = FeatureNameUseCaseImpl(
        featureName = featureName
    )

    // alternatively use a function when you need to pass default invoke parameters
//  private fun useCase(input: String = "default") =
//      FeatureNameUseCaseImpl(featureName = featureName).invoke(input)

    @Test
    fun `When successful call, then return successful result`() { // backtick names allowed in unit tests (commonTest / androidUnitTest); use camelCase for androidInstrumentedTest
        useCase("success").isSuccess.shouldBeTrue()
    }

    @Test
    fun `When this, then that`() {
        val input = "failure"
        val expectedResult = Result.failure<Unit>(IllegalStateException("failure"))
        every { featureName(input) } returns expectedResult

        useCase(input) shouldBe expectedResult
        verify { featureName.invoke(input) }
    }

    // helper assertions can be added at the bottom of the file if needed
    private fun Result<Unit>.shouldBeFailure() =
        shouldBe(Result.failure(IllegalStateException("failure")))
}
```

## 6. Adding a New Feature

When adding a new feature `{FeatureModel}`:

1. Create modules: `feature/{FeatureModel}/business`, `feature/{FeatureModel}/data`, `feature/{FeatureModel}/di`, `feature/{FeatureModel}/app`
2. Use the convention plugins in each module's `build.gradle.kts` (see Section 3): `feature.kmp.compose` for `app`, `feature.kmp.ksp` for `business`/`data`, `feature.kmp` for `di`
3. Define repository interface in `feature/{FeatureModel}/business/contract/`
4. Implement use cases in `feature/{FeatureModel}/business/`; implement repository in `feature/{FeatureModel}/data/`
5. Create `val {FeatureModel}Module: Module = module { single<{FeatureModel}Repository> { {FeatureModel}RepositoryImpl(get()) }; factory<Get{FeatureModel}UseCase> { Get{FeatureModel}UseCaseImpl(get()) } }` in `feature/{FeatureModel}/di`
6. Create `val {FeatureModel}AppModule: Module = module { viewModelOf(::{FeatureModel}ViewModel) }` in `feature/{FeatureModel}/di`
7. Add the screen composable and ViewModel in `feature/{FeatureModel}/app/commonMain`
8. In `shared/app`:
   - Add `feature:{FeatureModel}:di` as a dependency in `build.gradle.kts` (`app` is included transitively)
   - Add `{FeatureModel}Module` and `{FeatureModel}AppModule` to `appModules` in `AppModules.kt`
   - Add the new route to `AppNavigation.kt`

---

## 7. Git Workflow

- **Branch Naming**: `feature/<short-description>` or `fix/<short-description>`
- **Commit Messages**: Clear and descriptive.
- **Pull Requests**: All unit and instrumented tests must pass; no lint errors.
- **Committing**: Do not commit changes unless the user explicitly asks (e.g. "commit", "commit the changes"). Leave changes staged or unstaged for the developer to review and commit themselves.

