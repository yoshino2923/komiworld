package com.yosh.tv_core.domain.source.manga.interactor

import com.yosh.tv_core.domain.source.service.SourcePreferences
import tachiyomi.core.common.preference.getAndSet
import tachiyomi.domain.source.manga.model.Source

class ToggleMangaSourcePin(
    private val preferences: SourcePreferences,
) {

    fun await(source: Source) {
        val isPinned = source.id.toString() in preferences.pinnedMangaSources().get()
        preferences.pinnedMangaSources().getAndSet { pinned ->
            if (isPinned) pinned.minus("${source.id}") else pinned.plus("${source.id}")
        }
    }
}
