pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }

    }
}

rootProject.name = "BankDetailPro"
include(":app")
include(":api:bankdetail")
include(":data:bankdetail")
include(":domain:bankdetail")
include(":common:shared")
