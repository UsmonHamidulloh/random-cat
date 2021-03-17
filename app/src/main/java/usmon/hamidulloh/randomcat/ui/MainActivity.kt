package usmon.hamidulloh.randomcat.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Response
import usmon.hamidulloh.randomcat.R
import usmon.hamidulloh.randomcat.databinding.ActivityMainBinding
import usmon.hamidulloh.randomcat.model.Cat
import usmon.hamidulloh.randomcat.network.CatApi

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

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

                    if (body != null) {
                        Glide.with(this@MainActivity)
                                .load(body[0].url)
                                .error(R.drawable.img_error)
                                .placeholder(R.drawable.img_loading)
                                .into(binding.imgRandom)

                        openDialog(
                                width = body[0].width,
                                height = body[0].height
                        )

                        shareUrl(body[0].url)

                        binding.web.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(body[0].url))
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.localizedMessage}")
            }

        })

    }

    private fun openDialog(width: Int, height: Int) {
        binding.imageFrame.setOnClickListener {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Original size")
                    .setMessage("Width: ${width}\n\nHeight: ${height}")
                    .setPositiveButton("OK") { dialogInterface, which -> }

            val alerDialog = builder.create()
            alerDialog.setCancelable(false)
            alerDialog.show()
        }
    }

    private fun shareUrl(url: String) {
        binding.share.setOnClickListener {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, url)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
    }
}