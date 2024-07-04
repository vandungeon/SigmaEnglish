import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.example.sigmaenglish.DBType
import com.example.sigmaenglish.TaskDataBase
import com.example.sigmaenglish.Repository
import com.example.sigmaenglish.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository
    private val taskDAO = TaskDataBase.getDatabase(application).dao()

    // LiveData to observe changes in the list of tasks
    private val _tasks = MediatorLiveData<List<DBType.Task>>()
    val tasks: LiveData<List<DBType.Task>> = _tasks

    // MutableState for managing UI text input
    private val _state = mutableStateOf(ScreenState())
    val state: ScreenState get() = _state.value

    init {
        repository = Repository(taskDAO)
        _tasks.addSource(repository.readAllData) { tasks ->
            _tasks.value = tasks
            _state.value = _state.value.copy(tasks = tasks)
            Log.d("TaskViewModel", "Tasks updated: $tasks")
        }
    }

    fun addTask(task: DBType.Task) {
        Log.d("TaskViewModel", "Adding task: ${task.bodyTask}")
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTASK(task)
            Log.d("TaskViewModel", "Task added successfully")
        }
    }

    fun deleteTask(task: DBType.Task) {
        Log.d("TaskViewModel", "Deleting task: ${task.bodyTask}")
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTASK(task)
            Log.d("TaskViewModel", "Task deleted successfully")
        }
    }

    fun updateText(newText: String) {
        Log.d("TaskViewModel", "Updating text: $newText")
        _state.value = _state.value.copy(text = newText)
    }
}

