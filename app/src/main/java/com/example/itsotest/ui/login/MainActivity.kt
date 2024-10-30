package com.example.itsotest.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.itsotest.R
import com.example.itsotest.ui.home.HomeActivity
import com.example.itsotest.utils.EktpReaderIntiSingleton
import net.inti.intiektplib.ActiveAdmin
import net.inti.intiektplib.Terminal

class MainActivity : AppCompatActivity() {

    private val ektpReaderInti by lazy { EktpReaderIntiSingleton.getInstance(this) }
    private lateinit var buttonAuth: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAuth = findViewById(R.id.btn_admin_auth_test)

        checkAdminExist()

        adminAuth()


    }

    private fun checkAdminExist() {
        ektpReaderInti.checkAdminExist()
        ektpReaderInti.adminExistResult.observe(this) { result ->
            if (result) {
                buttonAuth.isEnabled = true
                Log.d(TAG, "Admin Exist")
            } else {
                Toast.makeText(baseContext, "Admin Not Exist", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun adminAuth() {
        buttonAuth.setOnClickListener {
            ektpReaderInti.authAdmin()
        }

        ektpReaderInti.adminAuthResult.observe(this) { result ->
            if (result) {
                Toast.makeText(baseContext, "Selamat Datang ${ActiveAdmin.nama}", Toast.LENGTH_LONG).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                Log.i("ektp", "Terminal ID : ${Terminal.id}")
            } else {
                Toast.makeText(baseContext, "Admin Not Found", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }

}