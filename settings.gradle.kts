dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://api.xposed.info")
    }
}
rootProject.name = "WooBoxForRedmagicOS"
include(":app")
include(":blockmiui")