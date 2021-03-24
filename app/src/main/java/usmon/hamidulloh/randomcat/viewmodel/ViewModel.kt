package usmon.hamidulloh.randomcat.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import usmon.hamidulloh.randomcat.database.HistoryDao
import usmon.hamidulloh.randomcat.model.History
import usmon.hamidulloh.randomcat.repository.HomeRepository

class ViewModel(historyDao: HistoryDao) : ViewModel() {
    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + job)
    private val repo = HomeRepository(historyDao)

    val imageViewModel = repo.imageRepository
    val imageQueryViewModel = repo.imagesQuery

    init {
        fetchPhoto()
    }

    fun deleteItemHistory(history: History) {
        repo.deleteItem(history)
    }

    fun deleteAllItems() {
        repo.deleteAllItem()
    }

    fun fetchPhoto() {
        viewModelScope.launch {
            repo.fetchImage()
        }
    }
}