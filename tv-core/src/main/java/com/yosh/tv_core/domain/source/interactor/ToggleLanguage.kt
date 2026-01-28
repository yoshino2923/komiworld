package com.yosh.tv_core.domain.source.interactor

import com.yosh.tv_core.domain.source.service.SourcePreferences
import tachiyomi.core.common.preference.getAndSet

class ToggleLanguage(
    val preferences: SourcePreferences,
) {

    fun await(language: String) {
        val isEnabled = language in preferences.enabledLanguages().get()
        preferences.enabledLanguages().getAndSet { enabled ->
            if (isEnabled) enabled.minus(language) else enabled.plus(language)
        }
    }
}
