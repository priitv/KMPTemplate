plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {
    jvmToolchain(21)

    androidTarget()
    listOf(iosArm64(), iosSimulatorArm64(), iosX64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared_app"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:ui"))
            implementation(project(":core:database"))
            implementation(project(":feature:mymodel:app"))
            implementation(project(":feature:mymodel:di"))
            implementation(libs.androidx.navigation.compose)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
        }
    }
}

android {
    namespace = "android.template.shared.app"
    compileSdk = 36
    defaultConfig { minSdk = 23 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
