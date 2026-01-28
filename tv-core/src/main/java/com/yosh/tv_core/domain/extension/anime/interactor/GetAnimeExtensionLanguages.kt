package com.yosh.tv_core.domain.extension.anime.interactor

import com.yosh.tv_core.domain.source.service.SourcePreferences
import com.yosh.tv_core.tachiyomi.extension.anime.AnimeExtensionManager
import com.yosh.tv_core.tachiyomi.util.system.LocaleHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAnimeExtensionLanguages(
    private val preferences: SourcePreferences,
    private val extensionManager: AnimeExtensionManager,
) {
    fun subscribe(): Flow<List<String>> {
        return combine(
            preferences.enabledLanguages().changes(),
            extensionManager.availableExtensionsFlow,
        ) { enabledLanguage, availableExtensions ->
            availableExtensions
                .flatMap { ext ->
                    if (ext.sources.isEmpty()) {
                        listOf(ext.lang)
                    } else {
                        ext.sources.map { it.lang }
                    }
                }
                .distinct()
                .sortedWith(
                    compareBy<String> { it !in enabledLanguage }.then(LocaleHelper.comparator),
                )
        }
    }
}
