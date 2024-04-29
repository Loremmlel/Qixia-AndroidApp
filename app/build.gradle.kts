plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // 踩了个坑，版本1.8.21-1.0.11是不可用的。
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
}

android {
    namespace = "org.hinanawiyuzu.qixia"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.hinanawiyuzu.qixia"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        // 原来是1.4.7,和kotlin的1.9版本不兼容。那我以前是怎么构建成功的？明明以前用的kotlin版本也是1.9.
        // 鉴定为room异变。
        // 又改成1.5.6来适配1.9.21版本的kotlin。
        kotlinCompilerExtensionVersion = "1.5.6"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("com.google.accompanist:accompanist-permissions:0.30.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.github.amggg:YXing:V2.0.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("org.mockito:mockito-core:5.7.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}