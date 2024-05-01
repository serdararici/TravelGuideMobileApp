plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.serdararici.travelguide"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.serdararici.travelguide"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

val nav_version = "2.7.7"
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //Google maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    //Location
    implementation("com.google.android.gms:play-services-location:21.2.0")
    //Places API
    implementation("com.google.android.libraries.places:places:3.4.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Navigation Component
    implementation ("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Retrofit with Scalar Converter
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    //JSON Parsing
    implementation("com.google.code.gson:gson:2.6.1")
    implementation("com.squareup.retrofit2:converter-gson:2.1.0")
    //MVVM
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    //Picasso
    implementation("com.squareup.picasso:picasso:2.71828")





}