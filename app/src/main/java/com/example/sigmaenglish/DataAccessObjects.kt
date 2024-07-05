package com.example.sigmaenglish

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DataAccessObjects {
    @Upsert
    suspend fun insertWord(task: DBType.Word)
    @Delete
    suspend fun deleteWord(task: DBType.Word)

    @Query(" SELECT * FROM Word")
    fun readAllData(): LiveData<List<DBType.Word>>
}