package usmon.hamidulloh.randomcat.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import usmon.hamidulloh.randomcat.database.HistoryDao
import usmon.hamidulloh.randomcat.viewmodel.HistoryViewModel

class HistoryViewModelFactory(val historyDao: HistoryDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(historyDao = historyDao) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}