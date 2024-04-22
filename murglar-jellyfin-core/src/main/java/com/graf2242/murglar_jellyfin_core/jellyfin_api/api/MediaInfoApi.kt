package com.graf2242.murglar_jellyfin_core.jellyfin_api.api

import com.badmanners.murglar.lib.core.network.NetworkRequest
import com.badmanners.murglar.lib.core.network.ResponseConverters
import com.graf2242.murglar_jellyfin_core.jellyfin_api.JellyfinApi
import kotlinx.serialization.decodeFromString
import org.jellyfin.sdk.api.client.Response
import org.jellyfin.sdk.api.client.extensions.get
import org.jellyfin.sdk.api.client.util.ApiSerializer
import org.jellyfin.sdk.model.UUID
import org.jellyfin.sdk.model.api.BaseItemDtoQueryResult
import org.jellyfin.sdk.model.api.PlaybackInfoResponse

class MediaInfoApi(val api: JellyfinApi) {
    fun getPlaybackInfo(id: UUID): PlaybackInfoResponse {
        val request = NetworkRequest.Builder("${api.serverUrl}/Items/${id}/PlaybackInfo", "GET")
            .addHeader("Authorization", api.getAuthHeader())
            .addParameter("userId", api.userId.toString())
            .build()
        val response = api.network.execute(request, ResponseConverters.asString())
        return ApiSerializer.json.decodeFromString<PlaybackInfoResponse>(response.result)
    }
}