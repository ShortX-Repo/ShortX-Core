@file:Suppress("UnstableApiUsage")


rootProject.name = "ShortXCore"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
@Suppress("JcenterRepositoryObsolete") dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        jcenter()
        maven(url = "https://jitpack.io")
    }
}

include(":app")
include(":core")
include(":external:utils")
