plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp") version "2.0.20-1.0.24"
}

android {
    namespace = "com.yjotdev.playermusic"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.yjotdev.playermusic"
        minSdk = 24
        targetSdk = 35
        versionCode = 7
        versionName = "7.0"

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
        kotlinCompilerExtensionVersion = "1.5.9"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //Compose
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation(platform("androidx.compose:compose-bom:2025.01.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    //Navegacion
    implementation("androidx.navigation:navigation-runtime-ktx:${rootProject.extra["navigationVersion"]}")
    implementation("androidx.navigation:navigation-compose:$${rootProject.extra["navigationVersion"]}")
    //Room
    implementation("androidx.room:room-runtime:${rootProject.extra["roomVersion"]}")
    implementation("androidx.room:room-ktx:${rootProject.extra["roomVersion"]}")
    implementation("androidx.navigation:navigation-testing:2.8.6")
    annotationProcessor("androidx.room:room-compiler:${rootProject.extra["roomVersion"]}")
    ksp("androidx.room:room-compiler:${rootProject.extra["roomVersion"]}")
    //Coil
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.coil-kt:coil:${rootProject.extra["coilVersion"]}")
    implementation("io.coil-kt:coil-compose:${rootProject.extra["coilVersion"]}")
    //Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.01.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}