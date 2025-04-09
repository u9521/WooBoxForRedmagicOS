// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.9.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.10")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }

}
plugins {
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
}
tasks.register("Delete", Delete::class) {
    delete(rootProject.buildDir)
}