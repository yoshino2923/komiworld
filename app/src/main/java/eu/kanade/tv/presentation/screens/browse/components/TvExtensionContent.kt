package eu.kanade.tv.presentation.screens.browse.components

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import eu.kanade.tachiyomi.extension.anime.model.AnimeExtension
import eu.kanade.tv.presentation.screens.browse.extension.TvExtensionViewModel
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.i18n.stringResource

@Composable
fun TvExtensionContent(
    state: TvExtensionViewModel.State,
    onClickUpdateAll: () -> Unit,
    onClickRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (AnimeExtension) -> Unit,
    onLongClick: (AnimeExtension) -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp)
            .focusGroup(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        TvExtensionTopBar(
            onClickUpdateAll = onClickUpdateAll,
            onClickRefresh = onClickRefresh,
        )

        when {
            state.isLoading -> CircularProgressIndicator(
                modifier = Modifier.size(40.dp).align(Alignment.CenterHorizontally),
                strokeWidth = 2.dp,
            )

            state.isEmpty -> CenteredLabel("No extensions")
            else -> {
                val groups = state.items.toList()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .focusGroup(),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {

                    // TODO search bar

                    groups.forEach { (header, list) ->
                        item(key = headerKey(header)) {
                            TvExtensionHeader(
                                header = header,
                                showUpdateAll = isUpdatesHeader(header),
                                onClickUpdateAll = onClickUpdateAll,
                            )
                        }

                        items(
                            items = list,
                            key = { it.extension.pkgName },
                        ) { uiItem ->
                            TvExtensionItem(
                                item = uiItem,
                                onClick = { onClick(uiItem.extension) },
                                onLongClick = { onLongClick(uiItem.extension) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TvExtensionTopBar(
    onClickUpdateAll: () -> Unit,
    onClickRefresh: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Extensions",
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onClickUpdateAll,
            enabled = true,
        ) { Text("Update all") }

        IconButton(onClick = onClickRefresh) {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = stringResource(resource = MR.strings.action_webview_refresh),
            )
        }
    }
}

@Composable
private fun CenteredLabel(text: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}
