package com.example.vkeducation

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)
        findViewById<View>(R.id.second_root).applySystemBarInsets()

        val text = intent.getStringExtra(EXTRA_TEXT)
        findViewById<TextView>(R.id.received_text).text =
            text?.takeIf { it.isNotBlank() } ?: getString(R.string.no_received_text)
        findViewById<Button>(R.id.back_button).setOnClickListener { finish() }
    }

    companion object {
        const val EXTRA_TEXT = "com.example.vkeducation.extra.TEXT"
    }
}
