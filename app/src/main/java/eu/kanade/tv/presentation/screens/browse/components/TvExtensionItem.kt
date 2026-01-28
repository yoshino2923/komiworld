package eu.kanade.tv.presentation.screens.browse.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.DenseListItem
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import eu.kanade.presentation.browse.anime.components.AnimeExtensionIcon
import eu.kanade.tachiyomi.ui.browse.anime.extension.AnimeExtensionUiModel

@Composable
fun TvExtensionItem(
    item: AnimeExtensionUiModel.Item,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {

    DenseListItem(
        selected = false,
        onClick = onClick,
        onLongClick = onLongClick,
        headlineContent = {
            Text(
                text = item.extension.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {/*TODO*/},
        leadingContent = {
            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center,
            ){
                val idle = item.installStep.isCompleted()
                if (!idle) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        strokeWidth = 2.dp,
                    )
                }

                AnimeExtensionIcon(
                    extension = item.extension,
                    modifier = Modifier
                        .matchParentSize()
                )
            }
        }
    )
}
