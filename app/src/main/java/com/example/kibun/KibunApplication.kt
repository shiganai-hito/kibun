package com.example.kibun

import android.app.Application
import com.example.kibun.data.KibunDatabase
import com.example.kibun.data.KibunRepository

class KibunApplication : Application() {
    val database by lazy { KibunDatabase.getInstance(this) }
    val repository by lazy { KibunRepository(database.kibunDao()) }
}
