import tornaco.project.android.shortx.Configs.sxIsDebugBuild
import tornaco.project.android.shortx.Configs.sxPackageName
import tornaco.project.android.shortx.Configs.sxVersionCode
import tornaco.project.android.shortx.Configs.sxVersionName
import tornaco.project.android.shortx.Configs.sxVersionNameSuffix
import tornaco.project.android.shortx.addAidlTask
import java.io.FileInputStream
import java.util.Properties
import java.util.UUID

plugins {
    alias(libs.plugins.gmazzo.buildconfig)

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.protobuf.gradle.plugin)
    alias(libs.plugins.kover)

    id("maven-publish")
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
    buildConfigField("String", "API_BASE_URL", "\"http://shortx.emui.tech/api/\"")
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
        artifact = "com.google.protobuf:protoc:3.25.2"
    }
}


val GROUP_ID = "shortx"
val ARTIFACT_ID = "core"
val VERSION = latestGitTag().ifEmpty { "1.0.0" }

fun latestGitTag(): String {
    val process = ProcessBuilder("git", "describe", "--tags", "--abbrev=0").start()
    return process.inputStream.bufferedReader().use { bufferedReader ->
        bufferedReader.readText().trim()
    }
}

val githubProperties = Properties()
githubProperties.load(FileInputStream(rootProject.file("github.properties")))

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = GROUP_ID
            artifactId = ARTIFACT_ID
            version = VERSION

            afterEvaluate {
                from(components["java"])
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ShortX-Repo/ShortX-Core")

            credentials {
                username = (githubProperties["gpr.usr"] ?: System.getenv("GPR_USER")).toString()
                password = (githubProperties["gpr.key"] ?: System.getenv("GPR_API_KEY")).toString()
            }
        }
    }
}
