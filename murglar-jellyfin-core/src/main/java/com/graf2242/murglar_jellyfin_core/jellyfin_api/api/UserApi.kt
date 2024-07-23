package com.graf2242.murglar_jellyfin_core.jellyfin_api.api

import com.badmanners.murglar.lib.core.network.NetworkRequest
import com.badmanners.murglar.lib.core.network.ResponseConverters
import com.badmanners.murglar.lib.core.utils.getJsonObject
import com.badmanners.murglar.lib.core.utils.string
import com.graf2242.murglar_jellyfin_core.jellyfin_api.JellyfinApi
import org.jellyfin.sdk.model.api.AuthenticateUserByName

class UserApi(val api: JellyfinApi) {
    data class AuthResult(val userId: String?, val token: String?, val userName: String?)

    fun authenticateUserByName(data: AuthenticateUserByName): AuthResult {
        val request = NetworkRequest.Builder("${api.serverUrl}/Users/AuthenticateByName" , "POST")
            .addHeader("Authorization", api.getAuthHeader())
            .body("""{ "username": "${data.username}", "pw": "${data.pw}" }""")
            .build()
        val response = api.network.execute(request, ResponseConverters.asJsonObject())
        val result = response.result
        return AuthResult(
            userId = result.getJsonObject("User")["Id"]?.string,
            token = result["AccessToken"].toString(),
            userName = result.getJsonObject("User")["Name"].toString()
        )
    }

    fun checkToken(): Boolean {
        val request = NetworkRequest.Builder("${api.serverUrl}/Users" , "GET")
            .addHeader("Authorization", api.getAuthHeader())
            .build()
        val response = api.network.execute(request, ResponseConverters.asString())
        api.logger.w("Jellyfin", "result: ${response.statusCode}, isSuccessful: $response.isSuccessful")
        return response.isSuccessful
    }
}