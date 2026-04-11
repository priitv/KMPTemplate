plugins {
    id("com.android.library")
    id("feature.kmp.compose")
}

android {
    namespace = "android.template.feature.app"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":feature:mymodel:business"))
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
        }
        androidUnitTest.dependencies {
            implementation(libs.mockk)
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
