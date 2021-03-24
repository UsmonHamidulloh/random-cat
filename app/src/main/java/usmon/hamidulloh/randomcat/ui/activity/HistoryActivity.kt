package usmon.hamidulloh.randomcat.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import usmon.hamidulloh.randomcat.viewmodel.ViewModel
import usmon.hamidulloh.randomcat.viewmodelfactory.ViewModelFactory

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var job: Job
    private lateinit var uiScope: CoroutineScope
    private lateinit var database: HistoryDatabase
    private lateinit var historyDao: HistoryDao
    private lateinit var viewModel: ViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var sendFeedbackIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()

        viewModel.imageQueryViewModel.observe(this, {
            historyAdapter.submitList(it)
        })

        binding.imagesList.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.history_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.send_feedback_menu -> {startActivity(sendFeedbackIntent)}
            R.id.clear_history_menu -> {viewModel.deleteAllItems()}
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initialize() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        job = Job()
        uiScope = CoroutineScope(Dispatchers.IO + job)
        database = HistoryDatabase.getInstance(applicationContext)
        historyDao = database.historyDao()

        viewModelFactory = ViewModelFactory(historyDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ViewModel::class.java)

        binding.toolbar.apply {
            navigationIcon = getDrawable(R.drawable.ic_arrow)
            setNavigationOnClickListener {
                finish()
            }
        }

        sendFeedbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/u_hamidulloh"))

        historyAdapter = HistoryAdapter(HistoryAdapter.ImageItemCallBack {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            startActivity(intent)
        },
            HistoryAdapter.ImageItemCallBack { item ->
                deleteDialog(item)
                historyAdapter.notifyDataSetChanged()
            },

            HistoryAdapter.ImageItemCallBack { item ->
                startActivity(shareUrlTemplate(item.url))
            }
        )
    }

    private fun deleteDialog(history: History) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setTitle("Delete")
            setMessage("Are you sure you want delete item forever ?")
            setPositiveButton("Delete") { _, _ ->
                viewModel.deleteItemHistory(history)
            }
            setNegativeButton("Cancel") { _, _ -> }
            val alertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }
}