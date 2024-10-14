package com.diskominfo.itsoreader.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diskominfo.itsoreader.data.model.EktpReadModel
import com.google.gson.Gson

class NewDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val welcomeJson = intent.getStringExtra("welcomeData")
        val welcomeData = Gson().fromJson(welcomeJson, EktpReadModel::class.java)

        setContent {
            NewDataScreen(welcomeData = welcomeData)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDataScreen(welcomeData: EktpReadModel) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("New Data Received") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            WelcomeCard(welcome = welcomeData)

        }
    }
}

@Composable
fun WelcomeCard(welcome: EktpReadModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${welcome.namaLengkap}", style = MaterialTheme.typography.headlineSmall)
            Text(text = "NIK: ${welcome.nik}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Gender: ${welcome.jenisKelamin}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Birthplace: ${welcome.tempatLahir}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Birthdate: ${welcome.tanggalLahir}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Religion: ${welcome.agama}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Marital Status: ${welcome.statusKawin}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Job Type: ${welcome.jenisPekerjaan}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Province: ${welcome.namaProvinsi}", style = MaterialTheme.typography.bodySmall)
            Text(text = "District: ${welcome.namaKabupaten}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Sub-district: ${welcome.namaKecamatan}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Village: ${welcome.namaKelurahan}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Address: ${welcome.alamat}", style = MaterialTheme.typography.bodySmall)
            Text(text = "RT/RW: ${welcome.nomorRt}/${welcome.nomorRw}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Valid Until: ${welcome.berlakuHingga}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Blood Type: ${welcome.golonganDarah}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nationality: ${welcome.kewarganegaraan}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Photo URL: ${welcome.foto}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Signature: ${welcome.ttd}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Finger Auth: ${welcome.fingerAuth}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Index1: ${welcome.index1}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Index2: ${welcome.index2}", style = MaterialTheme.typography.bodySmall)
            Text(text = "TID: ${welcome.tid ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
        }
    }
}