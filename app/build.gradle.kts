
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
}

fun getSigningProperties(): Properties {
    return Properties().apply {
        val propertiesFile = rootProject.file("sign.properties")
        if (propertiesFile.exists()) {
            load(FileInputStream(propertiesFile))
        }
    }
}


android {
    namespace = "com.u9521.wooboxforredmagicos"
    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("keystore/testkey.p12")
            storePassword = "android"
            keyAlias = "androidtestkey"
            keyPassword = "android"
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
        }
        create("release") {
            val props = getSigningProperties()
            val keystorePath = props.getProperty("signing.storeFile")
            val keystoreFile = keystorePath?.let { rootProject.file(it) }
            if (keystoreFile?.exists() == true) {
                storeFile = keystoreFile
                storePassword = props.getProperty("signing.storePassword", "")
                keyAlias = props.getProperty("signing.keyAlias", "")
                keyPassword = props.getProperty("signing.keyPassword", "")
                enableV1Signing = false
                enableV2Signing = false
                enableV3Signing = true
            } else {
                initWith(getByName("debug"))
                println("\u001B[33m[Warning]: Release signing config not found, falling back to debug signing\u001B[0m")
            }
        }
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
        versionCode = 7
        versionName = "1.0.6"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(
                listOf("proguard-rules.pro")
            )
            signingConfig = signingConfigs.getByName("release")
            ndk {
                abiFilters.clear()
                abiFilters.add("arm64-v8a")
            }

        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isJniDebuggable = true
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            ndk {
                abiFilters.clear()
                abiFilters.addAll(listOf("arm64-v8a", "x86_64"))
            }
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
        }
        dex {
            useLegacyPackaging = true
        }
    }
    applicationVariants.all {
        outputs.forEach { output ->
            var outPrefix = "WooBox4RMOS-${buildType.name}-${versionName}(${versionCode})"
            val abi = buildTypes.getByName(buildType.name).ndk.abiFilters
            if (abi.isNotEmpty()) {
                abi.forEach {
                    outPrefix += "-$it"
                }
            }
            outPrefix += ".apk"
            (output as BaseVariantOutputImpl).outputFileName = outPrefix
        }
        this.assembleProvider?.configure {
            doLast {
                val outputDir = File(project.rootDir, "outputs/${buildType.name}")
                outputDir.deleteRecursively()
                outputDir.mkdirs()
                val apkDir = layout.buildDirectory.dir("outputs/apk/${buildType.name}").get().asFile
                if (apkDir.exists()) {
                    apkDir.listFiles()?.forEach { file ->
                        val destFile = File(outputDir, file.name)
                        if (destFile.name == "baselineProfiles") {
                            return@forEach
                        }
                        file.copyTo(destFile, overwrite = true)
                        println("Copied apk file to: ${destFile.absolutePath}")
                    }
                }
                val mappingDir =
                    layout.buildDirectory.dir("outputs/mapping/${buildType.name}").get().asFile
                if (mappingDir.exists()) {
                    mappingDir.listFiles()?.forEach { file ->
                        var destFile = File(outputDir, file.name)
                        if (destFile.name == "mapping.txt") {
                            destFile = File(outputDir, "mapping-${versionName}(${versionCode}).txt")
                        }
                        file.copyTo(destFile, overwrite = true)
                        println("Copied mapping file to: ${destFile.absolutePath}")
                    }
                }

            }
        }
    }
}

dependencies {
    implementation("androidx.annotation:annotation-jvm:1.9.1")
    //API
    compileOnly("de.robv.android.xposed:api:82")
    implementation("org.luckypray:dexkit:2.0.3")
//    implementation("com.google.code.gson:gson:2.11.0")

    //UI
    implementation(project(":blockmiui"))
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
}