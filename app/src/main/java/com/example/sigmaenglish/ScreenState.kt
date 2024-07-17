package com.example.sigmaenglish

data class ScreenState(
    val words: List<DBType.Word> = emptyList(),
    val wordsFailed: List<DBType.WordsFailed> = emptyList()
)

