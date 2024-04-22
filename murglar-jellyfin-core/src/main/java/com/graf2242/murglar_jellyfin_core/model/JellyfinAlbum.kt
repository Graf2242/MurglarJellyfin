package com.graf2242.murglar_jellyfin_core.model

import com.badmanners.murglar.lib.core.model.album.BaseAlbum
import com.badmanners.murglar.lib.core.utils.contract.Model
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.extensions.imageApi
import org.jellyfin.sdk.api.client.extensions.itemsApi
import org.jellyfin.sdk.model.api.BaseItemDtoQueryResult
import org.jellyfin.sdk.model.api.ImageType
import org.threeten.bp.LocalDate
import com.badmanners.murglar.lib.core.model.node.NodeType.ALBUM
import com.graf2242.murglar_jellyfin_core.jellyfin_api.JellyfinApi


@Model
class JellyfinAlbum(
    id: String,
    title: String,
    additionalInfo: String?,
    releaseDate: LocalDate?,
    smallCoverUrl: String?,
    bigCoverUrl: String?,
    tracksCount: Int,
    artistIds: List<String>,
    artistNames: List<String>,
    genre: String?,
    override val nodeType: String,
    explicit: Boolean,
    serviceUrl: String?
) : BaseAlbum(
    id = id,
    title = title,
    description = additionalInfo,
    artistIds = artistIds,
    artistNames = artistNames,
    tracksCount = tracksCount,
    releaseDate = releaseDate,
    genre = genre,
    explicit = explicit,
    smallCoverUrl = smallCoverUrl,
    bigCoverUrl = bigCoverUrl,
    serviceUrl = serviceUrl
)

fun albumFromItemResult(result: BaseItemDtoQueryResult, jellyfinApi: JellyfinApi): List<JellyfinAlbum> {
    if (result.items == null)
        return emptyList();

    return result.items!!.map {
        val bigCoverUrl = if (it.imageTags!!.containsKey(ImageType.PRIMARY)) jellyfinApi.imageApi.getItemImageUrl(itemId = it.id, imageType = ImageType.PRIMARY) else null
        val smallCoverUrl = if (it.imageTags!!.containsKey(ImageType.LOGO)) jellyfinApi.imageApi.getItemImageUrl(itemId = it.id, imageType = ImageType.LOGO) else bigCoverUrl
        JellyfinAlbum(
            id = it.id.toString(),
            title = it.name!!,
            additionalInfo = null,
            artistIds = it.albumArtists!!.map { itr -> itr.id.toString() },
            artistNames = it.artistItems!!.map { itr -> itr.id.toString() },
            tracksCount = 0,
            releaseDate = LocalDate.parse(it.premiereDate!!.toString().split("T")[0]),
            genre = null,
            explicit = false,
            smallCoverUrl = smallCoverUrl,
            bigCoverUrl = bigCoverUrl,
            serviceUrl = null,
            nodeType = ALBUM
        )
    }
}