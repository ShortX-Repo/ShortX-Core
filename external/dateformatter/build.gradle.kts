import tornaco.project.android.shortx.Configs
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.agp.lib)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "github.tornaco.shortx.android.third.party.date.formatter"

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