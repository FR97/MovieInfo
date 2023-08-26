package fr97.movieinfo.data.api

import fr97.movieinfo.feature.videoslist.VideoModel

data class VideosListResponse(
    val id: Int = 0,
    val results: List<VideosListItemResponse> = emptyList()

)

data class VideosListItemResponse(
    val id: String = "",
    val key: String = "",
    val name: String = "",
    val site: String = "",
    val type: String = "",
    val size: Int = 0
)

fun VideosListItemResponse.toModel(): VideoModel {
    return VideoModel(id, key, name, site, type, size)
}