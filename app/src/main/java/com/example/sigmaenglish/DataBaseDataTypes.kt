package com.example.sigmaenglish

import androidx.compose.runtime.MutableState
import androidx.room.Entity
import androidx.room.PrimaryKey

class DBType {
    @Entity
    data class Task(
        val bodyTask: String,
        val checkedState: MutableState<Boolean>,
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0
    )
    @Entity
    data class Word(
        val english: String,
        val russian: String,
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val description: String
    )

}