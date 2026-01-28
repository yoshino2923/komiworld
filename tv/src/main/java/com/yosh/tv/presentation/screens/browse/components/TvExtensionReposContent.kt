package com.yosh.tv.presentation.screens.browse.components

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.Glow
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.ListItem
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.yosh.tv.presentation.screens.browse.repos.RepoScreenState
import mihon.domain.extensionrepo.model.ExtensionRepo
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.i18n.stringResource


@Composable
fun TvExtensionReposContent(
    state: RepoScreenState.Success,
    onClickCreate: () -> Unit,
    onClickDelete: (String) -> Unit,
    onClickRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {

    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp)
            .focusGroup(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        TvExtensionReposTopBar(
            onClickCreate = onClickCreate,
            onClickRefresh = onClickRefresh,
        )

        LazyColumn(
            state = lazyListState,
            modifier = modifier,
        ) {
            state.repos.forEach {
                item {
                    TvExtensionReposListItem(
                        repo = it,
                        onDelete = { onClickDelete(it.baseUrl)},
                    )
                }
            }
        }

    }
}


@Composable
fun TvExtensionReposTopBar(
    onClickCreate: () -> Unit,
    onClickRefresh: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Extension Repos",
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onClickCreate,
            enabled = true,
        ) { Text("Add Repo") }

        IconButton(onClick = onClickRefresh) {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = stringResource(resource = MR.strings.action_webview_refresh),
            )
        }
    }
}


@Composable
fun TvExtensionReposListItem(
    repo: ExtensionRepo,
    onDelete: () -> Unit,
) {
    val glow = Glow(
        elevation = 5.dp,
        elevationColor = MaterialTheme.colorScheme.primary,
    )

    ListItem(
        selected = false,
        onClick = onDelete,
        headlineContent = {
            Text(
                text = repo.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingContent = {
            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text("EXT", style = MaterialTheme.typography.labelSmall)
            }
        },

    )
}
