/*
 * Copyright (c) 2023 Meetacy. All rights reserved.
 */

plugins {
    id("kmm-library-convention")
}

android {
    namespace = "app.meetacy.vm.mvi"
}

dependencies {
    commonMainApi(projects.vm.core)
    commonMainApi(projects.vm.composeAnnotation)
    androidMainApi(projects.vm.core)
}
