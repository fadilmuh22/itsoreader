package com.example.itsotest.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.itsotest.R
import com.example.itsotest.databinding.ActivityMainBinding
import com.example.itsotest.ui.home.HomeActivity
import com.example.itsotest.utils.EktpReaderIntiSingleton
import net.inti.intiektplib.ActiveAdmin
import net.inti.intiektplib.EktpReaderInti
import net.inti.intiektplib.Terminal
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var ektpReaderInti: EktpReaderInti
    private lateinit var binding :ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ektpReaderInti = EktpReaderIntiSingleton.getInstance(this)

        checkAdminExist()

        adminAuth()

//        observeEktpReaderInti()
    }

    private fun checkAdminExist() {
        ektpReaderInti.checkAdminExist()
        ektpReaderInti.adminExistResult.observe(this) { result ->
            if (result) {
                binding.btnAdminAuthTest.isEnabled = true
                Log.d(TAG, "Admin Exist")
            } else {
                Toast.makeText(baseContext, "Admin Not Exist", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun observeEktpReaderInti() {
        ektpReaderInti.ektpReaderResult.observe(this) { result ->
            Log.d(TAG, "{$result}")
            if (result != "") {
                val jsonObject = JSONObject(result)
                val nik = jsonObject.getString("nik")

                Log.d(TAG, "Data KTP NIK: $nik")
                Toast.makeText(
                    baseContext,
                    "NIK : $nik",
                    Toast.LENGTH_LONG
                ).show()

                // Menghapus observer setelah mendapatkan data
                ektpReaderInti.ektpReaderResult.removeObservers(this)
                finish()
            } else {
                Toast.makeText(
                    baseContext,
                    "Failed To Read EKTP",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun adminAuth() {
        binding.btnAdminAuthTest.setOnClickListener {
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