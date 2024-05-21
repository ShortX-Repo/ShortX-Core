package tornaco.project.android.shortx

import org.gradle.api.Project
import java.io.FileInputStream
import java.util.Properties

class GPR(project: Project) {
    private val githubProperties = Properties()

    init {
        val propFile = project.rootProject.file("github.properties")
        if (propFile.exists()) {
            println("load prop file: $propFile")
            githubProperties.load(FileInputStream(propFile))
        }
    }

    val userName
        get() = ((githubProperties["gpr.usr"] ?: System.getenv("GPR_USER")) ?: "").toString()
    val password
        get() = ((githubProperties["gpr.key"] ?: System.getenv("GPR_API_KEY")) ?: "").toString()
}