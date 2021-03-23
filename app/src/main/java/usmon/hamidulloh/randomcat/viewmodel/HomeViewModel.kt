package usmon.hamidulloh.randomcat.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import usmon.hamidulloh.randomcat.repository.HomeRepository

class HomeViewModel(val repo: HomeRepository) : ViewModel() {
    val job = Job()
    val viewModelScope = CoroutineScope(Dispatchers.IO + job)

    val imageViewModel = repo.imageRepository

    init {
        fetchPhoto()
    }

    private fun fetchPhoto() {
        viewModelScope.launch {
            repo.fetchImage()
        }
    }
}