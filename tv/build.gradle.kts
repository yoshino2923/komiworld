plugins {
    id(libs.plugins.androidApplication.get().pluginId)
    id(libs.plugins.kotlinAndroid.get().pluginId)
    id(libs.plugins.kotlinCompose.get().pluginId)
}

android {
    namespace = "com.yosh.tv"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.yosh.tv"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.tv.foundation)
    implementation(libs.tv.material)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    implementation(libs.remote.creation.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // Extra Material Icons
    implementation("androidx.compose.material:material-icons-extended:1.6.1")

    // Material 3 for Android TV
    implementation("androidx.tv:tv-material:1.0.0")

    // ViewModel + Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.8.5")

    // Coil (Images)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Kotlinx Serialization (JSON)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Media3 (ExoPlayer)
    implementation("androidx.media3:media3-exoplayer:1.5.0")
    implementation("androidx.media3:media3-ui:1.5.0")

    // Splash Screen (Android 12+)
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Baseline Profile Installer
    implementation("androidx.profileinstaller:profileinstaller:1.3.1")

    // Compose Preview (debug only)
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.1")

}
