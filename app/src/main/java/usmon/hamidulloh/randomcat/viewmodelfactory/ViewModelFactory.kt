package usmon.hamidulloh.randomcat.viewmodelfactory

import androidx.lifecycle.ViewModelProvider
import usmon.hamidulloh.randomcat.database.HistoryDao
import usmon.hamidulloh.randomcat.viewmodel.MainViewModel

class ViewModelFactory(val historyDao: HistoryDao) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(historyDao = historyDao) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}