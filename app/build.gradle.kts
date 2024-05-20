plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    kotlin("kapt")
}

android {
    namespace = "com.mdev.chatapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mdev.chatapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.datastore.core.android)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.preference)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Version define
    val accompanist = "0.31.4-beta"
    val hilt = "2.51.1"
    val retrofit = "2.9.0"
    val nav = "2.7.5"
    val lottie = "6.2.0"
    val coil = "2.5.0"
    val datastore = "1.0.0"
    val room = "2.6.1"
    val jwt = "4.4.0"
    val compose = "1.0.5"
    // accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanist")

    // Dagger - Hilt
    implementation("com.google.dagger:hilt-android:$hilt")
    annotationProcessor("com.google.dagger:hilt-compiler:$hilt")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    ksp("com.google.dagger:dagger-compiler:$hilt") // Dagger compiler
    ksp("com.google.dagger:hilt-compiler:$hilt")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofit")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit")

    // Navigation
    implementation("androidx.navigation:navigation-compose:$nav")

    // Lottie
    implementation("com.airbnb.android:lottie-compose:$lottie")

    // Coil
    implementation("io.coil-kt:coil:$coil")
    implementation("io.coil-kt:coil-compose:$coil")

    // Data store
    implementation("androidx.datastore:datastore-preferences:$datastore")

    implementation("androidx.room:room-runtime:$room")
    implementation("androidx.room:room-ktx:$room")
    annotationProcessor("androidx.room:room-compiler:$room")
    ksp("androidx.room:room-compiler:$room")

    // Jwt - Decode
    implementation("com.auth0:java-jwt:$jwt")
    //
    implementation("androidx.compose.material:material-icons-extended: $compose")
}
