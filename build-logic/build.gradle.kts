plugins {
    `kotlin-dsl`
}

version = libs.versions.mvm.get()

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    api(libs.androidGradlePlugin)
    api(libs.kotlinGradlePlugin)
}
