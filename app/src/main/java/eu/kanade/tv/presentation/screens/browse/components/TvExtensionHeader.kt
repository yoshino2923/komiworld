package eu.kanade.tv.presentation.screens.browse.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Text
import eu.kanade.tachiyomi.ui.browse.anime.extension.AnimeExtensionUiModel
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.i18n.stringResource

@Composable
fun TvExtensionHeader(
    header: AnimeExtensionUiModel.Header,
    showUpdateAll: Boolean,
    onClickUpdateAll: () -> Unit,
) {
    val text = when (header) {
        is AnimeExtensionUiModel.Header.Text -> header.text
        is AnimeExtensionUiModel.Header.Resource -> stringResource(header.textRes)
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 3.dp,
        colors = SurfaceDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.weight(1f))

            if (showUpdateAll) {
                Button(onClick = onClickUpdateAll) { Text("Update all") }
            }
        }
    }
}

fun headerKey(header: AnimeExtensionUiModel.Header): String = when (header) {
    is AnimeExtensionUiModel.Header.Text -> "h-text-${header.text}"
    is AnimeExtensionUiModel.Header.Resource -> "h-res-${header.textRes.hashCode()}"
}

fun isUpdatesHeader(header: AnimeExtensionUiModel.Header): Boolean =
    header is AnimeExtensionUiModel.Header.Resource && header.textRes == MR.strings.ext_updates_pending
