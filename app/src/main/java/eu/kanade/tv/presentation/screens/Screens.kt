package eu.kanade.tv.presentation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.VideoLibrary


enum class Screens (
    private val args: List<String>? = null,
    val isTabItem: Boolean = false,
    val tabIcon : ImageVector? = null,
){
    Library(isTabItem = true),
    Browse(isTabItem = true),
    Updates(isTabItem = true),
    History(isTabItem = true),
    Dashboard,
    Calendar(isTabItem = true, tabIcon = Icons.Default.CalendarMonth),
    Settings(isTabItem = true, tabIcon = Icons.Default.Settings),;


    operator fun invoke(): String {
        val argList = StringBuilder()
        args?.let { nnArgs ->
            nnArgs.forEach { arg -> argList.append("/{$arg}") }
        }
        return name + argList
    }

    fun withArgs(vararg args: Any): String {
        val destination = StringBuilder()
        args.forEach { arg -> destination.append("/$arg") }
        return name + destination
    }

}
