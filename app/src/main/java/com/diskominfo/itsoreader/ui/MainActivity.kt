package com.diskominfo.itsoreader.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.diskominfo.itsoreader.data.SocketService
import com.diskominfo.itsoreader.data.remote.SocketManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val permissionRequestCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen()
        }

        SocketService.startService(this)
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,
            Manifest.permission.NFC,
            Manifest.permission.TRANSMIT_IR,
            Manifest.permission.UWB_RANGING
        )

        val permissionsNeeded = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded, permissionRequestCode)
        } else {
            SocketService.startService(this)
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val socketManager = remember { SocketManager() }
    val scope = rememberCoroutineScope()

    var messageToSend by remember { mutableStateOf("") }

    // Connect to the socket when the composable is first launched
    LaunchedEffect(Unit) {
        socketManager.connect()
    }

    // Disconnect from the socket when the composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            socketManager.disconnect()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Text field for sending messages
        OutlinedTextField(
            value = messageToSend,
            onValueChange = { messageToSend = it },
            label = { Text("Enter your message") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to send a message to the server
        Button(
            onClick = {
                scope.launch {
                    socketManager.sendMessage(messageToSend)
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Send")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to go to the ListActivity to view messages
        Button(
            onClick = {
                val intent = Intent(context, ListActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("View Messages")
        }
    }
}