package com.example.sigmaenglish

import androidx.lifecycle.LiveData

class Repository (private val dataAccessObjects: DataAccessObjects){

    val readAllData: LiveData<List<DBType.Word>> = dataAccessObjects.readAllData()

    suspend fun insertWord(word: DBType.Word){
        dataAccessObjects.insertWord(word)
    }

    suspend fun deleteWord(word: DBType.Word) {
        dataAccessObjects.deleteWord(word)
    }
}