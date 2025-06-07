package com.graf2242.murglar_jellyfin_core.jellyfin_api.api

import com.graf2242.murglar_jellyfin_core.jellyfin_api.JellyfinApi

class AudioApi(val api: JellyfinApi) {
    fun getAudioStreamUrl(
        id: String,
        container: String? = null,
        audioCodec: String? = null,
        audioChannels: Int? = null,
        audioBitRate: Int? = null,
        audioSampleRate: Int? = null,
    ): String {
        val pathParameters = buildMap<String, Any?>(1) {
            put("itemId", id)
        }
        val queryParameters = buildMap<String, Any?>(48) {
            put("container", container)
//            put("static", static)
//            put("params", params)
//            put("tag", tag)
//            put("deviceProfileId", deviceProfileId)
//            put("playSessionId", playSessionId)
//            put("segmentContainer", segmentContainer)
//            put("segmentLength", segmentLength)
//            put("minSegments", minSegments)
//            put("mediaSourceId", mediaSourceId)
//            put("deviceId", deviceId)
            put("audioCodec", audioCodec)
//            put("enableAutoStreamCopy", enableAutoStreamCopy)
//            put("allowVideoStreamCopy", allowVideoStreamCopy)
//            put("allowAudioStreamCopy", allowAudioStreamCopy)
//            put("breakOnNonKeyFrames", breakOnNonKeyFrames)
            put("audioSampleRate", audioSampleRate)
//            put("maxAudioBitDepth", maxAudioBitDepth)
            put("audioBitRate", audioBitRate)
            put("audioChannels", audioChannels)
//            put("maxAudioChannels", maxAudioChannels)
//            put("profile", profile)
//            put("level", level)
//            put("framerate", framerate)
//            put("maxFramerate", maxFramerate)
//            put("copyTimestamps", copyTimestamps)
//            put("startTimeTicks", startTimeTicks)
//            put("width", width)
//            put("height", height)
//            put("videoBitRate", videoBitRate)
//            put("subtitleStreamIndex", subtitleStreamIndex)
//            put("subtitleMethod", subtitleMethod)
//            put("maxRefFrames", maxRefFrames)
//            put("maxVideoBitDepth", maxVideoBitDepth)
//            put("requireAvc", requireAvc)
//            put("deInterlace", deInterlace)
//            put("requireNonAnamorphic", requireNonAnamorphic)
//            put("transcodingMaxAudioChannels", transcodingMaxAudioChannels)
//            put("cpuCoreLimit", cpuCoreLimit)
//            put("liveStreamId", liveStreamId)
//            put("enableMpegtsM2TsMode", enableMpegtsM2TsMode)
//            put("videoCodec", videoCodec)
//            put("subtitleCodec", subtitleCodec)
//            put("transcodeReasons", transcodeReasons)
//            put("audioStreamIndex", audioStreamIndex)
//            put("videoStreamIndex", videoStreamIndex)
//            put("context", context)
//            put("streamOptions", streamOptions)
        }
        return api.createUrl("/Audio/{itemId}/stream", pathParameters, queryParameters)
    }
}