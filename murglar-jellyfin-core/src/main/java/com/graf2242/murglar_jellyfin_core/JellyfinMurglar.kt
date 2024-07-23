package com.graf2242.murglar_jellyfin_core

import com.badmanners.murglar.lib.core.localization.RussianMessages.Companion.RUSSIAN
import com.badmanners.murglar.lib.core.log.LoggerMiddleware
import com.badmanners.murglar.lib.core.model.track.source.Bitrate
import com.badmanners.murglar.lib.core.model.track.source.Extension
import com.badmanners.murglar.lib.core.model.track.source.Source
import com.badmanners.murglar.lib.core.network.NetworkMiddleware
import com.badmanners.murglar.lib.core.notification.NotificationMiddleware
import com.badmanners.murglar.lib.core.preference.EditPreference
import com.badmanners.murglar.lib.core.preference.Preference
import com.badmanners.murglar.lib.core.preference.PreferenceMiddleware
import com.badmanners.murglar.lib.core.service.BaseMurglar
import com.badmanners.murglar.lib.core.utils.contract.WorkerThread
import com.graf2242.murglar_jellyfin_core.jellyfin_api.JellyfinApi
import com.graf2242.murglar_jellyfin_core.localization.JellyfinDefaultMessages
import com.graf2242.murglar_jellyfin_core.localization.JellyfinMessages
import com.graf2242.murglar_jellyfin_core.localization.JellyfinRuMessages
import com.graf2242.murglar_jellyfin_core.login.JellyfinLoginResolver
import com.graf2242.murglar_jellyfin_core.model.JellyfinAlbum
import com.graf2242.murglar_jellyfin_core.model.JellyfinArtist
import com.graf2242.murglar_jellyfin_core.model.JellyfinTrack
import com.graf2242.murglar_jellyfin_core.model.albumFromItemResult
import com.graf2242.murglar_jellyfin_core.model.artistFromItemResult
import com.graf2242.murglar_jellyfin_core.model.trackFromItemResult
import com.graf2242.murglar_jellyfin_core.node.JellyfinNodeResolver
import kotlinx.coroutines.runBlocking
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.PlayMethod
import org.jellyfin.sdk.model.api.PlaybackStartInfo
import org.jellyfin.sdk.model.api.PlaybackStopInfo
import org.jellyfin.sdk.model.api.RepeatMode

import java.util.Locale.ENGLISH
import java.util.UUID

class JellyfinMurglar(
    id: String,
    preferences: PreferenceMiddleware,
    network: NetworkMiddleware,
    notifications: NotificationMiddleware,
    logger: LoggerMiddleware
) : BaseMurglar<JellyfinTrack, JellyfinMessages>(
    id, ICON_URL, MESSAGES, preferences, network, notifications, logger
) {
    companion object {
        /**
         * Must be used only for [MediaId.build], don't pass it to the [BaseMurglar] constructor directly!
         */
        const val SERVICE_ID = "Jellyfin"

        private const val ICON_URL =
            "https://play-lh.googleusercontent.com/aFWiT2lTa9CYBpyPjfgfNHd0r5puwKRGj2rHpdPTNrz2N9LXgN_MbLjePd1OTc0E8Rl1"

        private val MESSAGES = mapOf(
            ENGLISH to JellyfinDefaultMessages,
            RUSSIAN to JellyfinRuMessages
        )

        const val SAMPLE_DOMAIN = "https://sample.com"
        private const val SERVER_URL_PREFERENCE = "jellyfin-domain"
    }

    val log = logger

    // Maybe sometimes we'll be able to use default jellyfin sdk api. For now it's broken because of coroutines incompatibility
//    val jellyfinApi = createJellyfin {
//        clientInfo = ClientInfo(name = "MurglarJellyfin", version = "0.0.1")
//        deviceInfo = DeviceInfo(id="device_id", name="MurglarClient")
//    }.createApi(baseUrl = serverUrl)
    val jellyfinApi = JellyfinApi(this, network, logger)

    @WorkerThread
    override fun onCreate() {
        if (!loginResolver.isLogged)
            return

        jellyfinApi.userId = loginResolver.userId
        jellyfinApi.token = loginResolver.accessToken
        jellyfinApi.authType = loginResolver.authType
    }

    override val murglarPreferences: List<Preference>
        get() = listOf(
            EditPreference(
                "serverUrl", messages.serverUrlTitle, messages.serverUrlSummary, ::serverUrl, { serverUrl = it }, true
            )
        )

    var serverUrl: String
        get() = preferences.getString(SERVER_URL_PREFERENCE, SAMPLE_DOMAIN)
        set(value) = when {
            value.isEmpty() -> preferences.remove(SERVER_URL_PREFERENCE)
            else -> preferences.setString(SERVER_URL_PREFERENCE, value.dropLastWhile { it == '/' })
        }

    override val loginResolver = JellyfinLoginResolver(preferences, network, notifications, this, messages)

    override val nodeResolver = JellyfinNodeResolver(this, messages)

    override val possibleFormats = listOf(
        Extension.UNKNOWN to Bitrate.B_UNKNOWN
    )

    override fun getTracksByMediaIds(mediaIds: List<String>): List<JellyfinTrack> {
        return emptyList()
    }

    override fun resolveSourceForUrl(track: JellyfinTrack, source: Source): Source {
        logger.w("Jellyfin", "resolveSourceForUrl")
        logger.w("Jellyfin", track.sources.toString())
        return source
    }

    @WorkerThread
    fun getMyAlbums(): List<JellyfinAlbum> {
        val result = jellyfinApi.itemsApi.getItems(
            userId = jellyfinApi.userId,
            includeItemTypes = listOf(BaseItemKind.MUSIC_ALBUM),
            recursive = true
        )

        return albumFromItemResult(result, jellyfinApi);
    }

    fun getMyArtists(): List<JellyfinArtist> {
        val result = jellyfinApi.itemsApi.getItems(
            userId = jellyfinApi.userId,
            includeItemTypes = listOf(BaseItemKind.MUSIC_ARTIST),
            recursive = true
        )
        return artistFromItemResult(result, jellyfinApi);
    }

    fun getMyTracks(page: Int?): List<JellyfinTrack> {
        val result = jellyfinApi.itemsApi.getItems(
            page = page,
            userId = jellyfinApi.userId,
            includeItemTypes = listOf(BaseItemKind.AUDIO),
            recursive = true
        )
        return trackFromItemResult(result, jellyfinApi);
    }

    fun searchTracks(query: String, page: Int): List<JellyfinTrack> {
        val result = jellyfinApi.itemsApi.getItems(
            userId = jellyfinApi.userId,
            includeItemTypes = listOf(BaseItemKind.AUDIO),
            recursive = true,
            searchTerm = query,
            page = page
        )
        return trackFromItemResult(result, jellyfinApi);
    }

    fun searchAlbums(query: String, page: Int): List<JellyfinAlbum> {
        val result = jellyfinApi.itemsApi.getItems(
            userId = jellyfinApi.userId,
            includeItemTypes = listOf(BaseItemKind.MUSIC_ALBUM),
            recursive = true,
            searchTerm = query,
            page = page
        )

        return albumFromItemResult(result, jellyfinApi);
    }

    fun searchArtists(query: String, page: Int): List<JellyfinArtist> {
        val result = jellyfinApi.itemsApi.getItems(
            userId = jellyfinApi.userId,
            includeItemTypes = listOf(BaseItemKind.MUSIC_ARTIST),
            recursive = true,
            searchTerm = query,
            page = page
        )
        return artistFromItemResult(result, jellyfinApi);
    }

    fun getAlbumTracks(albumId: String): List<JellyfinTrack> {
        val result = jellyfinApi.itemsApi.getItems(
            userId = jellyfinApi.userId,
            includeItemTypes = listOf(BaseItemKind.AUDIO),
            recursive = true,
            parentId = UUID.fromString(albumId),
        )
        return trackFromItemResult(result, jellyfinApi);
    }

    fun getArtistAlbums(artistId: String): List<JellyfinAlbum> {
        val result = jellyfinApi.itemsApi.getItems(
            userId = jellyfinApi.userId,
            includeItemTypes = listOf(BaseItemKind.MUSIC_ALBUM),
            recursive = true,
            parentId = UUID.fromString(artistId),
            ids = listOf(UUID.fromString(artistId))
        )

        return albumFromItemResult(result, jellyfinApi);
    }

    fun getArtist(artistId: String): JellyfinArtist {
        val result = jellyfinApi.itemsApi.getItems(
            userId = jellyfinApi.userId,
            includeItemTypes = listOf(BaseItemKind.MUSIC_ARTIST),
            recursive = true,
            ids = listOf(UUID.fromString(artistId)),
            sortBy = "SortName",
            sortOrder = "Ascending"
        )
        return artistFromItemResult(result, jellyfinApi)[0];
    }

    fun getAlbum(albumId: String): JellyfinAlbum {
        val result = jellyfinApi.itemsApi.getItems(
            userId = jellyfinApi.userId,
            includeItemTypes = listOf(BaseItemKind.MUSIC_ALBUM),
            recursive = true,
            ids = listOf(UUID.fromString(albumId)),
            sortBy = "PremiereDate",
            sortOrder = "Descending"
        )
        return albumFromItemResult(result, jellyfinApi)[0];
    }

    fun getTrack(trackId: String, albumId: String?): JellyfinTrack {
        val result = jellyfinApi.itemsApi.getItems(
            userId = jellyfinApi.userId,
            includeItemTypes = listOf(BaseItemKind.AUDIO),
            recursive = true,
            ids = listOf(UUID.fromString(trackId)),
            sortBy = "SortName",
            sortOrder = "Ascending"
        )
        return trackFromItemResult(result, jellyfinApi)[0]
    }

    fun addToFavorite(itemId: String) {
        runBlocking {
            jellyfinApi.userLibraryApi.markFavoriteItem(userId = jellyfinApi.userId!!, itemId = UUID.fromString(itemId))
        }
    }

    fun removeFromFavorite(itemId: String) {
        runBlocking {
            jellyfinApi.userLibraryApi.unmarkFavoriteItem(userId = jellyfinApi.userId!!, itemId = UUID.fromString(itemId))
        }
    }

    fun reportTrackStart(jellyfinTrack: JellyfinTrack) {
//        runBlocking {
//            val info = PlaybackStartInfo(
//                canSeek=false,
//                itemId=UUID.fromString(jellyfinTrack.id),
//                isMuted = false,
//                isPaused = false,
//                playMethod = PlayMethod.DIRECT_STREAM,
//                repeatMode = RepeatMode.REPEAT_NONE
//            )
//
//            jellyfinApi.playStateApi.reportPlaybackStart(info)
//        }
    }

    fun reportTrackEnd(jellyfinTrack: JellyfinTrack, endTimeMs: Int) {
//        runBlocking {
//            val info = PlaybackStopInfo(
//                itemId = UUID.fromString(jellyfinTrack.id),
//                failed = false
//            )
//
//            jellyfinApi.playStateApi.reportPlaybackStopped(info)
//        }
    }
}