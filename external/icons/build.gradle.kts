plugins {
    alias(libs.plugins.agp.lib)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "github.tornaco.shortx.icons"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = false
        buildConfig = false
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
        viewBinding = false
        dataBinding = false
    }
}
dependencies {
    testImplementation(libs.junit)
    testImplementation("com.android.tools:sdk-common:31.4.1")
    testImplementation("com.android.tools:common:31.4.1")
}