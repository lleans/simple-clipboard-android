package com.lleans.clipboard

import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class ShareToClipboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.let { handleShare(it) }
        finish()
    }

    private fun handleShare(intent: Intent) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        when (intent.action) {
            Intent.ACTION_SEND -> {
                intent.getStringExtra(Intent.EXTRA_TEXT)?.let { text ->
                    val clip = ClipData.newPlainText("shared text", text)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
                }

                val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                }

                uri?.let {
                    val clip = ClipData.newUri(contentResolver, "shared file", it)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(this, "File copied to clipboard", Toast.LENGTH_SHORT).show()
                }
            }

            Intent.ACTION_SEND_MULTIPLE -> {
                val uris: List<Uri>? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
                }

                uris?.firstOrNull()?.let {
                    val clip = ClipData.newUri(contentResolver, "shared file", it)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(this, "File copied to clipboard", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
