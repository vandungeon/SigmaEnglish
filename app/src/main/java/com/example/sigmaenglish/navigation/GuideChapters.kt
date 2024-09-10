package com.example.sigmaenglish.navigation

import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


object GuideChapters {

    @Composable
    fun ContentA() {
        Text(text = "This is content for 2-a", style = typography.bodyMedium)
    }

    @Composable
    fun ContentB() {
        Text(text = "This is content for 2-b", style = typography.bodyMedium)
    }
}