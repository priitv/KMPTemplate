plugins {
    id("com.android.library")
    id("feature.kmp")
}

android {
    namespace = "android.template.feature.di"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:database"))
            implementation(project(":feature:mymodel:app"))
            implementation(project(":feature:mymodel:business"))
            implementation(project(":feature:mymodel:data"))
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
        }
    }
}
