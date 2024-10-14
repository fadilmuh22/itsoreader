package com.diskominfo.itsoreader.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diskominfo.itsoreader.data.model.EktpReadModel
import com.diskominfo.itsoreader.data.remote.SocketManager
import com.diskominfo.itsoreader.ui.theme.ItSoReaderTheme

class ListActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ItSoReaderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    MessageListScreen()
                }
            }
        }


    }
}

@Composable
fun MessageListScreen() {
    val socketManager = remember { SocketManager() }
    val messageList =  remember { mutableStateListOf<EktpReadModel>() }

    // Connect to the socket when the composable is first launched
    LaunchedEffect(Unit) {
        socketManager.connect()

        // Set listener for messages received from the socket
        socketManager.onMessageReceived { messages ->
            messageList.clear()
            messageList.addAll(messages)
            messageList.toList()
        //            Log.d("ListActivity", "Received messages: ${messageList.toList()}")
        }

        socketManager.onNewDataReceived { message ->
            messageList.add(message)
        }
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
        Text(text = "Messages", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Displaying the list of messages using LazyColumn
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(messageList.size) { index ->
                EktpReadCard(messageList[index])
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun EktpReadCard(ektpRead: EktpReadModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${ektpRead.namaLengkap}", style = MaterialTheme.typography.headlineSmall)
            Text(text = "NIK: ${ektpRead.nik}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Gender: ${ektpRead.jenisKelamin}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Birthplace: ${ektpRead.tempatLahir}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Birthdate: ${ektpRead.tanggalLahir}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Religion: ${ektpRead.agama}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Marital Status: ${ektpRead.statusKawin}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Job Type: ${ektpRead.jenisPekerjaan}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Province: ${ektpRead.namaProvinsi}", style = MaterialTheme.typography.bodySmall)
            Text(text = "District: ${ektpRead.namaKabupaten}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Sub-district: ${ektpRead.namaKecamatan}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Village: ${ektpRead.namaKelurahan}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Address: ${ektpRead.alamat}", style = MaterialTheme.typography.bodySmall)
            Text(text = "RT/RW: ${ektpRead.nomorRt}/${ektpRead.nomorRw}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Valid Until: ${ektpRead.berlakuHingga}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Blood Type: ${ektpRead.golonganDarah}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nationality: ${ektpRead.kewarganegaraan}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Photo URL: ${ektpRead.foto}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Signature: ${ektpRead.ttd}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Finger Auth: ${ektpRead.fingerAuth}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Index1: ${ektpRead.index1}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Index2: ${ektpRead.index2}", style = MaterialTheme.typography.bodySmall)
            Text(text = "TID: ${ektpRead.tid ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
        }
    }
}