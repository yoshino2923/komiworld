package com.yosh.tv_core.domain.source.interactor

import com.yosh.tv_core.domain.source.service.SourcePreferences

class SetMigrateSorting(
    private val preferences: SourcePreferences,
) {

    fun await(mode: Mode, direction: Direction) {
        preferences.migrationSortingMode().set(mode)
        preferences.migrationSortingDirection().set(direction)
    }

    enum class Mode {
        ALPHABETICAL,
        TOTAL,
    }

    enum class Direction {
        ASCENDING,
        DESCENDING,
    }
}
