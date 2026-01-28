package com.yosh.tv_core.domain.extension.anime.model

import com.yosh.tv_core.tachiyomi.extension.anime.model.AnimeExtension

data class AnimeExtensions(
    val updates: List<AnimeExtension.Installed>,
    val installed: List<AnimeExtension.Installed>,
    val available: List<AnimeExtension.Available>,
    val untrusted: List<AnimeExtension.Untrusted>,
)
