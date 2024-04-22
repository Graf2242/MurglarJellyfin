package com.graf2242.murglar_jellyfin_core.model

import com.badmanners.murglar.lib.core.model.playlist.BasePlaylist
import com.badmanners.murglar.lib.core.utils.contract.Model


@Model
class JellyfinPlaylist(
    id: String,
    title: String,
    description: String?,
    smallCoverUrl: String?,
    bigCoverUrl: String?,
    tracksCount: Int,
    val ownerLogin: String,
    val ownerId: String,
    serviceUrl: String
) : BasePlaylist(
    id = id,
    title = title,
    description = description,
    tracksCount = tracksCount,
    explicit = false,
    smallCoverUrl = smallCoverUrl,
    bigCoverUrl = bigCoverUrl,
    serviceUrl = serviceUrl
)