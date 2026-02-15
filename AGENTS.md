# AGENTS.md

This document provides guidance for AI coding agents contributing to this repository.

## 1. Project Overview

You are an expert Android developer working on a modern, modular Android application. This project is a native Android app written entirely in Kotlin. It follows Google's official architecture recommendations, including a reactive, single-activity model with a unidirectional data flow (UDF).

### Core Technologies
- **UI**: The user interface is built exclusively with Jetpack Compose and Material 3. Do not suggest XML-based layouts.
- **Architecture**: We use a clean, modular MVVM approach with Use Cases. State is managed with ViewModels, which expose UI state as streams of data.
- **Asynchronicity**: All asynchronous operations are handled using Kotlin Coroutines and Flow.
- **Dependency Injection**: Hilt is used for dependency injection to manage dependencies and enhance testability.
- **Database**: Local data persistence is handled by the Room persistence library.
- **Navigation**: Navigation between screens is managed by Jetpack Navigation for Compose.
- **Data Layer**: The data layer is implemented using the Repository pattern, with data sources for remote and local data.

## 2. Project Structure

The project is organized into modules by feature to promote separation of concerns and scalability. Each feature is split into four layers: **app**, **business**, **data**, and **di**.

The structure is as follows, where parameters within `{}` like `{project-name}`, `{package}`, and `{feature-model}` are placeholders for the actual values:
```
{project-name}/
├── app/                        # Main application module, entry point, and navigation graph
│   └── src/main/java/{package}/
│       ├── Application.kt      # Hilt Application class
│       └── app/
│           ├── MainActivity.kt # Main activity class
│           └── Navigation.kt   # Main navigation host
├── core/
│   ├── database/               # Room database definitions, entities, and DAOs
│   └── network/                # Retrofit/Ktor setup for remote data sources
└── feature/
    └── {feature-model}/        # A self-contained feature module
       ├── app/                 # UI Layer: Screens and ViewModels
       │   └── src/main/java/{package}/feature/{feature-model}/app/
       │       ├── {FeatureModel}Screen.kt
       │       └── {FeatureModel}ViewModel.kt
       ├── business/
       │   └── src/main/java/{package}/feature/{feature-model}/business/
       │       ├── contract/    # Contains interfaces to be implemented by the data layer
       │       └── {FeatureModel}UseCase.kt
       ├── data/                # Data Layer: Repository implementations
       │   └── src/main/java/{package}/feature/{feature-model}/data/
       │       └── {FeatureModel}Repository.kt
       └── di/                  # DI Layer: Hilt modules wiring business and data
           └── src/main/java/{package}/feature/{feature-model}/di/
               ├── BusinessModule.kt
               └── DataModule.kt


```

### Layer Responsibilities & Dependency Rules

- The `app` module orchestrates the application and navigation.
- `feature` modules are self-contained and should not depend on each other directly.
- `core` modules provide shared functionality like database and networking.
- Within a feature module:
  - **app**: Contains Compose Screens and ViewModels. Depends on **business** (for Use Cases) and **di** (to allow Hilt to find bindings). **IMPORTANT**: ViewModels must never depend on Repository interfaces or Data layer classes directly. They must always use Use Cases from the business layer.
  - **business**: Contains the business logic (Use Cases) and defines Repository interfaces. It should have minimal dependencies. It acts as the mediator between the app layer and the data layer.
  - **data**: Implements the Repository interfaces. Depends on **business** and relevant **core** modules.
  - **di**: Provides Hilt `@Module`s that `@Binds` implementations from **data** to interfaces in **business**. Depends on both **data** and **business**.

**Important**: For Hilt to generate code correctly, any module containing `@Inject` or `@HiltViewModel` must have the KSP plugin and the Hilt compiler dependency.

## 3. Build and Test Commands

Use the following Gradle commands:
- **Build the project**: `./gradlew build`
- **Run unit tests**: `./gradlew test`
- **Run instrumented tests**: `./gradlew connectedAndroidTest`

## 4. Coding Style and Conventions

- **Language**: All new code must be written in Kotlin.
- **Formatting**: Adhere to the official Kotlin style guide.
- **Immutability**: Prefer `val` over `var` wherever possible.
- **UI**: All UI components must be created using Jetpack Compose.
- **State Management**: Use `StateFlow` in ViewModels to expose state. In Composables, collect the state using `collectAsStateWithLifecycle()`.
- **Error Handling**: Wrap repository and data source calls in a generic `Result<T>` sealed class to handle success and error states consistently.
- **Database**:
  - Room entities, DAOs, and the database class are located in the `core/database` module.
  - DAOs must be interfaces.
  - All database operations that could block the main thread must be `suspend` functions.
- **Comments**: Avoid adding comments in code. The code should be self-descriptive through clear naming and logical structure. Comments are only acceptable when they explain the *why* (rationale) behind complex or non-obvious decisions that cannot be expressed through code alone.

## 5. Testing Guidelines

- **Unit Tests**:
  - Use JUnit 5 and Turbine for testing Flows.
  - Mock dependencies using MockK.
  - ViewModel tests should cover state changes and events.
- **UI Tests**:
  - Use the `ComposeTestRule`.
  - For UI tests requiring dependency injection, use Hilt's testing capabilities to provide fake or mock implementations of repositories and data sources.

## 6. Git Workflow

- **Branch Naming**: Use the format `feature/<short-description>` for new features and `fix/<short-description>` for bug fixes.
- **Commit Messages**: Write clear and descriptive commit messages.
- **Pull Requests**: Before submitting a pull request, ensure that all unit and UI tests pass and there are no lint errors.
