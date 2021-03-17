package usmon.hamidulloh.randomcat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Response
import usmon.hamidulloh.randomcat.R
import usmon.hamidulloh.randomcat.databinding.ActivityMainBinding
import usmon.hamidulloh.randomcat.model.Cat
import usmon.hamidulloh.randomcat.model.CatList
import usmon.hamidulloh.randomcat.network.CatApi
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchPhoto()

        binding.refresh.setOnClickListener {
            fetchPhoto()
        }
    }

    private fun fetchPhoto() {
        CatApi.catService.getRandomCat().enqueue(object : retrofit2.Callback<List<Cat>> {
            override fun onResponse(call: Call<List<Cat>>, response: Response<List<Cat>>) {
                if (response.isSuccessful) {
                    val body = response.body()

                    if (body != null)
                        Glide.with(this@MainActivity)
                                .load(body[0].url)
                                .error(R.drawable.img_error)
                                .placeholder(R.drawable.img_loading)
                                .into(binding.imgRandom)

                    Log.d("TAG", "onResponse: ${body!![0].url}")
                }
            }

            override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.localizedMessage}")
            }

        })

    }
}