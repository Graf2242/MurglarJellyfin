package com.graf2242.murglar_jellyfin_core.model

import com.badmanners.murglar.lib.core.model.node.NodeType.TRACK
import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.model.track.source.Bitrate
import com.badmanners.murglar.lib.core.model.track.source.Container
import com.badmanners.murglar.lib.core.model.track.source.Extension
import com.badmanners.murglar.lib.core.model.track.source.Source
import com.badmanners.murglar.lib.core.utils.contract.Model
import com.graf2242.murglar_jellyfin_core.converters.bitrateConverter
import com.graf2242.murglar_jellyfin_core.converters.extensionConverter
import com.graf2242.murglar_jellyfin_core.jellyfin_api.JellyfinApi
import org.jellyfin.sdk.model.api.BaseItemDtoQueryResult
import org.jellyfin.sdk.model.api.ImageType
import org.jellyfin.sdk.model.api.MediaStreamType
import org.threeten.bp.LocalDate

const val TICKS_PER_MILLISECOND = 10000


@Model
class JellyfinTrack(
    id: String,
    title: String,
    subtitle: String?,
    artistIds: List<String>,
    artistNames: List<String>,
    albumId: String?,
    albumName: String?,
    albumReleaseDate: LocalDate?,
    indexInAlbum: Int?,
    volumeNumber: Int?,
    durationMs: Long,
    genre: String?,
    explicit: Boolean,
    gain: String?,
    peak: String?,
    sources: List<Source>,
    override val nodeType: String,
    mediaId: String,
    smallCoverUrl: String?,
    bigCoverUrl: String?,
    serviceUrl: String?
) : BaseTrack(
    id = id,
    title = title,
    subtitle = subtitle,
    artistIds = artistIds,
    artistNames = artistNames,
    albumId = albumId,
    albumName = albumName,
    albumReleaseDate = albumReleaseDate,
    indexInAlbum = indexInAlbum,
    volumeNumber = volumeNumber,
    durationMs = durationMs,
    genre = genre,
    explicit = explicit,
    gain = gain,
    peak = peak,
    sources = sources,
    mediaId = mediaId,
    smallCoverUrl = smallCoverUrl,
    bigCoverUrl = bigCoverUrl,
    serviceUrl = serviceUrl
)

fun trackFromItemResult(result: BaseItemDtoQueryResult, jellyfinApi: JellyfinApi): List<JellyfinTrack> {
    if (result.items == null)
        return emptyList();

    return result.items!!.map {
        val bigCoverUrl = if (it.imageTags!!.containsKey(ImageType.PRIMARY)) jellyfinApi.imageApi.getItemImageUrl(itemId = it.id, imageType = ImageType.PRIMARY) else null
        val smallCoverUrl = if (it.imageTags!!.containsKey(ImageType.LOGO)) jellyfinApi.imageApi.getItemImageUrl(itemId = it.id, imageType = ImageType.LOGO) else bigCoverUrl

        val playbackInfo = jellyfinApi.mediaInfoApi.getPlaybackInfo(it.id)

        val sourcesList = playbackInfo.mediaSources.map {itr ->
            itr.mediaStreams!!.filter { iter -> iter.type == MediaStreamType.AUDIO }.map { iter ->
                val url = jellyfinApi.audioApi.getAudioStreamUrl(it.id.toString(), container = it.container, audioCodec = iter.codec, audioChannels = iter.channels, audioBitRate = iter.bitRate, audioSampleRate = iter.sampleRate)
                Source(id=itr.id.toString(), url=url, container = Container.PROGRESSIVE, extension = extensionConverter(iter.codec?:""), tag=iter.displayTitle!!, bitrate = bitrateConverter(iter.bitRate!!), size=itr.size!!)
            }
        }


        JellyfinTrack(
            id = it.id.toString(),
            title = it.name!!,
            subtitle = null,
            artistIds = it.artistItems!!.map { itr -> itr.id.toString() },
            artistNames = it.artistItems!!.map { itr -> itr.name.toString() },
            albumId = it.albumId.toString(),
            albumName = it.album,
            albumReleaseDate = LocalDate.parse(it.premiereDate!!.toString().split("T")[0]),
            indexInAlbum = it.indexNumber,
            volumeNumber = null,
            durationMs = it.runTimeTicks!! / TICKS_PER_MILLISECOND,
            genre = null,
            explicit = false,
            gain = null,
            peak = null,
            sources = sourcesList.flatten(),
            mediaId = it.userData!!.key!!,
            smallCoverUrl = smallCoverUrl,
            bigCoverUrl = bigCoverUrl,
            serviceUrl = null,
            nodeType = TRACK
        )
    }
}
