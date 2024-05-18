import tornaco.project.android.shortx.Configs
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.agp.lib)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    id("maven-publish")
}

android {
    namespace = "io.github.rosemoe.sora.app"

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
        viewBinding = true
        dataBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(platform("io.github.Rosemoe.sora-editor:bom:0.23.4"))
    implementation("io.github.Rosemoe.sora-editor:editor")
    implementation("io.github.Rosemoe.sora-editor:language-java")
    implementation("io.github.Rosemoe.sora-editor:language-textmate")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.core.ktx)
    // Note: Do not upgrade this lib, ComposeOverlay.
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
}


val GROUP_ID = "shortx"
val ARTIFACT_ID = project.path.removePrefix(":").replace(":", "-")
val VERSION = Configs.latestGitTag().ifEmpty { "1.0.0" }

println("ARTIFACT_ID: $ARTIFACT_ID")

val githubProperties = Properties()
githubProperties.load(FileInputStream(rootProject.file("github.properties")))

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = GROUP_ID
            artifactId = ARTIFACT_ID
            version = VERSION

            afterEvaluate {
                from(components["release"])
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


