package usmon.hamidulloh.randomcat.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import usmon.hamidulloh.randomcat.R
import usmon.hamidulloh.randomcat.adapter.HistoryAdapter
import usmon.hamidulloh.randomcat.database.HistoryDatabase
import usmon.hamidulloh.randomcat.databinding.ActivityHistoryBinding
import usmon.hamidulloh.randomcat.model.History

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var job: Job
    private lateinit var uiScope : CoroutineScope
    private lateinit var database : HistoryDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        job = Job()
        uiScope = CoroutineScope(Dispatchers.IO + job)
        database = HistoryDatabase.getInstance(applicationContext)

        binding.toolbar.apply {
            setNavigationIcon(getDrawable(R.drawable.ic_arrow))
            setNavigationOnClickListener {
                finish()
            }
        }


        historyAdapter = HistoryAdapter(HistoryAdapter.ImageItemCallBack {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            startActivity(intent)
        }, HistoryAdapter.ImageItemCallBack {
            deleteDialog(it)
            historyAdapter.notifyDataSetChanged()
        }, HistoryAdapter.ImageItemCallBack {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, "\uD83D\uDD17 Link to image ➜ ${it.url}")
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        })

        val database = HistoryDatabase.getInstance(application)
        val historyDao = database.historyDao()
        val liveData = historyDao.queryAllImages()

        liveData.observe(this, {
            historyAdapter.images = it
            historyAdapter.notifyDataSetChanged()
        })

        binding.imagesList.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun deleteDialog(history: History) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setTitle("Delete ?")
            setMessage("Are you sure you want delete item forever ?")
            setPositiveButton("Delete") { dialogInterface, which ->
                uiScope.launch {
                    withContext(Dispatchers.IO) {
                        database.historyDao().deleteHistory(history)
                    }
                }
            }
            setNegativeButton("Cancel") { dialogInterface, which -> }

            val alertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }
}