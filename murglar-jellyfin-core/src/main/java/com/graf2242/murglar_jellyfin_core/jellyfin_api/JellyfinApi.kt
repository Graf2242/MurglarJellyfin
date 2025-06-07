package com.graf2242.murglar_jellyfin_core.jellyfin_api

import com.badmanners.murglar.lib.core.log.LoggerMiddleware
import com.badmanners.murglar.lib.core.network.NetworkMiddleware
import com.graf2242.murglar_jellyfin_core.JellyfinMurglar
import com.graf2242.murglar_jellyfin_core.jellyfin_api.api.ApikeyApi
import com.graf2242.murglar_jellyfin_core.jellyfin_api.api.AudioApi
import com.graf2242.murglar_jellyfin_core.jellyfin_api.api.ImageApi
import com.graf2242.murglar_jellyfin_core.jellyfin_api.api.ItemsApi
import com.graf2242.murglar_jellyfin_core.jellyfin_api.api.MediaInfoApi
import com.graf2242.murglar_jellyfin_core.jellyfin_api.api.PlayStateApi
import com.graf2242.murglar_jellyfin_core.jellyfin_api.api.UserApi
import com.graf2242.murglar_jellyfin_core.jellyfin_api.api.UserLibraryApi
import com.graf2242.murglar_jellyfin_core.login.JellyfinLoginResolver


class JellyfinApi(val murglar: JellyfinMurglar, val network: NetworkMiddleware, val logger: LoggerMiddleware) {

    var userId: String? = ""
    var token: String? = ""
    var authType: String? = ""
    var client: String? = "Murglar"
    var version: String? = "2"
    var deviceId: String? = "deadbeef"
    var deviceName: String? = "Murglar"

    fun getAuthHeader(): String =
        when (authType) {
            JellyfinLoginResolver.USERNAME_LOGIN_VARIANT -> "MediaBrowser Client=\"${client}\", Version=\"${version}\", DeviceId=\"${deviceId}\", Device=\"${deviceName}\", Token=${token}"
            JellyfinLoginResolver.TOKEN_LOGIN_VARIANT -> "MediaBrowser Token=${token}"
            else -> ""
        }

    val serverUrl: String
        get() = murglar.serverUrl

    fun createUrl(
        pathTemplate: String,
        pathParameters: Map<String, Any?> = emptyMap(),
        queryParameters: Map<String, Any?> = emptyMap()
    ): String {
        var url = serverUrl
        var path = pathTemplate

            for ((key, value) in pathParameters) {
                if (value != null) {
                    path = path.replace("{$key}", value.toString())
                }
            }

        url = when {
            url.endsWith("/") && path.startsWith("/") -> url + path.substring(1)
            !url.endsWith("/") && !path.startsWith("/") -> "$url/$path"
            else -> url + path
        }

        if (queryParameters.isNotEmpty()) {
            val queryString = queryParameters.entries
                .filter { it.value != null }
                .joinToString("&") { "${it.key}=${it.value.toString().encodeUrl()}" }

            if (queryString.isNotEmpty())
                url = "$url?$queryString"
        }

        return url
    }

    // Helper function to URL-encode parameter values
    private fun String.encodeUrl(): String {
        return java.net.URLEncoder.encode(this, "UTF-8")
            .replace("+", "%20") // Replace + with %20 for spaces
    }

    val itemsApi = ItemsApi(this)
    val userApi = UserApi(this)
    val imageApi = ImageApi(this)
    val userLibraryApi = UserLibraryApi(this)
    val playStateApi = PlayStateApi(this)
    val mediaInfoApi = MediaInfoApi(this)
    val audioApi = AudioApi(this)
    val apiKeyApi = ApikeyApi(this)
}