package usmon.hamidulloh.randomcat.repository

import kotlinx.coroutines.*
import usmon.hamidulloh.randomcat.database.HistoryDao
import usmon.hamidulloh.randomcat.model.History

class HistoryRepository(val historyDao: HistoryDao) {
    val imagesQuery = historyDao.queryAllImages()

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + job)

    fun deleteItem(history: History) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                historyDao.deleteItem(history)
            }
        }
    }

    fun deleteAllItem() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                historyDao.deleteAllItems()
            }
        }
    }

}