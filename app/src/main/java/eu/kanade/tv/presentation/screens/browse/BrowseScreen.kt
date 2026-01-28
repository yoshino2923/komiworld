package eu.kanade.tv.presentation.screens.browse

import androidx.compose.runtime.Composable
import eu.kanade.tv.presentation.screens.browse.extension.TvExtensionScreen
import eu.kanade.tv.presentation.screens.browse.repos.TvExtensionReposScreen


@Composable
fun BrowseScreen() {
    val actions = listOf(
        ExampleAction(
            title = "Installed Sources",
            content = {}
        ),

        ExampleAction(
            title = "Extensions",
            content = {
                TvExtensionScreen()
            }
        ),

        ExampleAction(
            title = "Repos",
            content = {
                TvExtensionReposScreen()
            }
        ),
    )

    ExamplesScreenWithDottedBackground(actions)
}

