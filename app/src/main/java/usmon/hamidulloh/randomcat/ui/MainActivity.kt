package usmon.hamidulloh.randomcat.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import usmon.hamidulloh.randomcat.R
import usmon.hamidulloh.randomcat.databinding.ActivityMainBinding
import usmon.hamidulloh.randomcat.model.History
import usmon.hamidulloh.randomcat.repository.HomeRepository
import usmon.hamidulloh.randomcat.viewmodel.HomeViewModel
import usmon.hamidulloh.randomcat.viewmodel.HomeViewModelFactory
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    private lateinit var imageUrlIntent: Intent

    private lateinit var imageViewModel: HomeViewModel
    private lateinit var imageViewModelFactory: HomeViewModelFactory
    private lateinit var repository: HomeRepository

    private lateinit var image: History

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        repository = HomeRepository()
        imageViewModelFactory = HomeViewModelFactory(repository)

        imageViewModel =
            ViewModelProvider(this, imageViewModelFactory).get(HomeViewModel::class.java)


        toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        imageViewModel.imageViewModel.observe(this, {
            image = it
            imageUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(image.url))
            fetchImage()
            clickListeners()
        })
    }



    private fun clickListeners() {
        val tChannelIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/cat_image"))

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

        binding.refresh.setOnClickListener {
            fetchImage()
        }
    }

    private fun fetchImage() {
        Glide.with(this)
            .load(image.url)
            .placeholder(R.drawable.img_loading)
            .error(R.drawable.img_error)
            .into(binding.imgRandom)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
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
            .setPositiveButton("OK") { dialogInterface, which -> }

        val alerDialog = builder.create()
        alerDialog.setCancelable(false)
        alerDialog.show()
    }
}

private fun shareUrlTemplate(url: String): Intent {
    return Intent().apply {
        this.action = Intent.ACTION_SEND
        this.putExtra(Intent.EXTRA_TEXT, "\uD83D\uDD17 Link to image ➜ ${url}")
        this.type = "text/plain"
    }
}