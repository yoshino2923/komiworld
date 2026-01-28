package com.yosh.tv.presentation.screens.browse.repos

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.resources.StringResource
import kotlinx.collections.immutable.ImmutableSet
import mihon.domain.extensionrepo.anime.interactor.CreateAnimeExtensionRepo
import mihon.domain.extensionrepo.anime.interactor.DeleteAnimeExtensionRepo
import mihon.domain.extensionrepo.anime.interactor.GetAnimeExtensionRepo
import mihon.domain.extensionrepo.anime.interactor.ReplaceAnimeExtensionRepo
import mihon.domain.extensionrepo.anime.interactor.UpdateAnimeExtensionRepo
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import mihon.domain.extensionrepo.model.ExtensionRepo
import tachiyomi.core.common.util.lang.launchIO
import tachiyomi.i18n.MR
import kotlinx.collections.immutable.toImmutableSet


class TvExtensionReposViewModel(
    private val getExtensionRepo: GetAnimeExtensionRepo = Injekt.get(),
    private val createExtensionRepo: CreateAnimeExtensionRepo = Injekt.get(),
    private val deleteExtensionRepo: DeleteAnimeExtensionRepo = Injekt.get(),
    private val replaceExtensionRepo: ReplaceAnimeExtensionRepo = Injekt.get(),
    private val updateExtensionRepo: UpdateAnimeExtensionRepo = Injekt.get(),
) : ViewModel() {

    private val _state = MutableStateFlow<RepoScreenState>(RepoScreenState.Loading)
    val state = _state.asStateFlow()

    private val _events: Channel<RepoEvent> = Channel(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launchIO {
            getExtensionRepo.subscribeAll()
                .collectLatest { repos ->
                    _state.value = RepoScreenState.Success(
                        repos = repos.toImmutableSet(),
                    )
                }
        }
    }

    fun createRepo(baseUrl: String) {
        viewModelScope.launchIO {
            when (val result = createExtensionRepo.await(baseUrl)) {
                CreateAnimeExtensionRepo.Result.InvalidUrl ->
                    _events.send(RepoEvent.InvalidUrl)

                CreateAnimeExtensionRepo.Result.RepoAlreadyExists ->
                    _events.send(RepoEvent.RepoAlreadyExists)

                is CreateAnimeExtensionRepo.Result.DuplicateFingerprint ->
                    showDialog(RepoDialog.Conflict(result.oldRepo, result.newRepo))

                else -> Unit
            }
        }
    }

    fun replaceRepo(newRepo: ExtensionRepo) {
        viewModelScope.launchIO {
            replaceExtensionRepo.await(newRepo)
        }
    }

    fun refreshRepos() {
        if (state.value is RepoScreenState.Success) {
            viewModelScope.launchIO {
                updateExtensionRepo.awaitAll()
            }
        }
    }

    fun deleteRepo(baseUrl: String) {
        viewModelScope.launchIO {
            deleteExtensionRepo.await(baseUrl)
        }
    }

    fun showDialog(dialog: RepoDialog) {
        _state.update {
            when (it) {
                RepoScreenState.Loading -> it
                is RepoScreenState.Success -> it.copy(dialog = dialog)
            }
        }
    }

    fun dismissDialog() {
        _state.update {
            when (it) {
                RepoScreenState.Loading -> it
                is RepoScreenState.Success -> it.copy(dialog = null)
            }
        }
    }
}


sealed class RepoEvent {
    sealed class LocalizedMessage(val stringRes: StringResource) : RepoEvent()
    data object InvalidUrl : LocalizedMessage(MR.strings.invalid_repo_name)
    data object RepoAlreadyExists : LocalizedMessage(MR.strings.error_repo_exists)
}

sealed class RepoDialog {
    data object Create : RepoDialog()
    data class Delete(val repo: String) : RepoDialog()
    data class Conflict(val oldRepo: ExtensionRepo, val newRepo: ExtensionRepo) : RepoDialog()
    data class Confirm(val url: String) : RepoDialog()
}

sealed class RepoScreenState {

    @Immutable
    data object Loading : RepoScreenState()

    @Immutable
    data class Success(
        val repos: ImmutableSet<ExtensionRepo>,
        val oldRepos: ImmutableSet<String>? = null,
        val dialog: RepoDialog? = null,
    ) : RepoScreenState() {

        val isEmpty: Boolean
            get() = repos.isEmpty()
    }
}
