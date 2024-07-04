package com.example.sigmaenglish

data class ScreenState(
    val tasks: List<DBType.Task> = emptyList(),
    val text: String = ""
)

