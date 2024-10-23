package com.diskominfo.itsoreader

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Base64
import android.util.Log
import id.co.inti.ztlib.util.Hex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.SocketException
import java.security.KeyStore
import java.security.SecureRandom
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLServerSocket
import javax.net.ssl.SSLServerSocketFactory

class TcpService : Service() {

    private var sssf: SSLServerSocketFactory? = null
    private var sss: SSLServerSocket? = null
    private var isRunning = true
    private lateinit var db: DBHelper

    override fun onCreate() {
        super.onCreate()
        db = DBHelper(this)
        db.openDB()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.IO).launch {
            tcp() // Start the socket server in the background
        }
        return START_STICKY
    }

    private fun tcp() {
        try {
            val sslContext = SSLContext.getInstance("TLS")
            val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            val ks = KeyStore.getInstance("BKS")
            val fis = resources.openRawResource(R.raw.key)
            ks.load(fis, "1nt1".toCharArray())
            kmf.init(ks, "1nt1".toCharArray())
            sslContext.init(kmf.keyManagers, null, SecureRandom())
            sssf = sslContext.serverSocketFactory

            sss?.close()
            sss = sssf?.createServerSocket(5503) as SSLServerSocket
            sss?.reuseAddress = true
            Log.d(TAG, "Secure Socket is starting up on port 5503")
        } catch (e: Exception) {
            Log.d(TAG, "Secure Socket Error: $e")
            return
        }

        try {
            while (isRunning) {
                try {
                    val socket = sss?.accept()
                    Log.d(TAG, "Client Connected")

                    socket?.use {
                        val `in` = BufferedReader(InputStreamReader(it.getInputStream()))
                        val out = BufferedWriter(OutputStreamWriter(it.getOutputStream()))
                        val buffer = CharArray(512)
                        val ret = `in`.read(buffer, 0, buffer.size)

                        if (ret > 0) {
                            val arrayMsg = CharArray(ret)
                            System.arraycopy(buffer, 0, arrayMsg, 0, arrayMsg.size)
                            val inBuffer = String(arrayMsg)
                            val outBuffer = parseJSON(inBuffer)
                            out.write(outBuffer, 0, outBuffer!!.length)
                            out.newLine()
                            out.flush()
                        }
                    }
                } catch (e: SocketException) {
                    if (e.message?.contains("Socket closed") == true) {
                        Log.d(TAG, "Server socket closed, exiting loop")
                        break
                    } else {
                        Log.d(TAG, "Error in client handling: $e")
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "Error in client handling: $e")
                }
            }
        } catch (e: IOException) {
            Log.d(TAG, "Error in server loop: $e")
        } finally {
            try {
                sss?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Error closing server socket: $e")
            }
        }
    }

    private fun parseJSON(msg: String): String? {
        var respMsg: String? = null

        try {
            val reader = JSONObject(msg)
            val key = reader.getString("key")
            if (key != "INTI") {
                return responseMSG(12, "Command not valid")
            }

            val cmd = reader.getString("command")
            when (cmd) {
                "setSAM" -> {
                    Log.d(TAG, "Set SAM")
                    val serial = reader.getString("pcid")
                    val part = reader.getString("configfile")
                    if (serial.isNotEmpty() && part.isNotEmpty()) {
                        db.setSAM(serial, part)
                        respMsg = responseMSG(0, "config SAM success")
                    } else {
                        respMsg = responseMSG(13, "data not valid")
                    }
                }

                "readIDENTITY" -> respMsg = readIdentity()

                "updateIDENTITY" -> {
                    Log.d(TAG, "update identity")
                    val serial = reader.getString("serialNumber")
                    val part = reader.getString("partNumber")
                    val code = reader.getString("companyCode")
                    db.setIdentity(serial, part, code, "")
                    respMsg = responseMSG(0, "update identity success")
                }

                "resetADMIN" -> {
                    for (i in 1..4) {
                        db.removeAdmin(i)
                    }
                    respMsg = responseMSG(0, "reset ADMIN success")
                }

                else -> respMsg = responseMSG(12, "Command not valid")
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error Parse : $e")
            respMsg = responseMSG(13, "data not valid")
        }

        return respMsg
    }

    private fun responseMSG(code: Int, desc: String): String {
        val jsonObj = JSONObject()
        return try {
            jsonObj.put("responseCode", code.toString())
            jsonObj.put("responseDesc", desc)
            jsonObj.toString()
        } catch (e: Exception) {
            ""
        }
    }

    private fun readIdentity(): String {
        val identity = db.readIdentity()
        val serial = identity[0]
        val part = identity[1]
        val code = identity[2]
        val sam = db.readSAM()
        val pcidbase = sam[0]
        val pcid = Base64.decode(pcidbase, 0)
        val pcidstr = Hex.bytesToHexString(pcid, 0, 16)
        val jsonresp = JSONObject()

        return try {
            jsonresp.put("serialNumber", serial)
            jsonresp.put("partNumber", part)
            jsonresp.put("companyCode", code)
            jsonresp.put("samId", pcidstr)
            jsonresp.put("appVer", "1.0")
            jsonresp.put("responseCode", "0")
            jsonresp.put("responseDesc", "Read Identity OK")
            jsonresp.toString()
        } catch (e: Exception) {
            ""
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        try {
            sss?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing server socket: $e")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // Not a bound service
    }

    companion object {
        private const val TAG = "TcpService"
    }
}
