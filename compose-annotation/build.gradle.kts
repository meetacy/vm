plugins {
    id("kmm-library-convention")
}

version = libs.versions.mvm.get()

android {
    namespace = "app.meetacy.vm.compose"
}

dependencies {
    androidMainImplementation(libs.composeFoundation)
    androidMainImplementation(platform(libs.composeBOM))
}
