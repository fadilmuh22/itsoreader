package com.example.itsotest.utils

import android.content.Context
import androidx.activity.ComponentActivity
import net.inti.intiektplib.EktpReaderInti
import java.lang.ref.WeakReference

object EktpReaderIntiSingleton {
    private var instance: EktpReaderInti? = null
    private var activityReference: WeakReference<ComponentActivity>? = null

    fun getInstance(activity: ComponentActivity): EktpReaderInti {
        if (instance == null || activityReference?.get() != activity) {
            activityReference = WeakReference(activity)
            instance = EktpReaderInti(activity)
        }
        return instance!!
    }
}
