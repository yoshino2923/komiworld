package com.yosh.tv.presentation.screens.browse.extension

fun fakeStateForTvAnimeExtensionScreen(): TvAnimeExtensionsState {
    return TvAnimeExtensionsState(
        isLoading = false,
        isRefreshing = false,
        updateAllEnabled = true,
        groups = listOf(
            TvExtensionsGroup(
                header = "Updates pending",
                isUpdatesHeader = true,
                items = listOf(
                    fakeInstalledExtension(
                        pkg = "eu.kanade.tachiyomi.extension.en.gogoanime",
                        name = "Gogoanime",
                        version = "1.4.2 → 1.5.0",
                        hasUpdate = true,
                    ),
                    fakeInstalledExtension(
                        pkg = "eu.kanade.tachiyomi.extension.it.animeworld",
                        name = "AnimeWorld",
                        version = "2.1.0 → 2.2.0",
                        hasUpdate = true,
                        isNsfw = true,
                    ),
                )
            ),
            TvExtensionsGroup(
                header = "Installed",
                items = listOf(
                    fakeInstalledExtension(
                        pkg = "eu.kanade.tachiyomi.extension.en.nineanime",
                        name = "9Anime",
                        version = "3.0.1",
                    ),
                    fakeInstalledExtension(
                        pkg = "eu.kanade.tachiyomi.extension.en.zoro",
                        name = "Zoro",
                        version = "1.9.4",
                        isObsolete = true,
                    ),
                    fakeInstalledExtension(
                        pkg = "eu.kanade.tachiyomi.extension.en.crunchyroll",
                        name = "Crunchyroll",
                        version = "0.8.0",
                        installStep = TvInstallStep.Downloading,
                    ),
                )
            ),
            TvExtensionsGroup(
                header = "Available",
                items = listOf(
                    fakeAvailableExtension(
                        pkg = "eu.kanade.tachiyomi.extension.en.animepahe",
                        name = "AnimePahe",
                    ),
                    fakeAvailableExtension(
                        pkg = "eu.kanade.tachiyomi.extension.jp.animeflv",
                        name = "AnimeFLV",
                        isNsfw = true,
                    ),
                )
            ),
        )
    )
}

private fun fakeInstalledExtension(
    pkg: String,
    name: String,
    version: String,
    lang: String = "EN",
    repo: String = "official",
    hasUpdate: Boolean = false,
    isNsfw: Boolean = false,
    isObsolete: Boolean = false,
    installStep: TvInstallStep = TvInstallStep.Idle,
): TvAnimeExtension =
    TvAnimeExtension(
        pkgName = pkg,
        name = name,
        type = TvExtensionType.Installed,
        langLabel = lang,
        versionName = version,
        repoName = repo,
        hasUpdate = hasUpdate,
        isNsfw = isNsfw,
        isObsolete = isObsolete,
        installStep = installStep,
    )

private fun fakeAvailableExtension(
    pkg: String,
    name: String,
    lang: String = "EN",
    repo: String = "community",
    isNsfw: Boolean = false,
): TvAnimeExtension =
    TvAnimeExtension(
        pkgName = pkg,
        name = name,
        type = TvExtensionType.Available,
        langLabel = lang,
        versionName = "",
        repoName = repo,
        isNsfw = isNsfw,
        installStep = TvInstallStep.Idle,
    )
