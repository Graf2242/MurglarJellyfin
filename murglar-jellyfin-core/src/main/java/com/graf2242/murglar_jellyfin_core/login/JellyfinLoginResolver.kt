package com.graf2242.murglar_jellyfin_core.login

import com.badmanners.murglar.lib.core.login.CredentialLoginStep
import com.badmanners.murglar.lib.core.login.CredentialsLoginVariant
import com.badmanners.murglar.lib.core.login.CredentialsLoginVariant.Credential
import com.badmanners.murglar.lib.core.login.LoginResolver
import com.badmanners.murglar.lib.core.login.SuccessfulLogin
import com.badmanners.murglar.lib.core.login.WebLoginVariant
import com.badmanners.murglar.lib.core.network.NetworkMiddleware
import com.badmanners.murglar.lib.core.notification.NotificationMiddleware
import com.badmanners.murglar.lib.core.preference.PreferenceMiddleware
import com.badmanners.murglar.lib.core.webview.WebViewProvider
import com.graf2242.murglar_jellyfin_core.JellyfinMurglar
import com.graf2242.murglar_jellyfin_core.localization.JellyfinMessages
import org.jellyfin.sdk.api.client.exception.ApiClientException
import org.jellyfin.sdk.model.api.AuthenticateUserByName
import javax.security.auth.login.FailedLoginException

class JellyfinLoginResolver(
    private val preferences: PreferenceMiddleware,
    private val network: NetworkMiddleware,
    private val notifications: NotificationMiddleware,
    private val murglar: JellyfinMurglar,
    private val messages: JellyfinMessages
) : LoginResolver {

    companion object {

        const val USERNAME_LOGIN_VARIANT = "username_login"
        const val USERNAME_CREDENTIAL = "username"
        const val PASSWORD_CREDENTIAL = "password"

        const val TOKEN_LOGIN_VARIANT = "token_login"
        const val TOKEN_CREDENTIAL = "token"

        private const val USERNAME_PREFERENCE = "username"
        private const val TOKEN_PREFERENCE = "token"
        private const val USERID_PREFERENCE = "userId"
        private const val AUTH_TYPE = "auth_type"

        private const val NO_VALUE = ""
    }

    val accessToken: String
        get() = preferences.getString(TOKEN_PREFERENCE, NO_VALUE)

    val userId: String
        get() = preferences.getString(USERID_PREFERENCE, NO_VALUE)

    val username: String
        get() = preferences.getString(USERNAME_PREFERENCE, NO_VALUE)

    val authType: String
        get() = preferences.getString(AUTH_TYPE, NO_VALUE)

    override val credentialsLoginVariants = listOf(
        CredentialsLoginVariant(
            id = USERNAME_LOGIN_VARIANT,
            label = { messages.loginWith(email = true) },
            credentials = listOf(
                Credential(USERNAME_CREDENTIAL, messages::username),
                Credential(PASSWORD_CREDENTIAL, messages::password)
            )
        ),
        CredentialsLoginVariant(
            id = TOKEN_LOGIN_VARIANT,
            label =  { messages.loginWith(token = true) },
            credentials = listOf(
                Credential(TOKEN_CREDENTIAL, messages::token)
            )
        )
    )

    override val isLogged: Boolean
        get() = accessToken != NO_VALUE

    override val loginInfo: String
        get() = when {
            isLogged -> "${messages.youAreLoggedIn}: $username"
            else -> messages.youAreNotLoggedIn
        }

    override val webLoginVariants: List<WebLoginVariant>
        get() = emptyList()


    override fun credentialsLogin(
        loginVariantId: String,
        args: Map<String, String>
    ): CredentialLoginStep {
        when (loginVariantId){
            USERNAME_LOGIN_VARIANT -> {
                val userApi = murglar.jellyfinApi.userApi
                try {
                     val authenticationResult = userApi.authenticateUserByName(
                             data = AuthenticateUserByName(
                                 username = args["username"],
                                 pw = args["password"],
                             )
                         )
                    if (authenticationResult.token != null)
                    {
                        murglar.jellyfinApi.token = authenticationResult.token
                        murglar.jellyfinApi.userId = authenticationResult.userId
                        murglar.jellyfinApi.authType = USERNAME_LOGIN_VARIANT
                        preferences.setString(TOKEN_PREFERENCE, authenticationResult.token)
                        preferences.setString(USERID_PREFERENCE, authenticationResult.userId.toString())
                        preferences.setString(USERNAME_PREFERENCE, authenticationResult.userName!!)
                        preferences.setString(AUTH_TYPE, USERNAME_LOGIN_VARIANT)
                    }
                } catch(err: ApiClientException) {
                    println("Something went wrong! ${err.message}")
                }

            }
            TOKEN_LOGIN_VARIANT -> {
                if (murglar.jellyfinApi.apiKeyApi.keys(args["token"]?:"") == null)
                    throw FailedLoginException("Wrong token")
                murglar.jellyfinApi.token = args["token"]
                murglar.jellyfinApi.authType = TOKEN_LOGIN_VARIANT
                preferences.setString(TOKEN_PREFERENCE, args["token"]!!)
                preferences.setString(AUTH_TYPE, TOKEN_LOGIN_VARIANT)
            }
        }

        return SuccessfulLogin
    }

    override fun logout() {
        network.clearAllCookies()
        preferences.remove(TOKEN_PREFERENCE)
        preferences.remove(USERNAME_PREFERENCE)
    }

    override fun webLogin(loginVariantId: String, webViewProvider: WebViewProvider): Boolean {
        throw UnsupportedOperationException()
    }

}