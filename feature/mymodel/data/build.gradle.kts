plugins {
    id("com.android.library")
    id("feature.kmp.ksp")
}

android {
    namespace = "android.template.feature.data"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:database"))
            implementation(project(":feature:mymodel:business"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
        }
    }
}

dependencies {
    kspAllTargets(libs.koin.ksp.compiler)
}
