package eu.kanade.tv.presentation.screens.browse.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.kanade.tachiyomi.extension.anime.model.AnimeExtension
import eu.kanade.tv.presentation.screens.browse.components.TvExtensionContent


@Composable
fun TvExtensionScreen(
    vm: TvExtensionViewModel = viewModel(),
) {
    val state by vm.state.collectAsState()

    TvExtensionContent(
        state = state,
        onClickUpdateAll = vm::updateAllExtensions,
        onClickRefresh = vm::findAvailableExtensions,
        onClick = { ext ->
            when (ext) {
                is AnimeExtension.Available -> vm.installExtension(ext)
                is AnimeExtension.Installed -> if (ext.hasUpdate) vm.updateExtension(ext)
                is AnimeExtension.Untrusted -> vm.trustExtension(ext)
            }
        },
        onLongClick = { ext ->
            when (ext) {
                is AnimeExtension.Installed -> vm.uninstallExtension(ext)
                else -> {}
            }
        },
    )
}
