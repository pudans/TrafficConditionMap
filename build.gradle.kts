plugins {
    id("org.jlleitschuh.gradle.ktlint") version "8.2.0"
}

ktlint {
    version.set("0.34.2")
    debug.set(true)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
//    reporters.set(setOf(ReporterType.PLAIN, ReporterType.CHECKSTYLE))
    ignoreFailures.set(true)
    kotlinScriptAdditionalPaths {
        include(fileTree("scripts/"))
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

buildscript {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(dependencyNotation = "com.android.tools.build:gradle:7.1.0-alpha03")
        classpath(kotlin(module = "gradle-plugin", version = "1.5.31"))
        classpath(dependencyNotation = "com.google.dagger:hilt-android-gradle-plugin:2.40")
        classpath(dependencyNotation = "com.google.gms:google-services:4.3.10")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
