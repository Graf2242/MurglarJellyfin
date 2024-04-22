package com.graf2242.murglar_jellyfin_core.localization

import com.badmanners.murglar.lib.core.localization.DefaultMessages


object JellyfinDefaultMessages : DefaultMessages(), JellyfinMessages {
    override val serviceName = "Jellyfin"
    override val serverUrlSummary = "Jellyfin server address"
    override val serverUrlTitle = "Server Address"
    override val loginHelpText = "Login help text here"
    override val oauthToken = "OAuth token"
    override val password = "Password"
    override val twoFAText = "2FA code from SMS"
    override val illegalOauthTokenFormat = "OAuth token must be in format x-xxxxxx-xxxxxxxxx-xxxxxxxxxxxxxx"
    override val illegalCookieFormat = "Cookie must be alphanumeric and 192 characters long"
    override val radio = "Radio"
}