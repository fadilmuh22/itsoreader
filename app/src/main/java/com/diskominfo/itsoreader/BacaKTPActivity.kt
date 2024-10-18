package com.diskominfo.itsoreader


import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.core.os.BuildCompat
import id.co.inti.ztlib.util.readcard
import org.json.JSONObject
import pl.droidsonroids.gif.GifTextView

class BacaKTPActivity : readcard() {
    private var baca: readcard? = null
    private var bypass: Bypassadmin? = null

    private lateinit var home: Button
    private lateinit var readagain: Button
    private lateinit var back: Button
    private lateinit var authorize: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var message: TextView
    private lateinit var msg: TextView
    private lateinit var txtProgress: TextView
    private lateinit var gambar: ImageView
    private lateinit var gif: GifTextView
    private lateinit var dialog: Dialog
    private lateinit var progressDialog: ProgressDialog

    private val DIALOG_ID = 1
    private val TAG = "EKTP Reader"
    private lateinit var context: Context

    private var validMinutea = 0

    private var nikadmin: String? = null
    private var nameadmin: String? = null

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var data = JSONObject()
    private var isWorking = false
    private var photo = ByteArray(4096)
    private var handler: android.os.Handler? = null
    private var readTask: ReadKtp? = null

    private val BAR_VALUE = 1
    private val BAR_VIEW = 2
    private val errorMessage = arrayOf(
        "Kartu Tidak Terdeteksi",
        "Gagal Membaca Foto",
        "Gagal Membaca SAM",
        "Gagal Authentikasi",
        "Gagal Membaca Biodata",
        "Gagal Membaca Tanda Tangan",
        "Gagal Membaca Minutea",
        "Sidik Jari Tidak Cocok"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baca_ktp)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Custom back press logic
                val intent = Intent(this@BacaKTPActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
                ledoff()
            }
        })

        // Initialize UI components
        progressBar = findViewById(R.id.progressBar5)
        message = findViewById(R.id.message)
        readagain = findViewById(R.id.readagain)
        back = findViewById(R.id.back)
        gif = findViewById(R.id.gif)
        authorize = findViewById(R.id.authorize)
        msg = findViewById(R.id.progress)
        txtProgress = findViewById(R.id.txtProgress)
        gambar = findViewById(R.id.gambar)
        progressDialog = ProgressDialog(this)
        context = this
        dialog = Dialog(context)

        nodisplay()


//        // Set onClick listeners
//        back.setOnClickListener {
//            backbutton()
//        }
//
//        authorize.setOnClickListener {
//            authorizeAction()
//        }
//
//        readagain.setOnClickListener {
//            backbutton()
//        }

        baca = readcard()
        readTask = ReadKtp()
        readTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }


    override fun onDestroy() {
        super.onDestroy()
        readTask?.cancel(true)
    }

    // AsyncTask for reading KTP
    private inner class ReadKtp : AsyncTask<Void, Int, Int>() {

        override fun doInBackground(vararg params: Void?): Int {
            try {
                var ret: Int
                val i = initCR()
                if (i == -1) {
                    displayerror(errorMessage[0], false)
                    return -1
                }
                Log.d(TAG, "read begin")
                ret = waitcard()
                if (ret == -1) {
                    displayerror(errorMessage[0], false)
                    Log.d(TAG, "sc failed")
                    return -1
                }
                val uid = UID()
                if (uid == null || uid.isEmpty()) {
                    Log.e(TAG, "UID is null or empty")
                    return -1
                } else {
                    Log.d(TAG, "UID : $uid")
                }

                try {
                    ret = selectMF()
                    Log.d(TAG, "selectMF() returned: $ret")

                    // Add an immediate log after the successful return
                    Log.d(TAG, "Moving to the next step after selectMF()")
                } catch (e: Exception) {
                    Log.e(TAG, "Exception in selectMF(): " + e.message)
                    e.printStackTrace()
                    return -1  // Handle the error
                }
//                display()
//                update("5")
                if (ret == -1) {
                    Log.d(TAG, "sc failed")
                    displayerror(errorMessage[1], false)
                    return -1
                }
//                update("10")
                try {
                    ret = readPhoto()
                    Log.d(TAG, "readPhoto() returned: $ret")
                } catch (e: Exception) {
                    Log.e(TAG, "Exception in readPhoto(): " + e.message)
                    e.printStackTrace()
                    return -1  // Handle the error
                }
                if (ret == -1) {
                    Log.d(TAG, "Read Photo Failed")
                    displayerror(errorMessage[1], false)
                    return -1
                }

//                if (this.pccid == null || this.config == null) {
//                    Log.e(TAG, "pccid or config is null, cannot call open_sam()")
//                    return -1  // Handle the error and prevent further execution
//                }

                try{
                    ret = open_sam()
                    Log.d(TAG, "open_sam() returned: $ret")
                } catch (e: Exception) {
                    Log.e(TAG, "Exception in open_sam(): " + e.message)
                    e.printStackTrace()
                    return -1  // Handle the error
                }
                if (ret == -1) {
                    Log.d(TAG, "Open SAM Failed")
                    displayerror(errorMessage[2], false)
                    return -1
                } else {
                    Log.d(TAG, "open_sam() succeeded")
                }

//                update("40")
                try {
                    ret = Autentikasi()
                    Log.d(TAG, "Autentikasi() returned: $ret")
                } catch (e: Exception) {
                    Log.e(TAG, "Exception in Autentikasi(): " + e.message)
                    e.printStackTrace()
                    return -1  // Handle the error
                }

                if (ret == -1) {
                    Log.d(TAG, "Autentikasi Failed")
                    displayerror(errorMessage[3], false)
                    return -1
                }
//                update("60")

                try {
                    ret = ReadBiodata()
                    Log.d(TAG, "ReadBiodata() returned: $ret")
                } catch (e: Exception) {
                    Log.e(TAG, "Exception in ReadBiodata(): " + e.message)
                    e.printStackTrace()
                    return -1  // Handle the error
                }
                if (ret == -1) {
                    Log.d(TAG, "Read Biodata Failed")
                    displayerror(errorMessage[4], false)
                    return -1
                }
//                update("70")
                try {
                    ret = readSignature()
                    Log.d(TAG, "readSignature() returned: $ret")
                } catch (e: Exception) {
                    Log.e(TAG, "Exception in readSignature(): " + e.message)
                    e.printStackTrace()
                    return -1  // Handle the error
                }
                if (ret == -1) {
                    Log.d(TAG, "Read TTD Failed")
                    displayerror(errorMessage[5], true)
                    return -1
                }
//                update("75")
                try {
                    ret = readMinutae1()
                    Log.d(TAG, "readMinutae1() returned: $ret")
                } catch (e: Exception) {
                    Log.e(TAG, "Exception in readMinutae1(): " + e.message)
                    e.printStackTrace()
                    return -1  // Handle the error
                }
                if (ret == -1) {
                    Log.d(TAG, "Read Minutea1 Failed")
                } else {
                    validMinutea++
                }
//                update("80")


                try {
                    ret = readMinutae2()
                    Log.d(TAG, "readMinutae2() returned: $ret")
                } catch (e: Exception) {
                    Log.e(TAG, "Exception in readMinutae2(): " + e.message)
                    e.printStackTrace()
                    return -1  // Handle the error
                }
                if (ret == -1) {
                    Log.d(TAG, "Read Minutea2 Failed")
                } else {
                    validMinutea++
                }
                if (validMinutea == 0) {
                    Log.d(TAG, "Read Minutea Failed")
                    displayerror(errorMessage[6], true)
                    return -1
                }
//                update("90")
                ret = StopDSAutoVerification()
                if (ret == -1) {
                    Log.d(TAG, "Stop DS Failed")
                    displayerror(errorMessage[6], true)
                    return -1
                }
                ret = VerifyDS()
//                update("95")
                if (ret == -1) {
                    Log.d(TAG, "Verify DS Failed")
                    displayerror(errorMessage[6], true)
                    return -1
                }
                val getjari = getjari()
                Log.d(TAG, "Nilai :$ret")

                if (ret == -1) {
                    Log.d(TAG, "Sidik Jari Tidak Cocok")
                    displayerror(errorMessage[7], true)
                    return -1
                } else if (ret == 1) {
                    Log.d(TAG, "Nilai V :$ret")
//                    update("100")
                    data = getdataktp()
                    val intent = Intent(context, MainActivity::class.java).apply {
                        putExtra("json", data.toString())
                        putExtra("typeAuth", "Pemilik")
                        putExtra("nikAdmin", nikadmin)
                        putExtra("nameAdmin", nameadmin)
                    }
                    startActivity(intent)
                    finish()
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return 1
        }

        override fun onPostExecute(result: Int?) {
            // Handle post-execution if needed
        }
    }

    // AsyncTask for bypassing admin (if needed)
    private inner class Bypassadmin : AsyncTask<Void, Int, Int>() {

        override fun doInBackground(vararg params: Void?): Int {
            runOnUiThread {
                dialog.setContentView(R.layout.alertfp)
                val txtContent = dialog.findViewById<TextView>(R.id.alertContent)
                txtContent.text = "Taruh Jari Terdaftar Admin \n Pada Pemindai Sidik Jari"
                dialog.show()
            }
            val result = bypassadmin()
            return result
        }

        override fun onPostExecute(result: Int?) {
            Log.d(TAG, "Hasil $result")
            if (result == -1) {
                Log.d(TAG, "Sidik Jari Tidak Cocok")
                dialog.dismiss()
                displayerror(errorMessage[7], true)
            }

            if (result == 2) {
                update("100")
                data = getdataktp()
                val intent = Intent(context, MainActivity::class.java).apply {
                    putExtra("json", data.toString())
                    putExtra("typeAuth", "Admin")
                    putExtra("nikAdmin", nikadmin)
                    putExtra("nameAdmin", nameadmin)
                }
                dialog.dismiss()
                startActivity(intent)
                finish()
            }
        }
    }

    // UI Update Methods
    private fun display() {
        runOnUiThread {
            msg.visibility = View.VISIBLE
            txtProgress.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            message.visibility = View.INVISIBLE
            progressBar.progress = 5
            txtProgress.text = "5 %"
            gambar.visibility = View.INVISIBLE
            msg.text = "Sedang Membaca KTP"
        }
    }

    private fun nodisplay() {
        runOnUiThread {
            msg.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
            message.visibility = View.VISIBLE
            gambar.visibility = View.INVISIBLE
            readagain.visibility = View.INVISIBLE
            back.visibility = View.INVISIBLE
            authorize.visibility = View.INVISIBLE
        }
    }

    private fun update(progressValue: String) {
        runOnUiThread {
            progressBar.visibility = View.VISIBLE
            progressBar.progress = progressValue.toInt()
            txtProgress.text = "$progressValue %"
        }
    }

    private fun sidikjari(text: String) {
        runOnUiThread {
            msg.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            msg.text = text
            try {
//                gif.setBackgroundResource(R.drawable.ktpfingergif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun displayerror(error: String, stat: Boolean) {
        runOnUiThread {
            msg.text = error
            if (stat) {
                readagain.visibility = View.VISIBLE
                authorize.visibility = View.VISIBLE
                back.visibility = View.VISIBLE
            } else {
                readagain.visibility = View.VISIBLE
                back.visibility = View.VISIBLE
            }
        }
    }

    private fun backbutton() {
        runOnUiThread {
            System.gc()
            msg.visibility = View.INVISIBLE
            txtProgress.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
            message.visibility = View.VISIBLE
            msg.text = "Sedang Membaca KTP"
            gambar.visibility = View.INVISIBLE
            readagain.visibility = View.INVISIBLE
            authorize.visibility = View.INVISIBLE
            back.visibility = View.INVISIBLE
            try {
//                gif.setBackgroundResource(R.drawable.ktptapgif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            readTask?.cancel(true)
            readTask = ReadKtp()
            readTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    // Authorize button action
    private fun authorizeAction() {
        readTask?.cancel(true)
        data = getdataktp()

    }
}
