package com.example.sigmaenglish.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.sigmaenglish.database.DBType
import com.example.sigmaenglish.database.WordDatabase
import com.example.sigmaenglish.main.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
   private val preferencesManager: PreferencesManager,
    application: Application
) : AndroidViewModel(application) {


    val highestScore: LiveData<Int> = preferencesManager.highestStreakFlow.asLiveData()
    private val repository: Repository
    private val wordDAO = WordDatabase.getDatabase(application).dao()

    // LiveData to observe changes in the list of words
    private val _words = MediatorLiveData<List<DBType.Word>>()
    val words: LiveData<List<DBType.Word>> = _words
    private val _wordsFailed = MediatorLiveData<List<DBType.WordsFailed>>()
    val wordsFailed: LiveData<List<DBType.WordsFailed>> = _wordsFailed

    private val context: Context get() = getApplication<Application>().applicationContext

    private val _isInitialized = mutableStateOf(false)
    val isInitialized: Boolean get() = _isInitialized.value

    init {
        repository = Repository(wordDAO)
        _words.addSource(repository.readAllData) { words ->
            _words.value = words
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Words updated: $words")
            checkInitialization()
        }
        _wordsFailed.addSource(repository.readAllDataFailed) { wordsFailed ->
            _wordsFailed.value = wordsFailed
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Words updated: $wordsFailed")
            checkInitialization()
        }
    }

    private fun checkInitialization() {
        if (_words.value != null) {
            _isInitialized.value = true
        }
    }

    fun addWord(word: DBType.Word) {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Adding word: ${word.english}")
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertWord(word)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Word added successfully")
        }
        refreshData()
    }

    fun deleteWord(word: DBType.Word) {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Deleting word: ${word.english}")
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteWord(word)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Word deleted successfully")
        }
        refreshData()
    }
    fun updateWord(word: DBType.Word) {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Updating word: ${word.english}")
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWord(word)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Word updated successfully")
        }
        refreshData()
    }

    fun updateWordFailed(word: DBType.WordsFailed) {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Updating word: ${word.english}")
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWordFailed(word)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Word updated successfully")
        }
        refreshData()
    }

    fun addWordFailed(word: DBType.WordsFailed) {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Adding word: ${word.english}")
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertWordFailed(word)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Failed word added successfully")
        }
        refreshData()
    }

    fun incrementTraining(word: String) {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Incrementing training times")
        viewModelScope.launch(Dispatchers.IO) {
            repository.incrementTraining(word)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Incremented successfully")
        }

    }
    fun decrementTraining(word: String) {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Decrementing training times")
        viewModelScope.launch(Dispatchers.IO) {
            repository.decrementTraining(word)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Decremented successfully")
        }
    }
    suspend fun isWordInFailedDatabase(englishWord: String): Boolean {
        return withContext(Dispatchers.IO) {
            repository.isWordInFailedDatabase(englishWord)
        }
    }
    suspend fun getWordIdIfExists(word: String) : Int?
    {
        return repository.getWordIdIfExists(word)
    }
    fun checkForDeletion() {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Checking for deletion")
        viewModelScope.launch(Dispatchers.IO) {
            repository.checkForDeletion()
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Checked for deletion successfully")
        }
        refreshData()
    }
    fun getWordsExportString () : String {
        val bufferList: List<DBType.Word>? = words.value
        var exportString = ""
        if(!bufferList.isNullOrEmpty()) {
            bufferList.forEach { testWord ->
                exportString += testWord.english + " - " + testWord.russian + " (" + testWord.description+  ")\n"
            }
        }
        return exportString
    }
    fun checkForUpdatesHS(newStreak: Int) {
        viewModelScope.launch {
            preferencesManager.checkForUpdateHS(getApplication(), newStreak)
        }
    }
    fun deleteMistakenWord(englishWord: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMistakenWord(englishWord)
            Log.d("Viewmodel", "Deletion of mistaken word issued")
        }
        refreshData()
    }
    fun deleteAllMistakenWords(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMistakenWords()
        }
        refreshData()
    }
    private fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedWords = repository.readAllData
            val updatedWordsFailed = repository.readAllDataFailed

            withContext(Dispatchers.Main) {
                _words.value = updatedWords.value
                _wordsFailed.value = updatedWordsFailed.value
                Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Data refreshed")
            }
        }
    }
    fun isTablet(): Boolean {
        val metrics = context.resources.displayMetrics
        val widthDp = metrics.widthPixels / metrics.density
        return widthDp >= 600
    }

}
