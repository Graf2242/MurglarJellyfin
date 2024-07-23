plugins {
    id("murglar-gradle-plugin-android") version "4.2"
}


murglarAndroidPlugin {
    id = "jellyfin"
    name = "Jellyfin"
    version = 2
    murglarClass = "com.graf2242.murglar_jellyfin_core.JellyfinMurglar"
}

dependencies {
    implementation(project(":murglar-jellyfin-core"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.stdlib.jdk8)
}

