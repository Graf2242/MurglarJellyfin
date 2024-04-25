package com.graf2242.murglar_jellyfin_core.jellyfin_api.api

import com.badmanners.murglar.lib.core.network.NetworkRequest
import com.badmanners.murglar.lib.core.network.ResponseConverters
import com.badmanners.murglar.lib.core.utils.getInt
import com.graf2242.murglar_jellyfin_core.jellyfin_api.JellyfinApi
import org.jellyfin.sdk.model.api.AuthenticationInfoQueryResult

class ApikeyApi(val api: JellyfinApi) {

    fun keys(token: String): AuthenticationInfoQueryResult? {
        val request = NetworkRequest.Builder("${api.serverUrl}/Auth/Keys" , "GET")
            .addHeader("Authorization", "MediaBrowser Token=${token}")
            .build()
        val response = api.network.execute(request, ResponseConverters.asJsonObject())
        if (response.statusCode != 200)
            return null
        val result = response.result
        return AuthenticationInfoQueryResult(
            startIndex = result.getInt("StartIndex"),
            totalRecordCount = result.getInt("TotalRecordCount")
        )
    }
}