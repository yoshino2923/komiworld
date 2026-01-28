package eu.kanade.tv.presentation.screens.browse.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.google.common.collect.ImmutableSet
import kotlin.text.isNotEmpty

@Composable
fun TvExtensionReposCreateDialog(
    onDismissRequest: () -> Unit,
    onCreate: (String) -> Unit,
    repoUrls: Set<String>,
) {

    var name by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val nameAlreadyExists = remember(name) { repoUrls.contains(name) }


    AlertDialog(
        onDismissRequest = {},
        containerColor = MaterialTheme.colorScheme.surface,
        confirmButton = {
            Button(
                enabled = name.isNotEmpty() && !nameAlreadyExists,
                onClick = {
                    onCreate(name)
                    onDismissRequest()
                },
            ) {
                Text(text = "Add")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest,
            ) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(text = "Add repo")
        },
        text = {
            Column {
                Text(text = "Add additional repos to Zoroku. This should be a URL that ends with index.min.json")

                OutlinedTextField(
                    modifier = Modifier
                        .focusRequester(focusRequester),
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text(text = "Repo URL")
                    },
                    supportingText = {
                        Text(text = "Required")
                    },
                    isError = name.isNotEmpty() && nameAlreadyExists,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    colors = OutlinedTextFieldDefaults.colors(
                        // testo inserito
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,

                        // cursore
                        cursorColor = MaterialTheme.colorScheme.primary,

                        // label
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,

                        // bordo
                        focusedBorderColor = MaterialTheme.colorScheme.primary,

                        // se il background è trasparente e hai uno sfondo scuro/chiaro strano:
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    )
                )
            }
        },
    )
}

@Composable
fun TvExtensionRepoDeleteDialog(
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
    repo: String,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surface,
        confirmButton = {
            Button(
                onClick = {
                    onDelete()
                    onDismissRequest()
                },
            ) {
                Text(text = "Delete")
                }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(text = "Delete repo")
        },
        text = {
            Text(text = "Do you wish to delete the repo $repo?")
        },

    )
}
