plugins {
    id("kmm-library-convention")
}

version = libs.versions.mvm.get()

android {
    namespace = "app.meetacy.vm"
}

dependencies {
    commonMainApi(libs.kotlinxCoroutines)

    androidMainApi(libs.lifecycleKtx)
    androidMainApi(libs.androidViewModel)
}
