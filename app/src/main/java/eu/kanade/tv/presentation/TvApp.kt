package eu.kanade.tv.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.kanade.tv.presentation.screens.Screens
import eu.kanade.tv.presentation.screens.dashboard.DashboardScreen


@Composable
fun TvApp (
    onBackPressed:() -> Unit
){

    val navController = rememberNavController()
    var isComingBackFromDifferentScreen by remember { mutableStateOf(false) }


    NavHost(
        navController = navController,
        startDestination = Screens.Dashboard(),
        builder = {
            composable(route = Screens.Dashboard()) {
                DashboardScreen(
                    onBackPressed = onBackPressed,
                    isComingBackFromDifferentScreen = isComingBackFromDifferentScreen,
                    resetIsComingBackFromDifferentScreen = {
                        isComingBackFromDifferentScreen = false
                    }
                )
            }
        }
    )


}
