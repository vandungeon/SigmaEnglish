package com.example.sigmaenglish.Database

import androidx.compose.runtime.MutableState
import androidx.room.Entity
import androidx.room.PrimaryKey

class DBType {
    @Entity
    data class Word(
        val english: String,
        val russian: String,
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val description: String
    )
    @Entity
    data class WordsFailed(
        val english: String,
        val russian: String,
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val description: String,
        val timesPractised: Int
    )
}