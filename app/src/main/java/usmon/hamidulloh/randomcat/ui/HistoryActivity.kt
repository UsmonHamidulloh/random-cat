package usmon.hamidulloh.randomcat.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import usmon.hamidulloh.randomcat.R
import usmon.hamidulloh.randomcat.adapter.HistoryAdapter
import usmon.hamidulloh.randomcat.database.HistoryDatabase

class HistoryActivity : AppCompatActivity() {
    private lateinit var historyAdapter: HistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val recyclerView = findViewById<RecyclerView>(R.id.images_list)

        historyAdapter = HistoryAdapter(HistoryAdapter.ImageItemCallBack {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            startActivity(intent)
        })

        val database = HistoryDatabase.getInstance(application)
        val historyDao = database.historyDao()
        val liveData = historyDao.queryAllImages()

        liveData.observe(this, {
            historyAdapter.images = it
            historyAdapter.notifyDataSetChanged()
        })

        recyclerView.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}