plugins {
    id("murglar-gradle-plugin-core") version "4.2"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
//    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
//    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20")
//    implementation(libs.jellyfin.core)
    implementation("org.jellyfin.sdk:jellyfin-core-jvm:1.4.7")

}

murglarPlugin {
    id = "jellyfin"                                                     // your plugin id
    name = "Jellyfin"                                                   // your plugin human-readable name (in English)
    version = "0.0.1"                                                     // plugin version (minor)
    murglarClass = "com.graf2242.murglar_jellyfin_core.JellyfinMurglar"  // entry point - Murglar implementation class
}
