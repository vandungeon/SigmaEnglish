plugins {
   alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.sigmaenglish"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sigmaenglish"
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.hilt.android)
    implementation(libs.androidx.constraintlayout.core)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.databinding.adapters)
    implementation(libs.firebase.crashlytics.buildtools)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.gson)
    implementation(libs.androidx.datastore.preferences)


    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.room.guava)
    implementation(libs.androidx.core.ktx.v1120)

    implementation(libs.androidx.lifecycle.viewmodel.ktx.v284)
    implementation(libs.androidx.lifecycle.livedata.ktx.v262)
    implementation (libs.google.accompanist.navigation.animation)

    implementation(libs.hilt.android.v250)
    kapt(libs.hilt.android.compiler.v250)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.fragment)
    implementation(libs.androidx.core.ktx.v1131)
    implementation(libs.androidx.lifecycle.runtime.ktx.v270)
    implementation(libs.androidx.activity.compose.v190)
    implementation(platform(libs.androidx.compose.bom.v20240600))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.runtime.livedata.v167)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    kapt (libs.hilt.compiler)
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.espresso.core.v361)
    androidTestImplementation(platform(libs.androidx.compose.bom.v20240600))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.lifecycle.viewmodel.compose)//google viewmodel setup
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}