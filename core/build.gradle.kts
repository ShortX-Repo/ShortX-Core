import tornaco.project.android.shortx.Configs.sxPackageName
import tornaco.project.android.shortx.addAidlTask

plugins {
    alias(libs.plugins.gmazzo.buildconfig)

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.protobuf.gradle.plugin)
    alias(libs.plugins.kover)
}

buildConfig {
    packageName("${sxPackageName}.core")
    className("BuildProp")
    useKotlinOutput { internalVisibility = false }

    buildConfigField("String", "LOG_PREFIX", "\"ShortX-\"")
    buildConfigField("String", "TG_GROUP", "\"https://t.me/shortxmod\"")
    buildConfigField("String", "QQ_GROUP_1", "\"882416995\"")
    buildConfigField("String", "WIKI_URL", "\"https://shortx-repo.github.io/ShortX-Pages/zh/\"")
    buildConfigField("String", "WIKI_URL_BILI", "\"https://www.bilibili.com/video/BV1hc411X7yF/\"")
    buildConfigField(
        "String",
        "WIKI_URL_MVEL",
        "\"https://shortx-repo.github.io/ShortX-Pages/zh/2023/01/05/shortx-usefull-mvel.html\""
    )
    buildConfigField(
        "String",
        "METHOD_HOOK_WIKI_URL",
        "\"https://shortx-repo.github.io/ShortX-Pages/zh/guide/fact_hook.html#method-hook\""
    )

    buildConfigField(
        "String",
        "APP_SHELL_SERVICE_NAME",
        "\"tornaco.apps.shortx.ui.service.AppShellService\""
    )
    buildConfigField(
        "String",
        "APP_NOTIFICATION_SERVICE_NAME",
        "\"tornaco.apps.shortx.ui.service.AppNotificationService\""
    )
    buildConfigField(
        "String",
        "APP_HTTP_REQUEST_SERVICE_NAME",
        "\"tornaco.apps.shortx.ui.service.AppHttpRequestService\""
    )
    buildConfigField(
        "String",
        "APP_MAIN_ACTIVITY",
        "\"tornaco.apps.shortx.ui.main.MainActivity\""
    )
    buildConfigField("String", "DEFAULT_OVERLAY_BUTTON_ICON", "\"\"")
    buildConfigField(
        "String",
        "OPEN_SOURCE_LICENSE_URL",
        "\"https://raw.githubusercontent.com/ShortX-Repo/ShortX-Release/main/open_source_license.md\""
    )
    buildConfigField("String", "SHORTX_GITHIB_REPO_URL", "\"https://github.com/ShortX-Repo\"")
    buildConfigField(
        "String",
        "SHORTX_DISCOVERY_DEFAULT_ONLINE_REPO_URL",
        "\"https://raw.githubusercontent.com/ShortX-Repo/Files/main/index.json\""
    )
}

dependencies {
    compileOnly(files("${rootProject.rootDir.path}/android-sdk/34/android-34.jar"))
    api(libs.protobuf.java)
    api(libs.protobuf.java.util)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlin.stdlib.jdk8)

    implementation(libs.kotlin.reflect)

    api(libs.retrofit)
    api(libs.converter.gson)
    api(libs.okhttp)
    api(libs.logging.interceptor)

    implementation(project(":external:utils"))

    testImplementation(libs.junit)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
}

addAidlTask()

protobuf {
    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:4.27.2"
    }
}
