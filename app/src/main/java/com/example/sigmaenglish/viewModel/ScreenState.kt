package com.example.sigmaenglish.viewModel

import com.example.sigmaenglish.Database.DBType

data class ScreenState(
    val words: List<DBType.Word> = emptyList(),
    val wordsFailed: List<DBType.WordsFailed> = emptyList()
)

