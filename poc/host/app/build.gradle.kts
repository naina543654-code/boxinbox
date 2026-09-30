plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.sandboxpoc.host"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sandboxpoc.host"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0-poc"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}
