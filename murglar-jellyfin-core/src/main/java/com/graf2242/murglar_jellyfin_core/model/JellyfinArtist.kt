package com.graf2242.murglar_jellyfin_core.model

import com.badmanners.murglar.lib.core.model.artist.BaseArtist
import com.badmanners.murglar.lib.core.utils.contract.Model
import com.graf2242.murglar_jellyfin_core.jellyfin_api.JellyfinApi
import org.jellyfin.sdk.model.api.BaseItemDtoQueryResult
import org.jellyfin.sdk.model.api.ImageType


@Model
class JellyfinArtist(
    id: String,
    name: String,
    smallCoverUrl: String?,
    bigCoverUrl: String?,
    serviceUrl: String?
) : BaseArtist(
    id = id,
    name = name,
    smallCoverUrl = smallCoverUrl,
    bigCoverUrl = bigCoverUrl,
    serviceUrl = serviceUrl
)

fun artistFromItemResult(result: BaseItemDtoQueryResult, jellyfinApi: JellyfinApi): List<JellyfinArtist> {
    if (result.items == null)
        return emptyList();

    return result.items!!.map {
        val smallCoverUrl = when {
            it.imageTags!!.containsKey(ImageType.LOGO) ->
                jellyfinApi.imageApi.getItemImageUrl(itemId = it.id, imageType = ImageType.LOGO)
            else -> null
        }
        val bigCoverUrl = when {
            it.imageTags!!.containsKey(ImageType.LOGO) ->
                jellyfinApi.imageApi.getItemImageUrl(itemId = it.id, imageType = ImageType.PRIMARY)
            else -> null
        }
        JellyfinArtist(
            id = it.id.toString(),
            name = it.name!!,
            smallCoverUrl = smallCoverUrl,
            bigCoverUrl = bigCoverUrl,
            serviceUrl = null
        )
    }
}
