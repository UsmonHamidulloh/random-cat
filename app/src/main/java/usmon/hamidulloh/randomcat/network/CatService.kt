package usmon.hamidulloh.randomcat.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import usmon.hamidulloh.randomcat.model.Cat

private const val BASE_URL = "https://api.thecatapi.com/"

interface CatService {
    @GET("/v1/images/search")
    fun getRandomCat() : Call<List<Cat>>
}

val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

object CatApi {
    val catService: CatService by lazy { retrofit.create(CatService::class.java) }
}