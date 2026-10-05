plugins {
    id("com.android.application")
    kotlin("android")
}

// Build stamp: git SHA + date baked into res/values/version.xml and versionName,
// so an installed APK is always identifiable (footer on the home screen).
// Falls back to "unknown" outside a git checkout.
fun gitOut(vararg args: String): String = try {
    Runtime.getRuntime().exec(arrayOf("git", "-C", "${rootDir}/..") + args)
        .inputStream.bufferedReader().readText().trim().ifEmpty { "unknown" }
} catch (e: Exception) { "unknown" }
val buildSha = gitOut("rev-parse", "--short", "HEAD")
val buildDate = gitOut("show", "-s", "--format=%cs", "HEAD")

android {
    namespace = "com.sandboxpoc.hostruntime"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sandboxpoc.hostruntime"
        minSdk = 33
        targetSdk = 35
        // Human-readable release series (bump manually per release: 1.1, 1.2, ...).
        // Exact build identity (git SHA + date) lives in the version.xml footer
        // string below, not in versionName.
        versionCode = 2
        versionName = "1.1"
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

// Build stamp: version.xml is written directly into src/main/res/values
// (gitignored — see .gitignore). Earlier revisions used a sourceSets hack to
// add a generated res dir; it silently dropped src/main/res on Jason's Gradle
// builds (missing icon, 2026-10-05). Direct write is bulletproof.
val generateVersionRes by tasks.registering {
    val outFile = layout.projectDirectory.file("src/main/res/values/version.xml")
    // Declare the stamp as inputs: without them Gradle's up-to-date check
    // skips this task after the first build and the footer keeps showing
    // the PREVIOUS checkout's SHA (versionName stays fresh, footer stale).
    inputs.property("buildSha", buildSha)
    inputs.property("buildDate", buildDate)
    outputs.file(outFile)
    doLast {
        outFile.asFile.parentFile.mkdirs()
        outFile.asFile.writeText(
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n" +
            "    <string name=\"build_version\">$buildSha ($buildDate)</string>\n</resources>\n"
        )
    }
}
tasks.named("preBuild") { dependsOn(generateVersionRes) }

dependencies {
    // BlackBox engine, built from source (zitanioi/blackbox) via
    // ../engine/build-aar-manual.sh. Consumed DIRECTLY from the tracked
    // engine/ tree so a `git pull` always brings the current engine —
    // a hand-copied app/libs/Bcore-release.aar silently goes stale
    // (caused the 2026-10-02 stale-spoof + broken-wipe episode).
    implementation(files("../../engine/Bcore-release.aar"))
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
