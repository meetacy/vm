plugins {
    id("kmm-library-convention")
}

version = libs.versions.mvm.get()

android {
    namespace = "app.meetacy.vm.mvi"
}

dependencies {
    commonMainApi(projects.vm.core)
    commonMainApi(projects.vm.composeAnnotation)
    androidMainApi(projects.vm.core)
}
