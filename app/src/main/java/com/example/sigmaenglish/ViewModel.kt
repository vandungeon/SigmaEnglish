package com.example.sigmaenglish

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.example.sigmaenglish.DBType
import com.example.sigmaenglish.WordDatabase
import com.example.sigmaenglish.Repository
import com.example.sigmaenglish.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository
    private val wordDAO = WordDatabase.getDatabase(application).dao()

    // LiveData to observe changes in the list of words
    private val _words = MediatorLiveData<List<DBType.Word>>()
    val words: LiveData<List<DBType.Word>> = _words

    // MutableState for managing UI text input
    private val _state = mutableStateOf(ScreenState())
    val state: ScreenState get() = _state.value

    init {
        repository = Repository(wordDAO)
        _words.addSource(repository.readAllData) { words ->
            _words.value = words
            _state.value = _state.value.copy(words = words) // Update ScreenState with words
            Log.d("com.example.sigmaenglish.ViewModel", "Words updated: $words")
        }
    }

    fun addWord(word: DBType.Word) {
        Log.d("com.example.sigmaenglish.ViewModel", "Adding word: ${word.english}")
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertWord(word)
            Log.d("com.example.sigmaenglish.ViewModel", "Word added successfully")
        }
    }

    fun deleteWord(word: DBType.Word) {
        Log.d("com.example.sigmaenglish.ViewModel", "Deleting word: ${word.english}")
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteWord(word)
            Log.d("com.example.sigmaenglish.ViewModel", "Word deleted successfully")
        }
    }
    fun updateWord(word: DBType.Word) {
        Log.d("com.example.sigmaenglish.ViewModel", "Updating word: ${word.english}")
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWord(word)
            Log.d("com.example.sigmaenglish.ViewModel", "Word updated successfully")
        }
    }


    fun updateText(newTextOrg: String, newTextTrans: String, newTextDesc: String) {
        Log.d("com.example.sigmaenglish.ViewModel", "Updating text: $newTextOrg")
        _state.value = _state.value.copy(textOriginal = newTextOrg)
        Log.d("com.example.sigmaenglish.ViewModel", "Updating text: $newTextTrans")
        _state.value = _state.value.copy(textTranslation = newTextTrans)
        Log.d("com.example.sigmaenglish.ViewModel", "Updating text: $newTextDesc")
        _state.value = _state.value.copy(textDescription = newTextDesc)
    }

}
