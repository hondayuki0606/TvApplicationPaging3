plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.example.tvapplicationpaging3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tvapplicationpaging3"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
}

dependencies {
    implementation("androidx.cardview:cardview:1.0.0")

    val room_version = "2.5.0"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin Symbol Processing (KSP)
//    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.leanback:leanback-paging:1.1.0-alpha11")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // optional - RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:$room_version")

    // optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:$room_version")

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:$room_version")

    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$room_version")

    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:$room_version")

    val paging_version = "3.3.2"

//    implementation("androidx.paging:paging-runtime-ktx:$paging_version")
//
//    // alternatively - without Android dependencies for tests
//    testImplementation("androidx.paging:paging-common-ktx:$paging_version")
//
//    // optional - RxJava2 support
//    implementation("androidx.paging:paging-rxjava2-ktx:$paging_version")
//
//    // optional - RxJava3 support
//    implementation("androidx.paging:paging-rxjava3:$paging_version")
//
//    // optional - Guava ListenableFuture support
//    implementation("androidx.paging:paging-guava:$paging_version")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.leanback)
    implementation(libs.glide)
}