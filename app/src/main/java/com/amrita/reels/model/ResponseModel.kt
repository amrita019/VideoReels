package com.amrita.reels.model

data class ResponseModel(val items: List<DataItem>)

data class DataItem(
    val kind: String,
    val id: VideoId,
    val snippet: Snippet
)

data class VideoId(
    val kind: String,
    val videoId: String
)

data class Snippet(
    val title: String,
    val thumbnails: DefaultThumbnail
)

data class DefaultThumbnail(
    val default: ThumbnailData
)

data class ThumbnailData(
    val url: String,
    val height: Int,
    val width: Int
)