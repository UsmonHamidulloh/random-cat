package usmon.hamidulloh.randomcat.viewmodel

import androidx.lifecycle.ViewModel
import usmon.hamidulloh.randomcat.database.HistoryDao
import usmon.hamidulloh.randomcat.model.History
import usmon.hamidulloh.randomcat.repository.HistoryRepository

class HistoryViewModel(val historyDao: HistoryDao) : ViewModel() {
    private val repo = HistoryRepository(historyDao)

    val imageQueryViewModel = repo.imagesQuery

    fun deleteItemHistory(history: History) {
        repo.deleteItem(history)
    }

    fun deleteAllItems() {
        repo.deleteAllItem()
    }

}