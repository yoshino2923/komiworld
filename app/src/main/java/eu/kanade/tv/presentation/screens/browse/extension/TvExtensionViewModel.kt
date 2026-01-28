package eu.kanade.tv.presentation.screens.browse.extension

import android.app.Application
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.kanade.domain.base.BasePreferences
import eu.kanade.domain.extension.anime.interactor.GetAnimeExtensionLanguages
import eu.kanade.domain.extension.anime.interactor.GetAnimeExtensionsByType
import eu.kanade.domain.source.interactor.ToggleLanguage
import eu.kanade.domain.source.service.SourcePreferences
import eu.kanade.presentation.components.SEARCH_DEBOUNCE_MILLIS
import eu.kanade.tachiyomi.extension.InstallStep
import eu.kanade.tachiyomi.extension.anime.AnimeExtensionManager
import eu.kanade.tachiyomi.extension.anime.model.AnimeExtension
import eu.kanade.tachiyomi.source.online.HttpSource
import eu.kanade.tachiyomi.ui.browse.anime.extension.AnimeExtensionUiModel
import eu.kanade.tachiyomi.ui.browse.anime.extension.ItemGroups
import eu.kanade.tachiyomi.util.system.LocaleHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tachiyomi.core.common.util.lang.launchIO
import tachiyomi.i18n.MR
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import kotlin.time.Duration.Companion.seconds

class TvExtensionViewModel(
    preferences: SourcePreferences = Injekt.get(),
    basePreferences: BasePreferences = Injekt.get(),
    private val extensionManager: AnimeExtensionManager = Injekt.get(),
    private val getExtensions: GetAnimeExtensionsByType = Injekt.get(),

    //dependencies to get all the languages
    private val getExtensionLanguages: GetAnimeExtensionLanguages = Injekt.get(),
    private val toggleLanguage: ToggleLanguage = Injekt.get(),
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val currentDownloads = MutableStateFlow<Map<String, InstallStep>>(hashMapOf())


    init {
        val context = Injekt.get<Application>()

        // ✅ Fix: se l’utente ha abilitato SOLO alcune lingue (es "en"),
        // abilitiamo TUTTE le lingue disponibili così vedrai tutto su TV.
        // Se enabledLanguages è vuoto, lo lasciamo stare (di solito = nessun filtro).
        viewModelScope.launchIO {
            runCatching {
                val enabled = preferences.enabledLanguages().get()
                if (enabled.isNotEmpty()) {
                    val allLangs = getExtensionLanguages.subscribe().first()
                    allLangs.forEach { lang ->
                        if (!enabled.contains(lang)) {
                            toggleLanguage.await(lang)
                        }
                    }
                }
            }
        }

        val extensionMapper: (Map<String, InstallStep>) -> (AnimeExtension) -> AnimeExtensionUiModel.Item = { map ->
            { ext -> AnimeExtensionUiModel.Item(ext, map[ext.pkgName] ?: InstallStep.Idle) }
        }

        val queryFilter: (String) -> (AnimeExtension) -> Boolean = { query ->
            filter@{ extension ->
                if (query.isBlank()) return@filter true
                query.split(",").any { raw ->
                    val input = raw.trim()
                    if (input.isEmpty()) return@any false

                    when (extension) {
                        is AnimeExtension.Available -> {
                            extension.sources.any {
                                it.name.contains(input, ignoreCase = true) ||
                                    it.baseUrl.contains(input, ignoreCase = true) ||
                                    it.id == input.toLongOrNull()
                            } || extension.name.contains(input, ignoreCase = true)
                        }

                        is AnimeExtension.Installed -> {
                            extension.sources.any {
                                it.name.contains(input, ignoreCase = true) ||
                                    it.id == input.toLongOrNull() ||
                                    if (it is HttpSource) it.baseUrl.contains(input, ignoreCase = true) else false
                            } || extension.name.contains(input, ignoreCase = true)
                        }

                        is AnimeExtension.Untrusted -> extension.name.contains(input, ignoreCase = true)
                    }
                }
            }
        }

        viewModelScope.launchIO {
            combine(
                state.map { it.searchQuery }.distinctUntilChanged().debounce(SEARCH_DEBOUNCE_MILLIS),
                currentDownloads,
                getExtensions.subscribe(),
            ) { query, downloads, (_updates, _installed, _available, _untrusted) ->
                val searchQuery = query ?: ""

                val itemsGroups: ItemGroups = mutableMapOf()

                val updates = _updates.filter(queryFilter(searchQuery)).map(
                    extensionMapper(downloads),
                )
                if (updates.isNotEmpty()) {
                    itemsGroups[AnimeExtensionUiModel.Header.Resource(MR.strings.ext_updates_pending)] = updates
                }

                val installed = _installed.filter(queryFilter(searchQuery)).map(
                    extensionMapper(downloads),
                )
                val untrusted = _untrusted.filter(queryFilter(searchQuery)).map(
                    extensionMapper(downloads),
                )
                if (installed.isNotEmpty() || untrusted.isNotEmpty()) {
                    itemsGroups[AnimeExtensionUiModel.Header.Resource(MR.strings.ext_installed)] = installed + untrusted
                }

                val languagesWithExtensions = _available
                    .filter(queryFilter(searchQuery))
                    .groupBy { it.lang }
                    .toSortedMap(LocaleHelper.comparator)
                    .map { (lang, exts) ->
                        AnimeExtensionUiModel.Header.Text(
                            LocaleHelper.getSourceDisplayName(lang, context),
                        ) to exts.map(extensionMapper(downloads))
                    }

                if (languagesWithExtensions.isNotEmpty()) {
                    itemsGroups.putAll(languagesWithExtensions)
                }

                itemsGroups
            }
                .collectLatest {
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            items = it,
                        )
                    }
                }
        }
        viewModelScope.launchIO { findAvailableExtensions() }

        preferences.animeExtensionUpdatesCount().changes()
            .onEach { _state.update { state -> state.copy(updates = it) } }
            .launchIn(viewModelScope)

        basePreferences.extensionInstaller().changes()
            .onEach { _state.update { state -> state.copy(installer = it) } }
            .launchIn(viewModelScope)

    }

    fun search(query: String?) {
        _state.update {
            it.copy(searchQuery = query)
        }
    }

    fun updateAllExtensions() {
        viewModelScope.launchIO {
            state.value.items.values.flatten()
                .map { it.extension }
                .filterIsInstance<AnimeExtension.Installed>()
                .filter { it.hasUpdate }
                .forEach(::updateExtension)
        }
    }

    fun installExtension(extension: AnimeExtension.Available) {
        viewModelScope.launchIO {
            extensionManager.installExtension(extension).collectToInstallUpdate(extension)
        }
    }

    fun updateExtension(extension: AnimeExtension.Installed) {
        viewModelScope.launchIO {
            extensionManager.updateExtension(extension).collectToInstallUpdate(extension)
        }
    }

    fun cancelInstallUpdateExtension(extension: AnimeExtension) {
        extensionManager.cancelInstallUpdateExtension(extension)
    }

    private fun addDownloadState(extension: AnimeExtension, installStep: InstallStep) {
        currentDownloads.update { it + Pair(extension.pkgName, installStep) }
    }

    private fun removeDownloadState(extension: AnimeExtension) {
        currentDownloads.update { it - extension.pkgName }
    }

    private suspend fun Flow<InstallStep>.collectToInstallUpdate(extension: AnimeExtension) =
        this
            .onEach { installStep -> addDownloadState(extension, installStep) }
            .onCompletion { removeDownloadState(extension) }
            .collect()

    fun uninstallExtension(extension: AnimeExtension) {
        extensionManager.uninstallExtension(extension)
    }

    fun findAvailableExtensions() {
        viewModelScope.launchIO {
            _state.update { it.copy(isRefreshing = true) }
            extensionManager.findAvailableExtensions()

            // Fake slower refresh so it doesn't seem like it's not doing anything
            delay(1.seconds)

            _state.update { it.copy(isRefreshing = false) }
        }
    }

    fun trustExtension(extension: AnimeExtension.Untrusted) {
        viewModelScope.launch {
            extensionManager.trust(extension)
        }
    }


    @Immutable
    data class State(
        val isLoading: Boolean = true,
        val isRefreshing: Boolean = false,
        val items: ItemGroups = mutableMapOf(),
        val updates: Int = 0,
        val installer: BasePreferences.ExtensionInstaller? = null,
        val searchQuery: String? = null,
    ) {
        val isEmpty = items.isEmpty()
    }

}

