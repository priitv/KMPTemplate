@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "android.template.test.navigation"
    compileSdk = 36
    targetProjectPath = ":app"

    defaultConfig {
        minSdk = 23
        targetSdk = 36

        testInstrumentationRunner = "android.template.core.testing.HiltTestRunner"
    }

    buildFeatures {
        aidl = false
        buildConfig = false
        shaders = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(project(":app"))
    implementation(project(":feature:mymodel:business"))
    implementation(project(":feature:mymodel:data"))
    implementation(project(":feature:mymodel:di"))
    implementation(project(":core:testing"))
    implementation(project(":feature:mymodel:app"))

    // Testing
    implementation(libs.androidx.test.core)

    // Hilt and instrumented tests.
    implementation(libs.hilt.android.testing)
    ksp(libs.hilt.android.compiler)

    // Compose
    implementation(libs.androidx.compose.ui.test.junit4)
}
