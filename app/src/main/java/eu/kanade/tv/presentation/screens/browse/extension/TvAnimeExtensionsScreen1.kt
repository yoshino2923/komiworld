package eu.kanade.tv.presentation.screens.browse.extension

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.*


@Composable
fun TvAnimeExtensionsScreen1(
    state: TvAnimeExtensionsState,
    modifier: Modifier = Modifier,
    onClickUpdateAll: () -> Unit,
    onRefresh: () -> Unit,

    onClickItem: (TvAnimeExtension) -> Unit,
    onLongClickItem: (TvAnimeExtension) -> Unit,
    onClickCancel: (TvAnimeExtension) -> Unit,

    onClickPrimaryAction: (TvAnimeExtension) -> Unit,   // install/update/open settings
    onClickSecondaryAction: (TvAnimeExtension) -> Unit, // open settings (installed)
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp, vertical = 28.dp)
            .focusGroup(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Anime Extensions",
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onClickUpdateAll,
                enabled = state.updateAllEnabled,
            ) { Text("Update all") }

            Button(
                onClick = onRefresh,
                enabled = !state.isRefreshing && !state.isLoading,
            ) { Text(if (state.isRefreshing) "Refreshing…" else "Refresh") }
        }

        // Content
        when {
            state.isLoading -> CenteredLabel("Loading…")
            state.groups.isEmpty() -> CenteredLabel("No extensions")
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().focusGroup(),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    state.groups.forEach { group ->
                        item(key = "header-${group.header}") {
                            TvExtensionHeader(
                                text = group.header,
                                // come nel tuo Android: solo l'header "Updates pending" ha azione
                                showUpdateAll = group.isUpdatesHeader,
                                onClickUpdateAll = onClickUpdateAll,
                            )
                        }

                        items(
                            items = group.items,
                            key = { it.pkgName },
                        ) { item ->
                            TvExtensionItemRow(
                                item = item,
                                onClick = { onClickItem(item) },
                                onLongClick = { onLongClickItem(item) },
                                onClickCancel = { onClickCancel(item) },
                                onClickPrimaryAction = { onClickPrimaryAction(item) },
                                onClickSecondaryAction = { onClickSecondaryAction(item) },
                            )
                        }
                    }
                }
            }
        }
    }
}

/* ----------------------------- Header ----------------------------- */

@Composable
private fun TvExtensionHeader(
    text: String,
    showUpdateAll: Boolean,
    onClickUpdateAll: () -> Unit,
) {
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

/* ----------------------------- Item row ----------------------------- */

@Composable
private fun TvExtensionItemRow(
    item: TvAnimeExtension,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onClickCancel: () -> Unit,
    onClickPrimaryAction: () -> Unit,
    onClickSecondaryAction: () -> Unit,
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
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                // “chips” info come nel FlowRow Android (ma su TV meglio 1-2 righe)
                Text(
                    text = item.subtitleLine(),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                val stepText = item.installStep.stepLabelOrNull()
                if (stepText != null) {
                    Text(
                        text = stepText,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        },
        leadingContent = {
            // Placeholder icona (aggancia qui la tua icon UI quando vuoi)
            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center,
            ) {
                // Se vuoi: mostra spinner quando non idle
                if (!item.installStep.isCompleted()) {
                    CircularProgressIndicator(modifier = Modifier.size(28.dp))
                }
                // placeholder
                Text("EXT", style = MaterialTheme.typography.labelSmall)
            }
        },
        trailingContent = {
            TvItemActions(
                item = item,
                onClickCancel = onClickCancel,
                onClickPrimaryAction = onClickPrimaryAction,
                onClickSecondaryAction = onClickSecondaryAction,
            )
        },
    )
}

@Composable
private fun TvItemActions(
    item: TvAnimeExtension,
    onClickCancel: () -> Unit,
    onClickPrimaryAction: () -> Unit,
    onClickSecondaryAction: () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        when {
            !item.installStep.isCompleted() -> {
                OutlinedButton(onClick = onClickCancel) { Text("Cancel") }
            }
            item.installStep == TvInstallStep.Error -> {
                OutlinedButton(onClick = onClickPrimaryAction) { Text("Retry") }
            }
            else -> {
                when (item.type) {
                    TvExtensionType.Installed -> {
                        // Secondary: Settings (apri dettagli / settings)
                        OutlinedButton(onClick = onClickSecondaryAction) { Text("Settings") }

                        // Primary: Update (se hasUpdate)
                        if (item.hasUpdate) {
                            Button(onClick = onClickPrimaryAction) { Text("Update") }
                        }
                    }
                    TvExtensionType.Available -> {
                        Button(onClick = onClickPrimaryAction) { Text("Install") }
                    }
                }
            }
        }
    }
}

@Composable
private fun CenteredLabel(text: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}

/* ----------------------------- Models (TV) ----------------------------- */

data class TvAnimeExtensionsState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val updateAllEnabled: Boolean = false,
    val groups: List<TvExtensionsGroup> = emptyList(),
)

data class TvExtensionsGroup(
    val header: String,
    val isUpdatesHeader: Boolean = false,
    val items: List<TvAnimeExtension>,
)

enum class TvExtensionType { Installed, Available }

data class TvAnimeExtension(
    val pkgName: String,
    val name: String,
    val type: TvExtensionType,
    val langLabel: String = "",
    val versionName: String = "",
    val repoName: String? = null,
    val isNsfw: Boolean = false,
    val isObsolete: Boolean = false,
    val hasUpdate: Boolean = false,
    val installStep: TvInstallStep = TvInstallStep.Idle,
)

enum class TvInstallStep { Idle, Pending, Downloading, Installing, Error }

private fun TvInstallStep.isCompleted(): Boolean = this == TvInstallStep.Idle || this == TvInstallStep.Error

private fun TvInstallStep.stepLabelOrNull(): String? = when (this) {
    TvInstallStep.Idle -> null
    TvInstallStep.Pending -> "Pending…"
    TvInstallStep.Downloading -> "Downloading…"
    TvInstallStep.Installing -> "Installing…"
    TvInstallStep.Error -> "Error"
}

private fun TvAnimeExtension.subtitleLine(): String {
    val parts = buildList {
        if (langLabel.isNotBlank()) add(langLabel)
        if (versionName.isNotBlank()) add(versionName)
        add(repoName?.let { "@$it" } ?: "(?)")

        when {
            isObsolete -> add("OBSOLETE")
            isNsfw -> add("NSFW")
        }
        if (type == TvExtensionType.Installed && hasUpdate) add("UPDATE")
    }
    return parts.joinToString(" • ")
}

