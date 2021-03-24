package usmon.hamidulloh.randomcat.viewmodelfactory

import androidx.lifecycle.ViewModelProvider
import usmon.hamidulloh.randomcat.database.HistoryDao
import usmon.hamidulloh.randomcat.viewmodel.ViewModel

class ViewModelFactory(val historyDao: HistoryDao) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModel::class.java)) {
            return ViewModel(historyDao = historyDao) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}