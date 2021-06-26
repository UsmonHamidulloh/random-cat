package usmon.hamidulloh.randomcat.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import usmon.hamidulloh.randomcat.database.HistoryDao
import usmon.hamidulloh.randomcat.model.Cat
import usmon.hamidulloh.randomcat.model.History
import usmon.hamidulloh.randomcat.network.CatApi
import java.text.SimpleDateFormat
import java.util.*

class Repository(val historyDao: HistoryDao) {

    val imageRepository = MutableLiveData<History>()

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + job)

    suspend fun fetchImage() {
        CatApi.catService.getRandomCat().enqueue(object : Callback<List<Cat>> {
            override fun onResponse(call: Call<List<Cat>>, response: Response<List<Cat>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val image = History(
                            width = body[0].width,
                            height = body[0].height,
                            url = body[0].url,
                            date = currentDate()
                        )

                        Log.d("TAG", "onResponse: ${image.url}")

                        imageRepository.value = image

                        uiScope.launch {
                            withContext(Dispatchers.IO) {
                                historyDao.insertItem(history = image)
                            }
                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.localizedMessage}")
            }
        })

    }

    @SuppressLint("SimpleDateFormat")
    private fun currentDate(): String {
        val current = Calendar.getInstance()
        val format = SimpleDateFormat("dd.MM.yyyy hh:mm")

        return format.format(current.time)
    }
}