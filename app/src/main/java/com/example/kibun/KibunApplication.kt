package com.example.kibun

import android.app.Application
import com.example.kibun.data.KibunDatabase
import com.example.kibun.data.KibunRepository
import com.example.kibun.data.ThemePreferences

class KibunApplication : Application() {
    val database by lazy { KibunDatabase.getInstance(this) }
    val repository by lazy { KibunRepository(database.kibunDao()) }
    val themePreferences by lazy { ThemePreferences(this) }
}

