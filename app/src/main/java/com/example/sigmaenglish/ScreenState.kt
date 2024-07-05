package com.example.sigmaenglish

data class ScreenState(
    val words: List<DBType.Word> = emptyList(),
    val textOriginal: String = "",
    val textTranslation: String = "",
    val textDescription: String = ""
)

