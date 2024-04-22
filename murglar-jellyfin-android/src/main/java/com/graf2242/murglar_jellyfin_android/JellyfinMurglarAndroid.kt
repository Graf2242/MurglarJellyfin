package com.graf2242.murglar_jellyfin_android

import com.badmanners.murglar.lib.core.log.LoggerMiddleware
import com.badmanners.murglar.lib.core.network.NetworkMiddleware
import com.badmanners.murglar.lib.core.notification.NotificationMiddleware
import com.badmanners.murglar.lib.core.preference.PreferenceMiddleware
import com.graf2242.murglar_jellyfin_core.JellyfinMurglar

//class JellyfinMurglarAndroid(
//    id: String,
//    preferences: PreferenceMiddleware,
//    network: NetworkMiddleware,
//    notifications: NotificationMiddleware,
//    logger: LoggerMiddleware
//) : JellyfinMurglar(id, preferences, network, notifications, logger) {
//

//    override val jellyfinApi = createJellyfin {
//        clientInfo = ClientInfo(name = "MurglarJellyfin", version = "0.0.1")
//        context = getApplicationContext()
//    }.createApi(baseUrl = SAMPLE_DOMAIN)
//}