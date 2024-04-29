pluginManagement {
    repositories {
        maven("https://jitpack.io")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // 原来依赖的远程仓库要加这里，上面的是plugin，也就是插件……
        maven("https://jitpack.io")
        google()
        mavenCentral()
    }
}

rootProject.name = "qixia"
include(":app")
 