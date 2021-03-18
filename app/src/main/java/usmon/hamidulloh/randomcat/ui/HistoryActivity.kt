package usmon.hamidulloh.randomcat.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import usmon.hamidulloh.randomcat.R
import usmon.hamidulloh.randomcat.adapter.HistoryAdapter
import usmon.hamidulloh.randomcat.database.HistoryDatabase
import usmon.hamidulloh.randomcat.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.apply {
            setNavigationIcon(getDrawable(R.drawable.ic_arrow))
            setNavigationOnClickListener {
                finish()
            }
        }


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

        binding.imagesList.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}