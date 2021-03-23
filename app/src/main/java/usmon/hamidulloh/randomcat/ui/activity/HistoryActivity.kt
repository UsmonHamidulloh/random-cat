package usmon.hamidulloh.randomcat.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import usmon.hamidulloh.randomcat.R
import usmon.hamidulloh.randomcat.database.HistoryDao
import usmon.hamidulloh.randomcat.database.HistoryDatabase
import usmon.hamidulloh.randomcat.databinding.ActivityHistoryBinding
import usmon.hamidulloh.randomcat.model.History
import usmon.hamidulloh.randomcat.ui.adapter.HistoryAdapter
import usmon.hamidulloh.randomcat.utils.shareUrlTemplate
import usmon.hamidulloh.randomcat.viewmodel.HomeViewModel
import usmon.hamidulloh.randomcat.viewmodelfactory.HomeViewModelFactory

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var job: Job
    private lateinit var uiScope: CoroutineScope
    private lateinit var database: HistoryDatabase
    private lateinit var historyDao: HistoryDao
    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFactory: HomeViewModelFactory

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
        historyDao = database.historyDao()

        viewModelFactory = HomeViewModelFactory(historyDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        binding.toolbar.apply {
            setNavigationIcon(getDrawable(R.drawable.ic_arrow))
            setNavigationOnClickListener {
                finish()
            }
        }

        historyAdapter = HistoryAdapter(HistoryAdapter.ImageItemCallBack {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            startActivity(intent)
        },
            HistoryAdapter.ImageItemCallBack { item ->
                deleteDialog(item)
                historyAdapter.notifyDataSetChanged()
            },
            HistoryAdapter.ImageItemCallBack { item ->
                shareUrlTemplate(item.url)
            }
        )

        viewModel.imageQueryViewModel.observe(this, {
            historyAdapter.submitList(it)
        })

        binding.imagesList.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun deleteDialog(history: History) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setTitle("Delete")
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