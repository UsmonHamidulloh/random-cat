package usmon.hamidulloh.randomcat.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import usmon.hamidulloh.randomcat.R
import usmon.hamidulloh.randomcat.database.HistoryDao
import usmon.hamidulloh.randomcat.database.HistoryDatabase
import usmon.hamidulloh.randomcat.databinding.ActivityMainBinding
import usmon.hamidulloh.randomcat.model.History
import usmon.hamidulloh.randomcat.utils.shareUrlTemplate
import usmon.hamidulloh.randomcat.viewmodel.MainViewModel
import usmon.hamidulloh.randomcat.viewmodelfactory.ViewModelFactory
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var imageUrlIntent: Intent
    private lateinit var mainViewModel: MainViewModel
    private lateinit var imageViewModelFactory: ViewModelFactory
    private lateinit var database: HistoryDatabase
    private lateinit var historyDao: HistoryDao
    private lateinit var circularProgressDrawable: CircularProgressDrawable
    private lateinit var tChannelIntent: Intent
    private lateinit var image: History

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()

        mainViewModel.imageViewModel.observe(this, {
            image = it
            fetchImage()
            imageUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(image.url))
        })

        toolbar.setNavigationOnClickListener {
            startActivity(tChannelIntent)
        }

        binding.imageFrame.setOnClickListener {
            startActivity(imageUrlIntent)
        }

        binding.imageFrame.setOnLongClickListener {
            openDialog(width = image.width, height = image.height)

            return@setOnLongClickListener true
        }

        binding.web.setOnClickListener {
            startActivity(imageUrlIntent)
        }

        binding.share.setOnClickListener {
            startActivity(shareUrlTemplate(image.url))
        }

        binding.refresh.setOnClickListener { refresh ->
            animRotate()
            refresh.isEnabled = true
            mainViewModel.fetchPhoto()
            mainViewModel.imageViewModel.observe(this, {
                image = it
                imageUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(image.url))
                fetchImage()
                refresh.isEnabled = false
            })
        }
    }

    private fun initialize() {
        database = HistoryDatabase.getInstance(applicationContext)
        historyDao = database.historyDao()

        tChannelIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/cat_image"))

        imageViewModelFactory = ViewModelFactory(historyDao)

        mainViewModel =
            ViewModelProvider(this, imageViewModelFactory).get(MainViewModel::class.java)

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 10f
        circularProgressDrawable.centerRadius = 100f
        circularProgressDrawable.start()

    }

    private fun fetchImage() {
        Glide.with(this)
            .load(image.url)
            .error(R.drawable.img_error)
            .placeholder(circularProgressDrawable)
            .into(binding.imgRandom)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.history -> {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    private fun openDialog(width: Int, height: Int) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Original size")
            .setMessage("Width: ${width}\n\nHeight: ${height}")
            .setPositiveButton("OK") { _, _ -> }

        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun animRotate() {
        val animator = ObjectAnimator.ofFloat(binding.refresh, View.ROTATION, 0F, 360F)

        animator.duration = 1_000
        animator.disableViewDuringAnimation(binding.refresh)

        animator.start()
    }

    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                view.isEnabled = true
            }
        })
    }


}