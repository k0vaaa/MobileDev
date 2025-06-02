plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "ru.mirea.kovalikaa.mireaproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.mirea.kovalikaa.mireaproject"
        minSdk = 26
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
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation("androidx.work:work-runtime:2.10.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.+")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("com.itextpdf:itext7-core:7.2.5")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)




}