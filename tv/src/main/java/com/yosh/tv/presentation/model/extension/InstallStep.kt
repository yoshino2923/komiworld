package com.yosh.tv.presentation.model.extension

enum class InstallStep {
    Idle,
    Pending,
    Downloading,
    Installing,
    Installed,
    Error,
    ;

    fun isCompleted(): Boolean {
        return this == Installed || this == Error || this == Idle
    }
}
