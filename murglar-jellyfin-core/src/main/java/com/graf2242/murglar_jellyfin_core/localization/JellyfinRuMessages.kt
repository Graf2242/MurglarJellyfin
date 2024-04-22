package com.graf2242.murglar_jellyfin_core.localization

import com.badmanners.murglar.lib.core.localization.RussianMessages


object JellyfinRuMessages : RussianMessages(), JellyfinMessages {
    override val serviceName = "Jellyfin"
    override val serverUrlSummary = "Адрес до сервера Jellyfin"
    override val serverUrlTitle = "Адрес сервера"
    override val loginHelpText = "Текст помощи со входом"
    override val oauthToken = "OAuth токен"
    override val password = "Пароль"
    override val twoFAText = "Код подтверждения из SMS"
    override val illegalOauthTokenFormat = "OAuth токен должен быть формата x-xxxxxx-xxxxxxxxx-xxxxxxxxxxxxxx"
    override val illegalCookieFormat = "Cookie должна состоять только из чисел и букв и быть длиной в 192 символа"
    override val radio = "Радио"
}