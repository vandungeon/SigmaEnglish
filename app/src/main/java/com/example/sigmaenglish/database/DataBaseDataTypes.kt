package com.example.sigmaenglish.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

class DBType {
    @Entity(indices =
    [Index(value = ["english"]),
        Index(value = ["russian"])
    ])
    data class Word(
        val english: String,
        val russian: String,
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val description: String,
        val favorite: Boolean = false
    )
    @Entity(indices =
    [Index(value = ["english"]),
        Index(value = ["russian"])
    ])
    data class WordsFailed(
        val english: String,
        val russian: String,
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val description: String,
        val timesPractised: Int
    )
    @Entity
    data class Tag(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val name: String,
        val numbers: List<Int>
    )
}