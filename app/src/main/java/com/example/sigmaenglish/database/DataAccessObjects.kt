package com.example.sigmaenglish.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface DataAccessObjects {
    @Upsert
    suspend fun insertWord(word: DBType.Word)
    @Delete
    suspend fun deleteWord(word: DBType.Word)
    @Update
    suspend fun updateWord(word: DBType.Word)
    @Upsert
    suspend fun insertWordFailed(word: DBType.WordsFailed)
    @Delete
    suspend fun deleteWordFailed(word: DBType.WordsFailed)
    @Update
    suspend fun updateWordFailed(word: DBType.WordsFailed)

    @Query(" SELECT * FROM Word")
    fun readAllData(): LiveData<List<DBType.Word>>

    @Query(" SELECT * FROM WordsFailed")
    fun readAllDataFailed(): LiveData<List<DBType.WordsFailed>>

    @Query(" Delete FROM WordsFailed WHERE timesPractised>=5")
    fun checkForCompletedFails()

    @Query(" Delete FROM WordsFailed WHERE english = :englishWord")
    fun deleteMistakenWord(englishWord: String)

    @Query("DELETE FROM WordsFailed")
    fun deleteAllMistakenWords()

    @Query("UPDATE WordsFailed SET timesPractised = timesPractised + 1 WHERE english = :englishWord")
    suspend fun incrementTimesPractised(englishWord: String)

    @Query("UPDATE WordsFailed SET timesPractised = 0 WHERE english = :englishWord")
    suspend fun decrementTimesPractised(englishWord: String)

    @Query("SELECT EXISTS(SELECT 1 FROM WordsFailed WHERE english = :englishWord)")
    suspend fun isWordInFailedDatabase(englishWord: String): Boolean
}