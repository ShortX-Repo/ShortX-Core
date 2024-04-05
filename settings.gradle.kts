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
include(":external:dateformatter")
include(":external:reorderable")
include(":external:picasso")
include(":external:compose-color-picker")
include(":external:icons")
include(":external:dex-maker")
include(":external:rhino-android")
include(":external:drawbox")
include(":external:codeditor")
