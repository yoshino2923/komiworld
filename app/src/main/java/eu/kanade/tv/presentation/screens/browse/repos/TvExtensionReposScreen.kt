package eu.kanade.tv.presentation.screens.browse.repos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.kanade.tv.presentation.screens.browse.components.TvExtensionRepoDeleteDialog
import eu.kanade.tv.presentation.screens.browse.components.TvExtensionReposContent
import eu.kanade.tv.presentation.screens.browse.components.TvExtensionReposCreateDialog
import eu.kanade.tachiyomi.util.system.toast
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.collectLatest
import tachiyomi.presentation.core.screens.LoadingScreen

@Composable
fun TvExtensionReposScreen(
    url: String? = null,
    vm: TvExtensionReposViewModel = viewModel(),
) {
    val context = LocalContext.current
    val state by vm.state.collectAsState()

    // Se arrivi con url, apri subito conferma
    LaunchedEffect(url) {
        url?.let { vm.showDialog(RepoDialog.Confirm(it)) }
    }

    if (state is RepoScreenState.Loading) {
        LoadingScreen()
        return
    }

    val successState = state as RepoScreenState.Success

    // ✅ Qui chiami la tua UI TV
    TvExtensionReposContent(
        state = successState,
        onClickCreate = { vm.showDialog(RepoDialog.Create) },
        onClickDelete = { vm.showDialog(RepoDialog.Delete(it)) },
        onClickRefresh = { vm.refreshRepos() },
    )

    // ✅ Dialog identici al file Voyager (stessa logica)
    when (val dialog = successState.dialog) {
        null -> Unit

        RepoDialog.Create -> {
            TvExtensionReposCreateDialog(
                onDismissRequest = vm::dismissDialog,
                onCreate = { vm.createRepo(it) },
                repoUrls = successState.repos
                    .map { it.baseUrl }
                    .toSet(),
            )
        }

        is RepoDialog.Delete -> {
            TvExtensionRepoDeleteDialog(
                onDismissRequest = vm::dismissDialog,
                onDelete = { vm.deleteRepo(dialog.repo) },
                repo = dialog.repo,
            )
        }

        else -> {}
    }

    // ✅ Toast eventi
    LaunchedEffect(Unit) {
        vm.events.collectLatest { event ->
            if (event is RepoEvent.LocalizedMessage) {
                context.toast(event.stringRes)
            }
        }
    }
}
