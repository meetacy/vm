plugins {
    id("kmm-library-convention")
}

android {
    namespace = "app.meetacy.vm.compose"
}

dependencies {
    androidMainImplementation(libs.composeFoundation)
    androidMainImplementation(platform(libs.composeBOM))
}
