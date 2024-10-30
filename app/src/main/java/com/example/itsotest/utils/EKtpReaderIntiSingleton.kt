package com.example.itsotest.utils

import android.content.Context
import androidx.activity.ComponentActivity
import net.inti.intiektplib.EktpReaderInti

object EktpReaderIntiSingleton {
    private var instance: EktpReaderInti? = null

    fun getInstance(activity: ComponentActivity): EktpReaderInti {
        if (instance == null) {
            instance = EktpReaderInti(activity)
        }
        return instance!!
    }
}