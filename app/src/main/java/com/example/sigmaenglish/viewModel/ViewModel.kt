package com.example.sigmaenglish.viewModel

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.sigmaenglish.Database.DBType
import com.example.sigmaenglish.Database.WordDatabase
import com.example.sigmaenglish.main.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//class ViewModel(application: Application, val preferencesManager: PreferencesManager) : AndroidViewModel(application) {
@HiltViewModel
class ViewModel @Inject constructor(
   private val preferencesManager: PreferencesManager,
    application: Application
) : AndroidViewModel(application) {


    val manager = preferencesManager

    val highestScore: LiveData<Int> = preferencesManager.highestStreakFlow.asLiveData()
    private val repository: Repository
    private val wordDAO = WordDatabase.getDatabase(application).dao()

    // LiveData to observe changes in the list of words
    private val _words = MediatorLiveData<List<DBType.Word>>()
    val words: LiveData<List<DBType.Word>> = _words
    private val _wordsFailed = MediatorLiveData<List<DBType.WordsFailed>>()
    val wordsFailed: LiveData<List<DBType.WordsFailed>> = _wordsFailed

    // MutableState for managing UI text input
    private val _state = mutableStateOf(ScreenState())
    val state: ScreenState get() = _state.value

    private val _isInitialized = mutableStateOf(false)
    val isInitialized: Boolean get() = _isInitialized.value

    init {
        repository = Repository(wordDAO)
        _words.addSource(repository.readAllData) { words ->
            _words.value = words
            _state.value = _state.value.copy(words = words)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Words updated: $words")
            checkInitialization()
        }
        _wordsFailed.addSource(repository.readAllDataFailed) { wordsFailed ->
            _wordsFailed.value = wordsFailed
            _state.value = _state.value.copy(wordsFailed = wordsFailed)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Words updated: $wordsFailed")
            checkInitialization()
        }
    }

    private fun checkInitialization() {
        if (_words.value != null && _wordsFailed.value != null) {
            _isInitialized.value = true
        }
    }

    fun addWord(word: DBType.Word) {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Adding word: ${word.english}")
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertWord(word)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Word added successfully")
        }
    }

    fun deleteWord(word: DBType.Word) {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Deleting word: ${word.english}")
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteWord(word)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Word deleted successfully")
        }
    }
    fun updateWord(word: DBType.Word) {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Updating word: ${word.english}")
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWord(word)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Word updated successfully")
        }
    }

    fun updateWordFailed(word: DBType.WordsFailed) {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Updating word: ${word.english}")
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWordFailed(word)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Word updated successfully")
        }
    }

    fun addWordFailed(word: DBType.WordsFailed) {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Adding word: ${word.english}")
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertWordFailed(word)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Failed word added successfully")
        }
    }

    fun incrementTraining(word: String) {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Checking for deletion")
        viewModelScope.launch(Dispatchers.IO) {
            repository.incrementTraining(word)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Checked for deletion successfully")
        }
    }
    fun decrementTraining(word: String) {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Checking for deletion")
        viewModelScope.launch(Dispatchers.IO) {
            repository.decrementTraining(word)
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Checked for deletion successfully")
        }
    }
    suspend fun isWordInFailedDatabase(englishWord: String): Boolean {
        return withContext(Dispatchers.IO) {
            repository.isWordInFailedDatabase(englishWord)
        }
    }

    fun checkForDeletion() {
        Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Checking for deletion")
        viewModelScope.launch(Dispatchers.IO) {
            repository.checkForDeletion()
            Log.d("com.example.sigmaenglish.viewModel.ViewModel", "Checked for deletion successfully")
        }
    }
    fun checkForUpdatesHS(newStreak: Int) {
        viewModelScope.launch() {
            preferencesManager.checkForUpdateHS(getApplication(), newStreak)
        }
    }
    fun deleteMistakenWord(englishWord: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMistakenWord(englishWord)
            Log.d("Viewmodel", "Deletion of mistaken word issued")
        }
    }
    fun deleteAllMistakenWords(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMistakenWords()
        }
    }

    /*
        fun saveUserPreference(context: Context, highestStreak: Int) {
            viewModelScope.launch(Dispatchers.IO) {
                preferencesManager.updateHighestStreak(context, highestStreak)
            }
        }
    */


}
