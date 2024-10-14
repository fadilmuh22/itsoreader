package com.diskominfo.itsoreader.data.remote

import android.util.Log
import com.diskominfo.itsoreader.data.model.EktpReadModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import java.net.URISyntaxException

class SocketManager {
    companion object {
        val SOCKET_BASE_URL = ApiConfig.BASE_URL
        val tag = "SOCKETMANAGER"
    }

    private var socket: Socket? = null
    private val gson = Gson()

    init {
        try {
            socket = IO.socket(SOCKET_BASE_URL)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun connect() {
        socket?.connect()
    }

    fun disconnect() {
        socket?.disconnect()
    }

    fun isConnected(): Boolean {
        return socket?.connected() ?: false
    }

    /**
     * Listens for 'newData' events and parses the incoming JSON into a list of Welcome objects.
     */
    fun onMessageReceived(listener: (List<EktpReadModel>) -> Unit) {
        socket?.on("message") { args ->
            if (args.isNotEmpty()) {
                val data = args[0]
                val jsonString: String = when (data) {
                    is JSONArray -> data.toString()
                    else -> {
                        Log.e(tag, "Unsupported data type received: ${data::class.java}")
                        return@on
                    }
                }

                Log.d(tag, "Received JSON: $jsonString")

                try {
                    val listType = object : TypeToken<List<EktpReadModel>>() {}.type
                    val welcomeList: List<EktpReadModel> = gson.fromJson(jsonString, listType)
                    listener.invoke(welcomeList)
                } catch (e: Exception) {
                    Log.e(tag, "Gson parsing error: ${e.message}")
                }
            }
        }
    }

    fun onNewDataReceived(listener: (EktpReadModel) -> Unit) {
        socket?.on("newData") { args ->
            if (args.isNotEmpty()) {
                val json = args[0].toString()
                try {
                    val welcome = gson.fromJson(json, EktpReadModel::class.java)
                    listener.invoke(welcome)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun sendMessage(message: String) {
        socket?.emit("message", message)
    }
}