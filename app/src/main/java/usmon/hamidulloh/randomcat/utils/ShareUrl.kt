package usmon.hamidulloh.randomcat.utils

import android.content.Intent

fun shareUrlTemplate(url: String): Intent {
    return Intent().apply {
        this.action = Intent.ACTION_SEND
        this.putExtra(Intent.EXTRA_TEXT, "\uD83D\uDD17 Link to image ➜ ${url}")
        this.type = "text/plain"
    }
}