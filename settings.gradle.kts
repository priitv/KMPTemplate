pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Multimodule template"

include(":app")
include(":core:database")
include(":core:testing")
include(":core:ui")
include(":feature:mymodel:app", ":feature:mymodel:business", ":feature:mymodel:di",":feature:mymodel:data")
include(":test-app")
