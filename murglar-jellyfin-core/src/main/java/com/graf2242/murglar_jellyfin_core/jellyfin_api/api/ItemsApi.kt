package com.graf2242.murglar_jellyfin_core.jellyfin_api.api

import com.badmanners.murglar.lib.core.network.NetworkRequest
import com.badmanners.murglar.lib.core.network.ResponseConverters
import com.graf2242.murglar_jellyfin_core.jellyfin_api.JellyfinApi
import kotlinx.serialization.decodeFromString
import org.jellyfin.sdk.api.client.util.ApiSerializer
import org.jellyfin.sdk.model.api.BaseItemDtoQueryResult
import org.jellyfin.sdk.model.api.BaseItemKind
import java.util.UUID

class ItemsApi(var api: JellyfinApi) {
    val page_size = 50
    fun getItems(
        userId: String? = api.userId,
        includeItemTypes: List<BaseItemKind> = emptyList(),
        recursive: Boolean? = null,
        searchTerm: String? = null,
        parentId: UUID? = null,
        ids: List<UUID> = emptyList(),
        page: Int? = null,
        sortBy: String? = null,
        sortOrder: String? = null
    ): BaseItemDtoQueryResult {
        var itemTypes = includeItemTypes[0].serialName
        for (i in 1 until includeItemTypes.count()) {
            itemTypes += ",${includeItemTypes[i]}"
        }
        val request = NetworkRequest.Builder("${api.serverUrl}/Items", "GET")
            .addHeader("Authorization", api.getAuthHeader())
            .addParameter("sortOrder", "Descending")
            .apply {
                if (sortBy != null)
                    addParameter("sortBy", sortBy)
                if (sortOrder != null)
                    addParameter("sortOrder", sortOrder)
                if (userId != null)
                    addParameter("userId", userId)
                if (includeItemTypes.isNotEmpty())
                    addParameter("includeItemTypes", itemTypes)
                if (recursive != null)
                    addParameter("recursive", recursive)
                if (searchTerm != null)
                    addParameter("searchTerm", searchTerm)
                if (parentId != null)
                    addParameter("parentId", parentId)
                if (ids.isNotEmpty())
                    addParameter("ids", ids.map { it.toString() }.joinToString { "," })
                if (page != null) {
                    addParameter("startIndex", page * page_size)
                    addParameter("limit", page_size)
                }
            }
            .build()
        val response = api.network.execute(request, ResponseConverters.asString())
        return ApiSerializer.json.decodeFromString<BaseItemDtoQueryResult>(response.result)
    }
}