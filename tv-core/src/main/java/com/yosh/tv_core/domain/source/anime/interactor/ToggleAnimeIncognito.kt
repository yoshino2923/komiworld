package com.yosh.tv_core.domain.source.anime.interactor

import com.yosh.tv_core.domain.source.service.SourcePreferences
import tachiyomi.core.common.preference.getAndSet

class ToggleAnimeIncognito(
    private val preferences: SourcePreferences,
) {
    fun await(extensions: String, enable: Boolean) {
        preferences.incognitoAnimeExtensions().getAndSet {
            if (enable) it.plus(extensions) else it.minus(extensions)
        }
    }
}
