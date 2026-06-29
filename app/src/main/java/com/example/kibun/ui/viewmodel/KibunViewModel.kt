package com.example.kibun.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kibun.data.KibunEntry
import com.example.kibun.data.KibunPlan
import com.example.kibun.data.KibunRepository
import com.example.kibun.data.ThemePreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class KibunViewModel(
    private val repository: KibunRepository,
    private val themePreferences: ThemePreferences
) : ViewModel() {

    companion object {
        private const val TAG = "KibunViewModel"
    }

    // テーマ設定（DataStoreで永続化）
    val themeOverride: StateFlow<String?> = themePreferences.themeOverride.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun setThemeOverride(theme: String?) {
        viewModelScope.launch {
            try {
                themePreferences.setThemeOverride(theme)
            } catch (e: Exception) {
                Log.e(TAG, "テーマ設定の保存に失敗しました", e)
            }
        }
    }

    // すべてのエントリをStateFlowとして提供
    val allEntries: StateFlow<List<KibunEntry>> = repository.allEntries.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // すべての予定をStateFlowとして提供
    val allPlans: StateFlow<List<KibunPlan>> = repository.allPlans.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insert(entry: KibunEntry) = viewModelScope.launch {
        try {
            repository.insert(entry)
        } catch (e: Exception) {
            Log.e(TAG, "日記の保存に失敗しました", e)
        }
    }

    fun update(entry: KibunEntry) = viewModelScope.launch {
        try {
            repository.update(entry)
        } catch (e: Exception) {
            Log.e(TAG, "日記の更新に失敗しました", e)
        }
    }

    fun delete(entry: KibunEntry) = viewModelScope.launch {
        try {
            repository.delete(entry)
        } catch (e: Exception) {
            Log.e(TAG, "日記の削除に失敗しました", e)
        }
    }

    fun toggleFavorite(entry: KibunEntry) = viewModelScope.launch {
        try {
            val updatedEntry = entry.copy(isFavorite = !entry.isFavorite)
            repository.update(updatedEntry)
        } catch (e: Exception) {
            Log.e(TAG, "お気に入りの切り替えに失敗しました", e)
        }
    }

    // Plans
    fun insertPlan(plan: KibunPlan) = viewModelScope.launch {
        try {
            repository.insertPlan(plan)
        } catch (e: Exception) {
            Log.e(TAG, "予定の保存に失敗しました", e)
        }
    }

    fun updatePlan(plan: KibunPlan) = viewModelScope.launch {
        try {
            repository.updatePlan(plan)
        } catch (e: Exception) {
            Log.e(TAG, "予定の更新に失敗しました", e)
        }
    }

    fun deletePlan(plan: KibunPlan) = viewModelScope.launch {
        try {
            repository.deletePlan(plan)
        } catch (e: Exception) {
            Log.e(TAG, "予定の削除に失敗しました", e)
        }
    }
}

class KibunViewModelFactory(
    private val repository: KibunRepository,
    private val themePreferences: ThemePreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KibunViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KibunViewModel(repository, themePreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

