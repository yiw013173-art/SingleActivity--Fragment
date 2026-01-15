plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.myapplicationview"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.myapplicationview"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    val nav_version = "2.8.0" // 请检查最新版本
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation("com.google.android.material:material:1.11.0")
    implementation(project(":core:network"))
    // ViewModel 核心（含 Kotlin 扩展）
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
// Lifecycle 核心（监听生命周期、协程等）
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
// Fragment KTX（提供 viewModels() 委托函数）
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    // 推荐使用 Coil，它是 Kotlin 原生的图片库，轻量且支持协程
    implementation("io.coil-kt:coil:2.4.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.datastore:datastore-preferences:1.1.0")
    implementation("io.github.scwang90:refresh-layout-kernel:2.1.0")
// 核心
    implementation("io.github.scwang90:refresh-header-classics:2.1.0")
// 经典刷新头
    implementation("io.github.scwang90:refresh-footer-classics:2.1.0")
// 经典加载底
    // 必须添加核心库，SmartRefreshLayout 依赖它处理嵌套滚动
    implementation("androidx.core:core-ktx:1.9.0")
// 建议同时确保以下库也存在（通常项目自带）
    implementation("androidx.appcompat:appcompat:1.6.1")
}