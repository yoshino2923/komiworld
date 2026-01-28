package com.yosh.tv_core.tachiyomi.data.track.kitsu.dto

import kotlinx.serialization.Serializable

@Serializable
data class KitsuAddEntryResult(
    val data: KitsuAddEntryItem,
)

@Serializable
data class KitsuAddEntryItem(
    val id: Long,
)
