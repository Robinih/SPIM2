plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.cvsuagritech.spim"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.cvsuagritech.spim"
        minSdk = 24
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
    buildFeatures{
        mlModelBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // TensorFlow Lite dependencies
    val tfliteVersion = "0.4.4"
    implementation("org.tensorflow:tensorflow-lite-support:$tfliteVersion")
    implementation("org.tensorflow:tensorflow-lite-task-vision:$tfliteVersion")
    implementation("org.tensorflow:tensorflow-lite-metadata:$tfliteVersion")
    
    // Mapbox dependencies (as per official documentation)
    implementation("com.mapbox.maps:android:11.16.0")
    
    // Google Play Services (required by Mapbox)
    implementation("com.google.android.gms:play-services-location:21.0.1")
}