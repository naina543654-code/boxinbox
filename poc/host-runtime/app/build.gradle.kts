plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.sandboxpoc.hostruntime"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sandboxpoc.hostruntime"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0-poc-runtime"
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

dependencies {
    // BlackBox engine, built from source (zitanioi/blackbox) via
    // ../engine/build-aar-manual.sh. Rebuild the AAR on a normal machine
    // with: cd ../engine && ./build-aar-manual.sh all
    implementation(files("libs/Bcore-release.aar"))
    // Engine runtime dependencies (also baked into the manual APK build):
    // BlackReflection shim (JitPack) + FreeReflection (JitPack, official
    // tiann artifact — the me.weishu:free_reflection:3.0.1 coordinate the
    // engine declares is dead) + androidx.
    implementation("com.github.CodingGay.BlackReflection:core:1.1.2")
    implementation("com.github.tiann:FreeReflection:3.2.2")
    implementation("androidx.annotation:annotation:1.1.0")
    implementation("androidx.core:core:1.3.0")
    implementation("androidx.appcompat:appcompat:1.2.0")
}
