import com.android.build.gradle.LibraryExtension
import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("feature.kmp")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

private val catalog = the<VersionCatalogsExtension>().named("libs")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)
        }
        androidMain.dependencies {
            implementation(compose.uiTooling)
        }
        androidInstrumentedTest.dependencies {
            implementation(catalog.findLibrary("androidx-compose-ui-test-junit4").get())
            implementation(catalog.findLibrary("androidx-test-ext-junit").get())
            implementation(catalog.findLibrary("androidx-test-runner").get())
        }
    }
}

pluginManager.withPlugin("com.android.library") {
    configure<LibraryExtension> {
        buildFeatures {
            compose = true
        }
        defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
}
