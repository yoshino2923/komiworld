/*package com.yosh.tv_core.domain.items.episode.model

import com.yosh.tv_core.domain.entries.anime.model.downloadedFilter
import com.yosh.tv_core.tachiyomi.data.download.anime.AnimeDownloadManager
import com.yosh.tv_core.tachiyomi.ui.entries.anime.EpisodeList
import tachiyomi.domain.entries.anime.model.Anime
import tachiyomi.domain.entries.applyFilter
import tachiyomi.domain.items.episode.model.Episode
import tachiyomi.domain.items.episode.service.getEpisodeSort
import tachiyomi.source.local.entries.anime.isLocal

/**
 * Applies the view filters to the list of episodes obtained from the database.
 * @return an observable of the list of episodes filtered and sorted.
 */
fun List<Episode>.applyFilters(anime: Anime, downloadManager: AnimeDownloadManager): List<Episode> {
    val isLocalAnime = anime.isLocal()
    val unseenFilter = anime.unseenFilter
    val downloadedFilter = anime.downloadedFilter
    val bookmarkedFilter = anime.bookmarkedFilter
    val fillermarkedFilter = anime.fillermarkedFilter

    return asSequence().filter { episode -> applyFilter(unseenFilter) { !episode.seen } }
        .filter { episode -> applyFilter(bookmarkedFilter) { episode.bookmark } }
        .filter { episode -> applyFilter(fillermarkedFilter) { episode.fillermark } }
        .filter { episode ->
            applyFilter(downloadedFilter) {
                val downloaded = downloadManager.isEpisodeDownloaded(
                    episode.name,
                    episode.scanlator,
                    anime.title,
                    anime.source,
                )
                downloaded || isLocalAnime
            }
        }
        .sortedWith(getEpisodeSort(anime)).toList()
}

/**
 * Applies the view filters to the list of episodes obtained from the database.
 * @return an observable of the list of episodes filtered and sorted.
 */
fun List<EpisodeList.Item>.applyFilters(anime: Anime): Sequence<EpisodeList.Item> {
    val isLocalAnime = anime.isLocal()
    val unseenFilter = anime.unseenFilter
    val downloadedFilter = anime.downloadedFilter
    val bookmarkedFilter = anime.bookmarkedFilter
    val fillermarkedFilter = anime.fillermarkedFilter
    return asSequence()
        .filter { (episode) -> applyFilter(unseenFilter) { !episode.seen } }
        .filter { (episode) -> applyFilter(bookmarkedFilter) { episode.bookmark } }
        .filter { (episode) -> applyFilter(fillermarkedFilter) { episode.fillermark } }
        .filter { applyFilter(downloadedFilter) { it.isDownloaded || isLocalAnime } }
        .sortedWith { (episode1), (episode2) -> getEpisodeSort(anime).invoke(episode1, episode2) }
}
*/
