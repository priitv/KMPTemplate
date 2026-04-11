plugins {
    id("com.android.library")
    id("feature.kmp.ksp")
}

android {
    namespace = "android.template.feature.business"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
        }
    }
}

dependencies {
    kspAllTargets(libs.koin.ksp.compiler)
}
