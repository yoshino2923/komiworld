package com.yosh.tv_core.domain.extension.manga.model

import com.yosh.tv_core.tachiyomi.extension.manga.model.MangaExtension

data class MangaExtensions(
    val updates: List<MangaExtension.Installed>,
    val installed: List<MangaExtension.Installed>,
    val available: List<MangaExtension.Available>,
    val untrusted: List<MangaExtension.Untrusted>,
)
