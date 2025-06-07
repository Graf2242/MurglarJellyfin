package com.graf2242.murglar_jellyfin_core.jellyfin_api.api

import com.badmanners.murglar.lib.core.network.NetworkRequest
import com.graf2242.murglar_jellyfin_core.jellyfin_api.JellyfinApi
import org.jellyfin.sdk.model.api.PlaybackStartInfo
import org.jellyfin.sdk.model.api.PlaybackStopInfo

class PlayStateApi(val api: JellyfinApi) {
    fun reportPlaybackStopped(info: PlaybackStopInfo) {
        val request = NetworkRequest.Builder("/Sessions/Playing/Stopped", "POST")
            .addHeader("Authorization", api.getAuthHeader())
            .body("""{
                "ItemId": "${info.itemId}",
                "failed": ${info.failed}
            }""".trimIndent())
            .build()
//        api.network.execute(request, ResponseConverters.asString())
    }

    fun reportPlaybackStart(info: PlaybackStartInfo) {
        val request = NetworkRequest.Builder("/Sessions/Playing", "POST")
            .addHeader("Authorization", api.getAuthHeader())
            .body("""{
                "canSeek"=false,
                "itemId"=UUID.fromString(jellyfinTrack.id),
                "isMuted" = false,
                "isPaused" = false,
                "playMethod" = PlayMethod.DIRECT_STREAM,
                "repeatMode" = RepeatMode.REPEAT_NONE
            }""".trimIndent())
            .build()
//        api.network.execute(request, ResponseConverters.asString())
    }
}