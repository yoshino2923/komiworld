package com.yosh.tv_core.domain.download.anime.interactor

import com.yosh.tv_core.tachiyomi.data.download.anime.AnimeDownloadManager
import tachiyomi.core.common.util.lang.withNonCancellableContext
import tachiyomi.domain.entries.anime.model.Anime
import tachiyomi.domain.items.episode.model.Episode
import tachiyomi.domain.source.anime.service.AnimeSourceManager

class DeleteEpisodeDownload(
    private val sourceManager: AnimeSourceManager,
    private val downloadManager: AnimeDownloadManager,
) {

    suspend fun awaitAll(anime: Anime, vararg episodes: Episode) = withNonCancellableContext {
        sourceManager.get(anime.source)?.let { source ->
            downloadManager.deleteEpisodes(episodes.toList(), anime, source)
        }
    }
}
