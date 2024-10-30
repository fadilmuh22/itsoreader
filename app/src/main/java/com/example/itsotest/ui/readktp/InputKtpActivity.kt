package com.example.itsotest.ui.readktp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.itsotest.R
import org.json.JSONObject

class InputKtpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_ktp)

        val result = intent.getStringExtra(RESULT)
        result?.let {
            // Lakukan sesuatu dengan data `result` (misalnya, parsing kembali ke JSONObject jika diperlukan)
            val jsonObject = JSONObject(it)
            Toast.makeText(this, "NIK : ${jsonObject.get("nik").toString()}", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val RESULT = "result"
    }
}