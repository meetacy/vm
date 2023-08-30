plugins {
    id("kmm-library-convention")
}

android {
    namespace = "app.meetacy.vm"
}

dependencies {
    commonMainApi(libs.kotlinxCoroutines)

    androidMainApi(libs.lifecycleKtx)
    androidMainApi(libs.androidViewModel)
}
