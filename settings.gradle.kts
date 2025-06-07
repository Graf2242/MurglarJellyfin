pluginManagement {
    repositories {
        gradlePluginPortal()                                        // for kotlin gradle plugins
        google()                                                    // for android gradle plugin
        maven { url = java.net.URI.create("https://jitpack.io") }   // for murglar gradle plugins
        mavenCentral()
        mavenLocal()
    }
    resolutionStrategy {
        eachPlugin {
            // workaround for requesting gradle plugins from plain maven repository (jitpack)
            // with `plugins {...}` block syntax
            val prefix = "murglar-gradle-plugin-"
            if (requested.id.id.startsWith(prefix)) {
                val artifactId = "${requested.id.id.substringAfter(prefix)}-plugin-gradle-plugin"
                useModule("com.github.badmannersteam.murglar-plugins:$artifactId:${requested.version}")
            }
        }
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("catalog") {
            version("murglar-plugins", "4.4")
            version("murglar-jellyfin", "2")    // use just a single number

            // for core module
            plugin("murglar-plugin-core", "murglar-gradle-plugin-core").versionRef("murglar-plugins")
            // for android module
            plugin("murglar-plugin-android", "murglar-gradle-plugin-android").versionRef("murglar-plugins")

            library("jellyfin", "org.jellyfin.sdk", "jellyfin-core-jvm").version("1.4.7")
        }
    }
}


include(":murglar-jellyfin-core")
include(":murglar-jellyfin-android")
