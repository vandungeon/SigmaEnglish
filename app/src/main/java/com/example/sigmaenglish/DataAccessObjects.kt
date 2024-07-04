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
    suspend fun insertTask(task: DBType.Task)
    @Delete
    suspend fun deleteTask(task: DBType.Task)

    @Query(" SELECT * FROM Task")
    fun readAllData(): LiveData<List<DBType.Task>>

    @Query(" SELECT * FROM Task ORDER BY checkedState DESC")
    fun getTaskOrderedByCompletion(): Flow<List<DBType.Task>>
}