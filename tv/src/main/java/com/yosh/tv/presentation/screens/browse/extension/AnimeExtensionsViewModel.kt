package com.yosh.tv.presentation.screens.browse.extension

fun fakeTvAnimeExtensionsState(): TvAnimeExtensionsState {
    return TvAnimeExtensionsState(
        isLoading = false,
        isRefreshing = false,
        updateAllEnabled = true,
        groups = listOf(
            TvExtensionsGroup(
                header = "Updates pending",
                isUpdatesHeader = true,
                items = listOf(
                    TvAnimeExtension(
                        pkgName = "ext.gogoanime",
                        name = "GogoAnime",
                        type = TvExtensionType.Installed,
                        langLabel = "English",
                        versionName = "1.4.2",
                        repoName = "official",
                        hasUpdate = true,
                        installStep = TvInstallStep.Idle,
                    ),
                    TvAnimeExtension(
                        pkgName = "ext.animeunity",
                        name = "AnimeUnity",
                        type = TvExtensionType.Installed,
                        langLabel = "Italiano",
                        versionName = "2.0.1",
                        repoName = "official",
                        hasUpdate = true,
                        installStep = TvInstallStep.Downloading,
                    ),
                ),
            ),

            TvExtensionsGroup(
                header = "Installed",
                items = listOf(
                    TvAnimeExtension(
                        pkgName = "ext.crunchyroll",
                        name = "Crunchyroll",
                        type = TvExtensionType.Installed,
                        langLabel = "English",
                        versionName = "3.1.0",
                        repoName = "official",
                        hasUpdate = false,
                        installStep = TvInstallStep.Idle,
                    ),
                    TvAnimeExtension(
                        pkgName = "ext.animesaturn",
                        name = "AnimeSaturn",
                        type = TvExtensionType.Installed,
                        langLabel = "Italiano",
                        versionName = "1.9.4",
                        repoName = "community",
                        isNsfw = true,
                        installStep = TvInstallStep.Idle,
                    ),
                ),
            ),

            TvExtensionsGroup(
                header = "English",
                items = listOf(
                    TvAnimeExtension(
                        pkgName = "ext.animepahe",
                        name = "AnimePahe",
                        type = TvExtensionType.Available,
                        langLabel = "English",
                        versionName = "1.0.0",
                        repoName = "official",
                        installStep = TvInstallStep.Idle,
                    ),
                    TvAnimeExtension(
                        pkgName = "ext.animeflix",
                        name = "AnimeFlix",
                        type = TvExtensionType.Available,
                        langLabel = "English",
                        versionName = "0.9.2",
                        repoName = "community",
                        installStep = TvInstallStep.Idle,
                    ),
                ),
            ),

            TvExtensionsGroup(
                header = "Italiano",
                items = listOf(
                    TvAnimeExtension(
                        pkgName = "ext.animeworld",
                        name = "AnimeWorld",
                        type = TvExtensionType.Available,
                        langLabel = "Italiano",
                        versionName = "2.3.0",
                        repoName = "community",
                        installStep = TvInstallStep.Idle,
                    ),
                ),
            ),
        ),
    )
}
