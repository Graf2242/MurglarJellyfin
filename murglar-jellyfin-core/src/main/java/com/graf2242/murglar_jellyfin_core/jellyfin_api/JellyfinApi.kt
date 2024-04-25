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
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.exception.MissingBaseUrlException
import org.jellyfin.sdk.api.client.util.UrlBuilder

class JellyfinApi(val murglar: JellyfinMurglar, val network: NetworkMiddleware, val logger: LoggerMiddleware) {

    var userId: String? = ""
    var token: String? = ""
    var authType: String? = ""
    var client: String? = "Murglar"
    var version: String? = "0.0.1"
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
        queryParameters: Map<String, Any?> = emptyMap(),
        includeCredentials: Boolean = false,
        ignorePathParameters: Boolean = false,
    ): String = UrlBuilder.buildUrl(
        murglar.serverUrl,
        pathTemplate,
        pathParameters,
        queryParameters.run {
            if (includeCredentials) plus(ApiClient.QUERY_ACCESS_TOKEN to checkNotNull(token))
            else this
        },
        ignorePathParameters,
    )

    val itemsApi = ItemsApi(this)
    val userApi = UserApi(this)
    val imageApi = ImageApi(this)
    val userLibraryApi = UserLibraryApi(this)
    val playStateApi = PlayStateApi(this)
    val mediaInfoApi = MediaInfoApi(this)
    val audioApi = AudioApi(this)
    val apiKeyApi = ApikeyApi(this)
}