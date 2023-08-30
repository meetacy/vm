import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    compileSdkVersion(33)

    defaultConfig {
        minSdk = 16
        //noinspection OldTargetApi
        targetSdk = 33
    }
}
