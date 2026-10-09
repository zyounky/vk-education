package com.example.vkeducation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    private lateinit var input: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.main_root).applySystemBarInsets()
        input = findViewById(R.id.input_text)

        findViewById<Button>(R.id.open_second_button).setOnClickListener {
            val text = requireText() ?: return@setOnClickListener
            startActivity(Intent(this, SecondActivity::class.java).apply {
                putExtra(SecondActivity.EXTRA_TEXT, text)
            })
        }

        findViewById<Button>(R.id.dial_button).setOnClickListener {
            val text = requireText() ?: return@setOnClickListener
            val number = InputValidator.normalizePhoneNumber(text)
            if (number == null) {
                showInputError(R.string.error_invalid_phone)
                return@setOnClickListener
            }
            openExternalActivity(
                Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null)),
                R.string.error_no_dialer,
            )
        }

        findViewById<Button>(R.id.share_button).setOnClickListener {
            val text = requireText() ?: return@setOnClickListener
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            openExternalActivity(
                Intent.createChooser(sendIntent, getString(R.string.share_chooser_title)),
                R.string.error_no_share_app,
            )
        }
    }

    private fun requireText(): String? {
        val text = input.text.toString()
        if (text.isBlank()) {
            showInputError(R.string.error_empty_text)
            return null
        }
        input.error = null
        return text
    }

    private fun showInputError(message: Int) {
        input.error = getString(message)
        input.requestFocus()
    }

    private fun openExternalActivity(intent: Intent, errorMessage: Int) {
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}
