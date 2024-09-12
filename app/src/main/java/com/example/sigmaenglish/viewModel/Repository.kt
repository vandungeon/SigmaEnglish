package com.example.sigmaenglish.viewModel

import androidx.lifecycle.LiveData
import com.example.sigmaenglish.Database.DBType
import com.example.sigmaenglish.Database.DataAccessObjects

class Repository (private val dataAccessObjects: DataAccessObjects){

    val readAllData: LiveData<List<DBType.Word>> = dataAccessObjects.readAllData()

    suspend fun insertWord(word: DBType.Word){
        dataAccessObjects.insertWord(word)
    }

    suspend fun deleteWord(word: DBType.Word) {
        dataAccessObjects.deleteWord(word)
    }

    suspend fun updateWord(word: DBType.Word) {
        dataAccessObjects.updateWord(word)
    }


    val readAllDataFailed: LiveData<List<DBType.WordsFailed>> = dataAccessObjects.readAllDataFailed()

    suspend fun insertWordFailed(word: DBType.WordsFailed){
        dataAccessObjects.insertWordFailed(word)
    }

    suspend fun deleteWordFailed(word: DBType.WordsFailed) {
        dataAccessObjects.deleteWordFailed(word)
    }

    suspend fun decrementTraining(word: String) {
        dataAccessObjects.decrementTimesPractised(word)
    }
    suspend fun incrementTraining(word: String) {
        dataAccessObjects.incrementTimesPractised(word)
    }

    suspend fun updateWordFailed(word: DBType.WordsFailed) {
        dataAccessObjects.updateWordFailed(word)
    }
    suspend fun isWordInFailedDatabase(word: String): Boolean {
        return dataAccessObjects.isWordInFailedDatabase(word)
    }

    fun checkForDeletion() {
        dataAccessObjects.checkForCompletedFails()
    }

    fun deleteMistakenWord(englishWord: String) {
        dataAccessObjects.deleteMistakenWord(englishWord = englishWord)
    }

    fun deleteAllMistakenWords(){
        dataAccessObjects.deleteAllMistakenWords()
    }
}