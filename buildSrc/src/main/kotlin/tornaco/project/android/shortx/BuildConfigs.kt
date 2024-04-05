package tornaco.project.android.shortx

import org.gradle.api.Plugin
import org.gradle.api.Project
import tornaco.project.android.shortx.Configs.sxIsDebugBuild
import java.util.Properties

private val props = Properties()

object Configs {
    const val sxPackageName = "tornaco.apps.shortx"
    var sxVersionCode: Int? = 0
    var sxVersionName: String? = null
    val sxVersionNameSuffix: String get() = System.getProperty("versionNameSuffix", "")
    var sxIsDebugBuild: Boolean = false

    const val compileSdkVersion = 34
    const val minSdkVersion = 26
    const val targetSdkVersion = 34

    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    // We use this value to find some build tools binaries.
    // consider to find the latest version more gracefully
    // currently we set to 30.0.3 to test circle ci.
    const val buildToolsVersion = "30.0.3"
    const val ndkVersion = "21.1.6352462"
    val cmakeVersion get() = "3.22.1"

    val Project.resPrefix: String get() = "${this.name}_"

    operator fun get(key: String): String? {
        val v = props[key] as? String ?: return null
        return v.ifBlank { null }
    }

    fun Project.keyStoreAlias(): String {
        return Configs["keyAlias"] ?: this.findProperty("keyAlias")?.toString() ?: ""
    }

    fun Project.keyStorePassword(): String {
        return Configs["keyPassword"] ?: this.findProperty("keyPassword")?.toString() ?: ""
    }

    fun latestGitTag(): String {
        val process = ProcessBuilder("git", "describe", "--tags", "--abbrev=0").start()
        return process.inputStream.bufferedReader().use { bufferedReader ->
            bufferedReader.readText().trim()
        }
    }
}


@ExperimentalStdlibApi
class ShortXAndroidBuildPlugin : Plugin<Project> {
    override fun apply(project: Project) = project.applyPlugin()

    private fun Project.applyPlugin() {
        props.clear()
        rootProject.file("gradle.properties").let { propFile ->
            if (propFile.exists()) {
                propFile.inputStream().use { props.load(it) }
            }
        }
        rootProject.file("local.properties").let { propFile ->
            if (propFile.exists()) {
                propFile.inputStream().use { props.load(it) }
            }
        }

        updateBuildConfigs()
    }

    private fun Project.updateBuildConfigs() {
        sxIsDebugBuild = getBuildVariant() == Variant.DEBUG
        log("sxIsDebugBuild: $sxIsDebugBuild")
    }

    private fun Project.getBuildVariant(): Variant {
        val tskReqStr = gradle.startParameter.taskRequests.map {
            it.args
        }.toString()
        log("getBuildVariant, tskReqStr: $tskReqStr")
        val isDebug = tskReqStr.contains(Variant.DEBUG.name, ignoreCase = true)
        return if (isDebug) {
            Variant.DEBUG
        } else {
            Variant.RELEASE
        }
    }

    enum class Variant {
        DEBUG,
        RELEASE
    }
}
