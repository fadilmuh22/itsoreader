package com.example.itsotest.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.itsotest.R
import com.example.itsotest.databinding.ActivityHomeBinding
import com.example.itsotest.ui.history.HistoryActivity
import com.example.itsotest.ui.login.MainActivity
import com.example.itsotest.ui.inputktp.InputKtpActivity
import com.example.itsotest.utils.EktpReaderIntiSingleton
import net.inti.intiektplib.EktpReaderInti
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {
    private lateinit var ektpReaderInti: EktpReaderInti
    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ektpReaderInti = EktpReaderIntiSingleton.getInstance(this)

        readKtp()
        adminManagement()
        goHistory()
    }

    private fun readKtp() {
        Log.d(TAG, "Memulai fungsi readKtp") // Log sebelum memulai
        binding.btnReadektpTest.setOnClickListener {
            ektpReaderInti.startReadEktp()
        }
        ektpReaderInti.ektpReaderResult.observe(this) { result ->
            Log.d(MainActivity.TAG, "{$result}")
            if (result != "") {
                val jsonObject = JSONObject(result)
                val intent = Intent(this, InputKtpActivity::class.java).apply {
                    putExtra(InputKtpActivity.RESULT, jsonObject.toString())
                }
                startActivity(intent)

            } else {
                Toast.makeText(
                    baseContext,
                    "Gagal Scan E-KTP",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun adminManagement() {
        binding.btnAdminManagement.setOnClickListener {
            ektpReaderInti.showManageAdmin()
        }
    }

    private fun goHistory() {
        binding.btnHistory.setOnClickListener {
            val intentHistory = Intent(this, HistoryActivity::class.java)
            startActivity(intentHistory)
        }
    }

    companion object {
        const val TAG = "HomeActivity"
    }
}