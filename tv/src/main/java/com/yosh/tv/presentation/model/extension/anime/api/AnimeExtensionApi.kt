package com.yosh.tv.presentation.model.extension.anime.api

import com.yosh.tv.domain.source.service.SourcePreferences
import eu.kanade.tachiyomi.network.NetworkHelper
import kotlinx.serialization.json.Json
import mihon.domain.extensionrepo.anime.interactor.GetAnimeExtensionRepo
import mihon.domain.extensionrepo.anime.interactor.UpdateAnimeExtensionRepo
import tachiyomi.core.common.preference.PreferenceStore
import uy.kohesive.injekt.injectLazy

internal class AnimeExtensionApi {

    private val networkService: NetworkHelper by injectLazy()
    private val preferenceStore: PreferenceStore by injectLazy()
    private val getExtensionRepo: GetAnimeExtensionRepo by injectLazy()
    private val updateExtensionRepo: UpdateAnimeExtensionRepo by injectLazy()
    //private val animeExtensionManager: AnimeExtensionManager by injectLazy()
    private val json: Json by injectLazy()
    private val sourcePreferences: SourcePreferences by injectLazy()

}
