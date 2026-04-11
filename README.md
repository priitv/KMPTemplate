# KMP Multimodule Template

A Kotlin Multiplatform template for Android and iOS apps using Compose Multiplatform. Clone this repo and run the customizer script to start a new project — it renames the package, data model, and app name throughout the codebase.

## Prerequisites

- **Android Studio** (latest stable) with the Kotlin Multiplatform plugin
- **Xcode** (for iOS builds)
- **JDK 21**
- **Bash 4+** — macOS ships with Bash 3; install a newer version via Homebrew if needed:
  ```bash
  brew install bash
  ```

## Starting a new project

```bash
bash customizer.sh <package> <DataModel> [ApplicationName]
```

| Argument | Example | Description |
|---|---|---|
| `package` | `com.example.myapp` | Root package name for all Kotlin source files |
| `DataModel` | `Product` | Name for the primary domain model (replaces `MyModel`) |
| `ApplicationName` | `MyApp` | Android `Application` class name (optional, defaults to `MyApplication`) |

**Example:**
```bash
bash customizer.sh com.example.shop Product ShopApp
```

The script:
1. Moves source files into the new package directory structure
2. Renames `android.template` → your package in all `.kt` and `.kts` files
3. Renames `MyModel`/`myModel`/`mymodel` → your data model name
4. Renames `MyApplication` → your application name (if provided)
5. Removes itself and the `.git/` directory when done

After running the script, open the project in Android Studio, sync Gradle, and you're ready to build.

## Project structure

The template is organised into feature modules, each split into four layers:

| Layer | Purpose |
|---|---|
| `feature/{x}/app` | Compose screens and ViewModels |
| `feature/{x}/business` | Use case interfaces + implementations, repository contracts |
| `feature/{x}/data` | Repository implementations |
| `feature/{x}/di` | Koin wiring — binds data to business interfaces, registers ViewModels |

Shared navigation, Koin module list, and the iOS entry point live in `shared/app`. The Android entry point is in `app/`.

See **[AGENTS.md](AGENTS.md)** for full architecture details, coding conventions, and testing guidelines.
