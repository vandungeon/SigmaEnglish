package com.example.sigmaenglish

import androidx.lifecycle.LiveData

class Repository (private val dataAccessObjects: DataAccessObjects){

    val readAllData: LiveData<List<DBType.Task>> = dataAccessObjects.readAllData()

    suspend fun addTASK(task: DBType.Task){
        dataAccessObjects.insertTask(task)
    }

    suspend fun deleteTASK(task: DBType.Task) {
        dataAccessObjects.deleteTask(task)
    }
}