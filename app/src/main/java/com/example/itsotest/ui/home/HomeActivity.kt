package com.example.itsotest.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.itsotest.R
import com.example.itsotest.ui.history.HistoryActivity
import com.example.itsotest.ui.readktp.InputKtpActivity
import com.example.itsotest.utils.EktpReaderIntiSingleton
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {
    private val ektpReaderInti by lazy { EktpReaderIntiSingleton.getInstance(this) }
    private lateinit var buttonReadKtp: Button
    private lateinit var buttonAdminManagement: Button
    private lateinit var buttonHistory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        buttonReadKtp = findViewById(R.id.btn_readektp_test)
        buttonAdminManagement = findViewById(R.id.btn_admin_management)
        buttonHistory = findViewById(R.id.btn_history)

        readKtp()
        adminManagement()
        goHistory()
    }

    private fun readKtp() {
        Log.d(TAG, "Memulai fungsi readKtp") // Log sebelum memulai
        buttonReadKtp.setOnClickListener {
            ektpReaderInti.startReadEktp()
        }
        ektpReaderInti.ektpReaderResult.observe(this) { result ->
            Log.d(TAG, "{$result}")
            try {
                if (result != "") {
                    val jsonObject = JSONObject(result)
                    val nik = jsonObject.getString("nik")

                    Log.d(TAG, "Data KTP NIK: $nik")
                    Toast.makeText(
                        baseContext,
                        "NIK : $nik",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        baseContext,
                        "Failed To Read EKTP",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error reading EKTP data: ${e.message}")
                Toast.makeText(
                    baseContext,
                    "Error reading EKTP data",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    private fun adminManagement() {
        buttonAdminManagement.setOnClickListener {
            ektpReaderInti.showManageAdmin()
        }
    }

    private fun goHistory() {
        buttonHistory.setOnClickListener {
            val intentHistory = Intent(this, HistoryActivity::class.java)
            startActivity(intentHistory)
        }
    }

    companion object {
        const val TAG = "HomeActivity"
    }
}