package usmon.hamidulloh.randomcat.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import usmon.hamidulloh.randomcat.model.Cat
import usmon.hamidulloh.randomcat.model.History
import usmon.hamidulloh.randomcat.network.CatApi
import java.text.SimpleDateFormat
import java.util.*

class HomeRepository() {

    val imageRepository = MutableLiveData<History>()

    suspend fun fetchImage() {
        withContext(Dispatchers.IO) {
            CatApi.catService.getRandomCat().enqueue(object : Callback<List<Cat>>{
                override fun onResponse(call: Call<List<Cat>>, response: Response<List<Cat>>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            imageRepository.value = History (
                                width = body[0].width,
                                height = body[0].height,
                                url = body[0].url,
                                date = currentDate()
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                    Log.d("TAG", "onFailure: ${t.localizedMessage}")
                }
            })
        }
    }
    private fun currentDate() : String {
        val current = Calendar.getInstance()
        val format = SimpleDateFormat("dd.MM.yyyy hh:mm")

        return format.format(current.time)
    }
}