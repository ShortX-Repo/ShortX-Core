import com.android.build.gradle.api.AndroidBasePlugin
import tornaco.project.android.shortx.Configs
import tornaco.project.android.shortx.Configs.sxIsDebugBuild
import tornaco.project.android.shortx.Configs.sxVersionCode
import tornaco.project.android.shortx.Configs.sxVersionName
import tornaco.project.android.shortx.GPR
import tornaco.project.android.shortx.log

buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}

plugins {
    alias(libs.plugins.agp.lib) apply false
    alias(libs.plugins.agp.app) apply false

    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.dagger.hilt.android) apply false

    alias(libs.plugins.protobuf.gradle.plugin) apply false
    alias(libs.plugins.gmazzo.buildconfig) apply false

    alias(libs.plugins.gladed.androidgitversion) apply true
    alias(libs.plugins.diffplug.spotless) apply true
    alias(libs.plugins.kover) apply false

    id("shortx")
    id("maven-publish")
}

androidGitVersion {
    prefix = "v"
    codeFormat = "MNPBBB"
    baseCode = 2000
}

sxVersionCode = androidGitVersion.code()
sxVersionName = androidGitVersion.name()

val androidSourceCompatibility by extra(JavaVersion.VERSION_17)
val androidTargetCompatibility by extra(JavaVersion.VERSION_17)

subprojects {
    log("subprojects: ${this.name}")

    tasks.withType<JavaCompile> {
        options.compilerArgs.addAll(
            arrayOf(
                "-Xmaxerrs",
                "1000"
            )
        )
        options.encoding = "UTF-8"
    }

    plugins.withType(com.android.build.gradle.api.AndroidBasePlugin::class.java) {
        extensions.configure(com.android.build.api.dsl.CommonExtension::class.java) {
            compileSdk = Configs.compileSdkVersion
            ndkVersion = Configs.ndkVersion

            externalNativeBuild {
                cmake {
                    version = "3.22.1+"
                }
            }

            defaultConfig {
                minSdk = Configs.minSdkVersion
                if (this is com.android.build.api.dsl.ApplicationDefaultConfig) {
                    targetSdk = Configs.targetSdkVersion
                    versionCode =
                        if (sxIsDebugBuild) 999999 else Configs.sxVersionCode
                    versionName = Configs.sxVersionName
                    testInstrumentationRunner =
                        Configs.testInstrumentationRunner
                }
            }

            lint {
                abortOnError = true
                checkReleaseBuilds = false
            }

            compileOptions {
                sourceCompatibility = androidSourceCompatibility
                targetCompatibility = androidTargetCompatibility
            }

        }
    }
    plugins.withType(JavaPlugin::class.java) {
        extensions.configure(JavaPluginExtension::class.java) {
            sourceCompatibility = androidSourceCompatibility
            targetCompatibility = androidTargetCompatibility
        }
    }
    project.afterEvaluate {
        val hasAndroidPlugin = project.plugins.hasPlugin(AndroidBasePlugin::class)
        log("Check publishing subproject: ${project.name}, hasAndroidPlugin: $hasAndroidPlugin")
        val publishingModules = listOf(
            "core",
            "ui-base",
            "external:utils",
            "external:dateformatter",
            "external:reorderable",
            "external:picasso",
            "external:compose-color-picker",
            "external:icons",
            "external:dex-maker",
            "external:rhino-android",
            "external:drawbox",
            "external:codeditor"
        )
        if (publishingModules.contains(project.name)) {
            val GROUP_ID = "shortx"
            val ARTIFACT_ID = project.path.removePrefix(":").replace(":", "-")
            val VERSION = Configs.latestGitTag().ifEmpty { "1.0.0" }

            project.plugins.apply("maven-publish")
            publishing {
                publications {
                    register<MavenPublication>("release") {
                        groupId = GROUP_ID
                        artifactId = ARTIFACT_ID
                        version = VERSION

                        afterEvaluate {
                            from(components[if (hasAndroidPlugin) "release" else "java"])
                        }
                    }
                }

                repositories {
                    maven {
                        name = "GitHubPackages"
                        url = uri("https://maven.pkg.github.com/ShortX-Repo/ShortX-Core")

                        credentials {
                            username = GPR(project).userName
                            password = GPR(project).password
                        }
                    }
                }
            }
        }

    }

}