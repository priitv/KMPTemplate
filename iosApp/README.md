# iOS App

This directory contains the iOS entry point for the KMP application.

## First-time setup (required before opening in Xcode/Android Studio)

The KMP framework must be pre-built at least once before Xcode can resolve Swift module imports.
Run from the **project root**:

```bash
# For simulator (iPhone in Android Studio)
./gradlew :feature:mymodel:app:linkDebugFrameworkIosSimulatorArm64

# For physical device
./gradlew :feature:mymodel:app:linkDebugFrameworkIosArm64
```

After that, Android Studio's KMP plugin will keep the framework up-to-date automatically on every run.

## Running from Android Studio

1. Open the project root in Android Studio (Hedgehog or later with Kotlin Multiplatform plugin)
2. Run the pre-build command above once
3. Select the **iosApp** run configuration and choose an iPhone simulator

## Running from Xcode

Open `iosApp/iosApp.xcodeproj`. The "Compile Kotlin Framework" build phase calls Gradle automatically.

## Structure

- `iOSApp.swift` — SwiftUI app entry point
- `ContentView.swift` — Wraps the shared `MainViewController` from KMP
- `Configuration/Config.xcconfig` — Xcode build settings (framework search paths, product name)
