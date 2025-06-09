package com.graf2242.murglar_jellyfin_core.jellyfin_api.api

import com.graf2242.murglar_jellyfin_core.jellyfin_api.JellyfinApi
import org.jellyfin.sdk.model.UUID
import org.jellyfin.sdk.model.api.ImageType

class ImageApi(val api: JellyfinApi) {
    fun getItemImageUrl(itemId: UUID, imageType: ImageType): String {
        val pathParameters = buildMap<String, Any?>(2) {
            put("itemId", itemId)
            put("imageType", imageType)
        }
        val queryParameters = buildMap<String, Any?>(16) {
//            put("maxWidth", maxWidth)
//            put("maxHeight", maxHeight)
//            put("width", width)
//            put("height", height)
//            put("quality", quality)
//            put("fillWidth", fillWidth)
//            put("fillHeight", fillHeight)
//            put("tag", tag)
//            put("format", format)
//            put("addPlayedIndicator", addPlayedIndicator)
//            put("percentPlayed", percentPlayed)
//            put("unplayedCount", unplayedCount)
//            put("blur", blur)
//            put("backgroundColor", backgroundColor)
//            put("foregroundLayer", foregroundLayer)
//            put("imageIndex", imageIndex)
        }
        return api.createUrl("/Items/{itemId}/Images/{imageType}", pathParameters, queryParameters)
    }
}