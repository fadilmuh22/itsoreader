package com.diskominfo.itsoreader

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var scan: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        scan = findViewById<Button>(R.id.btn_ktp)

        scan.setOnClickListener {
            startActivity(Intent(this, BacaKTPActivity::class.java))
        }
    }
}