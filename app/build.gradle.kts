plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.mapsapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mapsapp"
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
secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.properties"
    ignoreList.add("keyToIgnore")
    ignoreList.add("sdk.*")
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
    implementation(libs.androidx.navigation.compose)//NAVIGATION
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("com.google.maps.android:maps-compose:2.11.4") //APIMAPS
    implementation("com.google.android.gms:play-services-maps:18.2.0") //APIMAPS
    implementation("com.google.android.gms:play-services-location:21.1.0") //APIMAPS
    implementation("androidx.navigation:navigation-common-ktx:2.7.6") //NAVIGATION
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2") //VIEWMDODEL
    implementation("androidx.compose.material:material:1.6.1") //BOTTOMNAVIGATION
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4") //LIVEDATA
    implementation("androidx.camera:camera-core:1.3.2") //CAMERA
    implementation("androidx.camera:camera-camera2:1.3.2") //CAMERA
    implementation("androidx.camera:camera-lifecycle:1.3.2") //CAMERA
    implementation("androidx.camera:camera-view:1.3.2") //CAMERA
    implementation("androidx.camera:camera-extensions:1.3.2") //CAMERA
    implementation("androidx.compose.material:material-icons-extended:1.6.3") //ICONS
}