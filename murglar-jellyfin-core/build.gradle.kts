plugins {
    alias(catalog.plugins.murglar.plugin.core)
}

murglarPlugin {
    id = "jellyfin"
    name = "Jellyfin"
    version = catalog.versions.murglar.jellyfin
    murglarClass = "com.graf2242.murglar_jellyfin_core.JellyfinMurglar"
}

dependencies {
    implementation(catalog.jellyfin) {
        exclude("io.ktor")
    }
}
