plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Remove alias(libs.plugins.kotlin.compose), it's redundant with the buildFeatures block

    // Add these two plugin aliases
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)

    alias(libs.plugins.kotlin.compose)

    id("com.google.gms.google-services")

}

android {
    namespace = "com.techmania.maargdarshak"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.techmania.maargdarshak"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.accompanist.permissions)


    // Navigation
    implementation(libs.androidx.navigation.compose) // Add this

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose) // Add this
    implementation(libs.androidx.lifecycle.viewmodel.ktx)     // Add this

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.compose.animation.core.lint)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)


    implementation("com.google.maps.android:maps-compose:4.4.1")
    implementation(libs.androidx.navigation.runtime.ktx)

    implementation(libs.androidx.compose.material.iconsExtended)

    implementation(platform("com.google.firebase:firebase-bom:34.2.0"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


}