package usmon.hamidulloh.randomcat.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import usmon.hamidulloh.randomcat.database.HistoryDao
import usmon.hamidulloh.randomcat.model.History
import usmon.hamidulloh.randomcat.repository.Repository

class MainViewModel(historyDao: HistoryDao) : ViewModel() {
    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + job)
    private val repo = Repository(historyDao)

    val imageViewModel = repo.imageRepository

    init {
        fetchPhoto()
    }

    fun fetchPhoto() {
        viewModelScope.launch {
            repo.fetchImage()
        }
    }
}