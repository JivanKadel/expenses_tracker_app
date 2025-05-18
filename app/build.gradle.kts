plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.jivan.expense_tracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jivan.expense_tracker"
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
    }
    sourceSets {
        getByName("main") {
            res {
                srcDirs(
                    "src\\main\\res",
                    "src\\main\\res\\layouts\\auth",
                    "src\\main\\res",
                    "src\\main\\res\\layouts\\expenses",
                    "src\\main\\res",
                    "src\\main\\res\\layouts\\reports",
                    "src\\main\\res",
                    "src\\main\\res\\layouts\\settings"
                )
            }
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.jbcrypt)
    implementation(libs.mpandroidchart)
    implementation(libs.volley)
}