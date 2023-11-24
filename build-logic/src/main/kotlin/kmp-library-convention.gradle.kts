@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("publication-convention")
    id("android-base-convention")
}

kotlin {
    jvmToolchain(11)
    explicitApi()

    android {
        publishLibraryVariants("release")
    }
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        val commonMain by getting
        val commonTest by getting

        val nativeTargets = listOf(
            "iosArm32",
            "iosArm64",
            "iosX64",
            "iosSimulatorArm64",
        )

        val targetWithoutAndroid = nativeTargets + listOf(
            "jvm",
        )

        val nonAndroidMain by creating
        nonAndroidMain.dependsOn(commonMain)

        targetWithoutAndroid.mapNotNull { findByName("${it}Main") }
            .forEach { it.dependsOn(nonAndroidMain) }

        val nonAndroidTest by creating
        nonAndroidTest.dependsOn(commonTest)

        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}