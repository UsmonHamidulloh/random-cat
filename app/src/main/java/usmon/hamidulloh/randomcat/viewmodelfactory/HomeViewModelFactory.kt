package usmon.hamidulloh.randomcat.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import usmon.hamidulloh.randomcat.database.HistoryDao
import usmon.hamidulloh.randomcat.repository.HomeRepository
import usmon.hamidulloh.randomcat.viewmodel.HomeViewModel

class HomeViewModelFactory(val historyDao: HistoryDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(historyDao = historyDao) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}