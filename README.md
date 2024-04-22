# Murglar plugin for Jellyfin  #

Allow integrate own Jellyfin server in Murglar app

Supported features:
- [x] Artists
- [x] Albums
- [x] Tracks
- [ ] TODO

# Build #

1. [Setup keystore](https://github.com/badmannersteam/murglar-plugins?tab=readme-ov-file#android-module-additional-requirements)
2. Run `./gradlew clean build`.
3Grab your plugins:
    - JAR - `murglar-jellyfin-core/build/libs/murglar-plugin-jellyfin-<version>.jar`
    - APK - `murglar-jellyfin-android/build/outputs/apk/release/murglar-plugin-jellyfin-<version>.apk