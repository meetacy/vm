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

    sourceSets {
        val commonMain by getting
        val commonTest by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }

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