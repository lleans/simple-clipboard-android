package com.lleans.clipboard

import android.content.ClipboardManager
import android.content.ClipData
import android.content.ClipDescription
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
                val text: String? = intent.getStringExtra(Intent.EXTRA_TEXT)
                val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                }

                if (text != null && uri != null) {
                    // -- both text & one URI --
                    val textItem = ClipData.Item(text)
                    val uriItem  = ClipData.Item(uri)

                    // MIME types: plain text + actual type of URI
                    val mimeTypes = arrayOf(
                        ClipDescription.MIMETYPE_TEXT_PLAIN,
                        contentResolver.getType(uri) ?: ClipDescription.MIMETYPE_TEXT_PLAIN
                    )
                    val description = ClipDescription("shared text+file", mimeTypes)
                    val clipData = ClipData(description, textItem).apply {
                        addItem(uriItem)
                    }

                    clipboard.setPrimaryClip(clipData)
                    Toast.makeText(this, "Text & image copied to clipboard", Toast.LENGTH_SHORT).show()

                } else if (text != null) {
                    // -- only text --
                    val clip = ClipData.newPlainText("shared text", text)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()

                } else if (uri != null) {
                    // -- only one URI --
                    val clip = ClipData.newUri(contentResolver, "shared file", uri)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(this, "File copied to clipboard", Toast.LENGTH_SHORT).show()
                }
            }

            Intent.ACTION_SEND_MULTIPLE -> {
                // Multiple URIs (with or without text)
                val uris: List<Uri>? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
                }
                val text: String? = intent.getStringExtra(Intent.EXTRA_TEXT)

                if (!uris.isNullOrEmpty()) {
                    // First, prepare ClipData items
                    val firstUri = uris[0]
                    val firstItem = when {
                        text != null -> ClipData.Item(text)
                        else -> ClipData.Item(firstUri)
                    }

                    // Build mime types array
                    val mimeTypes = mutableListOf<String>()
                    text?.let { mimeTypes += ClipDescription.MIMETYPE_TEXT_PLAIN }
                    // Try to detect the type of first URI
                    mimeTypes += (contentResolver.getType(firstUri)
                        ?: ClipDescription.MIMETYPE_TEXT_PLAIN)

                    val description = ClipDescription("shared multiple files", mimeTypes.toTypedArray())
                    val clipData = ClipData(description, firstItem)

                    // If text was first, now add the URI as second; otherwise URIs start here
                    if (text != null) {
                        clipData.addItem(ClipData.Item(firstUri))
                    }

                    // Add remaining URIs
                    uris.drop(if (text != null) 1 else 0).forEach { uriItem ->
                        clipData.addItem(ClipData.Item(uriItem))
                    }

                    clipboard.setPrimaryClip(clipData)
                    Toast.makeText(this,
                        "Copied ${uris.size} item${if (uris.size>1) "s" else ""} to clipboard",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Nothing to copy", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
