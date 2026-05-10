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
        // زدنا هاد السطر باش نضمنوا توافق كاع المكتبات الخارجية
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Yomu AI"
include(":app")
