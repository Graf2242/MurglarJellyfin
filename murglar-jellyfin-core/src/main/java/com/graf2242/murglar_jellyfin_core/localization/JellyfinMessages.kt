package com.graf2242.murglar_jellyfin_core.localization

import com.badmanners.murglar.lib.core.localization.Messages


interface JellyfinMessages : Messages {
    val serverUrlSummary: String
    val serverUrlTitle: String
    val loginHelpText: String
    val oauthToken: String
    val twoFAText: String
    val illegalOauthTokenFormat: String
    val illegalCookieFormat: String
    val radio: String
}