package com.yosh.tv.presentation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Info


enum class Screens (
    private val args: List<String>? = null,
    val isTabItem: Boolean = false,
    val tabIcon : ImageVector? = null
){
    Profile,
    Library(isTabItem = true),
    History(isTabItem = true),
    Browse(isTabItem = true),
    Updates(isTabItem = true),
    Dashboard,
    Calendar(isTabItem = true, tabIcon = Icons.Default.Info);


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
