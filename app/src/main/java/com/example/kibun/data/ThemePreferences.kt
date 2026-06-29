package com.example.kibun.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kibun_settings")

class ThemePreferences(private val context: Context) {

    private val themeKey = stringPreferencesKey("theme_override")

    val themeOverride: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[themeKey]
    }

    suspend fun setThemeOverride(theme: String?) {
        context.dataStore.edit { preferences ->
            if (theme == null) {
                preferences.remove(themeKey)
            } else {
                preferences[themeKey] = theme
            }
        }
    }
}
