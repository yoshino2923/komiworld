package com.yosh.tv.presentation.screens.browse.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.DenseListItem
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.yosh.tv_core.tachiyomi.extension.anime.model.AnimeExtension

@Composable
fun TvBrowseItem(
    item: AnimeExtension,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    DenseListItem(
        selected = false,
        onClick = onClick,
        onLongClick = onLongClick,
        headlineContent = {
            Text(
                text = item.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {},
        leadingContent = {
            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center,
            ){
                Text("EXT", style = MaterialTheme.typography.labelSmall)
            }
        }
    )
}
