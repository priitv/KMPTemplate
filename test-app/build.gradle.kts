plugins {
    id("com.android.test")
}

android {
    namespace = "android.template.test.navigation"
    compileSdk = 36
    targetProjectPath = ":app"

    defaultConfig {
        minSdk = 23
        targetSdk = 36
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(project(":shared:app"))
    implementation(project(":feature:mymodel:business"))
    implementation(project(":feature:mymodel:data"))
    implementation(project(":feature:mymodel:di"))
    implementation(project(":feature:mymodel:app"))

    implementation(libs.androidx.test.core)
    implementation(libs.androidx.compose.ui.test.junit4)
    implementation(libs.koin.android)
    implementation(libs.koin.test)
    implementation(libs.koin.test.junit4)
    implementation(libs.mockk)
}
