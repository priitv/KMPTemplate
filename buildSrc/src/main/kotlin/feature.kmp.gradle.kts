import com.android.build.gradle.LibraryExtension
import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

private val catalog = the<VersionCatalogsExtension>().named("libs")

kotlin {
    jvmToolchain(21)

    androidTarget()
    iosArm64()
    iosSimulatorArm64()
    iosX64()

    sourceSets {
        commonTest.dependencies {
            implementation(catalog.findLibrary("kotlinx-coroutines-test").get())
            implementation(catalog.findLibrary("kotest").get())
            implementation(kotlin("test"))
        }
    }
}

pluginManager.withPlugin("com.android.library") {
    configure<LibraryExtension> {
        compileSdk = 36
        defaultConfig {
            minSdk = 23
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
    }
}
