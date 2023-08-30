plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    api(libs.androidGradlePlugin)
    api(libs.kotlinGradlePlugin)
}
