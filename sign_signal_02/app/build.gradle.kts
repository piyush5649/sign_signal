plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.sign_signal_02"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.sign_signal_02"
        minSdk = 24
        targetSdk = 35
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("androidx.camera:camera-camera2:1.2.3") // Or the latest version
    implementation ("androidx.camera:camera-lifecycle:1.2.3") // Or the latest version
    implementation ("androidx.camera:camera-view:1.2.3") // Or the latest version

    implementation ("io.socket:socket.io-client:2.0.0")
    implementation ("com.google.guava:guava:27.0.1-android") // Guava dependency

    implementation ("commons-codec:commons-codec:1.15")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    }



