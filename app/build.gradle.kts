import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    namespace = "com.u9521.wooboxforredmagicos"
    signingConfigs {
        create("release") {}
    }
    compileSdk = 35
//    buildToolsVersion = "32.0.0"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        applicationId = "com.u9521.wooboxforredmagicos"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        encoding = "UTF-8"
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.majorVersion
    }
    packaging {
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
    implementation("androidx.annotation:annotation-jvm:1.9.1")
    //API
    compileOnly("de.robv.android.xposed:api:82")
    implementation("com.github.kyuubiran:EzXHelper:2.2.1")
    implementation("org.luckypray:dexkit:2.0.3")
    //UI
    implementation(project(":blockmiui"))
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    //APP Center
//    val appCenterSdkVersion = "5.0.6"
//    implementation("com.microsoft.appcenter:appcenter-analytics:${appCenterSdkVersion}")
//    implementation("com.microsoft.appcenter:appcenter-crashes:${appCenterSdkVersion}")
}