package com.example.sigmaenglish.database

import androidx.room.Entity
import androidx.room.PrimaryKey

class DBType {
    @Entity
    data class Word(
        val english: String,
        val russian: String,
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val description: String,
        val favorite: Boolean = false
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