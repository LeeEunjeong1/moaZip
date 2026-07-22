pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "moaZip-android"

include(":app")
include(":core:model")
include(":core:domain")
include(":core:data")
include(":core:presentation")
include(":core:ui")
include(":feature:dashboard")
include(":feature:auth")
include(":feature:assets")
include(":feature:recurring")
include(":feature:import")
