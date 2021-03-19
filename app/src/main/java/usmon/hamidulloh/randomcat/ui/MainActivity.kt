package usmon.hamidulloh.randomcat.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import usmon.hamidulloh.randomcat.R
import usmon.hamidulloh.randomcat.database.HistoryDatabase
import usmon.hamidulloh.randomcat.databinding.ActivityMainBinding
import usmon.hamidulloh.randomcat.model.Cat
import usmon.hamidulloh.randomcat.model.History
import usmon.hamidulloh.randomcat.network.CatApi
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var job: Job
    private lateinit var uiScope : CoroutineScope
    private lateinit var database : HistoryDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()
        uiScope = CoroutineScope(Dispatchers.IO + job)
        database = HistoryDatabase.getInstance(applicationContext)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            Toast.makeText(this, "Developer's channel", Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/usmon_h"))
            startActivity(intent)
        }

        fetchPhoto()

        binding.refresh.setOnClickListener {
            fetchPhoto()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.history -> {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    private fun fetchPhoto() {
        CatApi.catService.getRandomCat().enqueue(object : retrofit2.Callback<List<Cat>> {
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

                        Log.d("TAG", "onResponse: ${currentDate()}")

                        Glide.with(this@MainActivity)
                            .load(image.url)
                            .error(R.drawable.img_error)
                            .placeholder(R.drawable.img_loading)
                            .into(binding.imgRandom)

                        openDialog(
                            width = image.width,
                            height = image.height
                        )

                        shareUrl(image.url)

                        binding.imageFrame.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(image.url))
                            startActivity(intent)
                        }

                        binding.web.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(image.url))
                            startActivity(intent)
                        }
                        writeToRoom(image)
                    }
                }
            }

            override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.localizedMessage}")
            }

        })

    }

    private fun openDialog(width: Int, height: Int) {
        binding.imageFrame.setOnLongClickListener {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Original size")
                .setMessage("Width: ${width}\n\nHeight: ${height}")
                .setPositiveButton("OK") { dialogInterface, which -> }

            val alerDialog = builder.create()
            alerDialog.setCancelable(false)
            alerDialog.show()
            return@setOnLongClickListener true

        }
    }

    private fun shareUrl(url: String) {
        binding.share.setOnClickListener {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, "\uD83D\uDD17 Link to image ➜ ${url}")
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
    }

    private fun writeToRoom(history: History) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.historyDao().insertHistory(history)
            }
        }
    }

    private fun currentDate() : String {
        val current = Calendar.getInstance()
        val format = SimpleDateFormat("dd.MM.yyyy hh:mm")

        return format.format(current.time)
    }
}