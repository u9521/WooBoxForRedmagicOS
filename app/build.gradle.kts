import com.android.aaptcompiler.Macro
import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.u9521.wooboxforredmagicos"
    signingConfigs {
        create("release") {}
    }
    compileSdk = 34
//    buildToolsVersion = "32.0.0"
    buildFeatures {
        buildConfig = true
        compose = true
    }
    defaultConfig {
        applicationId = "com.u9521.wooboxforredmagicos"
        minSdk = 31
        targetSdk = 33
        versionCode = 5
        versionName = "1.0.4"
    }

    buildTypes {
        all {
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(
                listOf("proguard-rules.pro")
            )
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.majorVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/**"
            excludes += "/kotlin/**"
            excludes += "/*.txt"
            excludes += "/*.bin"
        }
        dex {
            useLegacyPackaging = true
        }
    }
    applicationVariants.all {
        outputs.all {
            (this as BaseVariantOutputImpl).outputFileName =
                "WooBoxForColorOS-$versionName-$name.apk"
        }
    }
}

dependencies {
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.runtime:runtime-android:1.7.8")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    //API
    compileOnly("de.robv.android.xposed:api:82")
    implementation("com.github.kyuubiran:EzXHelper:2.2.1")
    //UI
    implementation(project(":blockmiui"))
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    //APP Center
//    val appCenterSdkVersion = "5.0.6"
//    implementation("com.microsoft.appcenter:appcenter-analytics:${appCenterSdkVersion}")
//    implementation("com.microsoft.appcenter:appcenter-crashes:${appCenterSdkVersion}")
}