package com.example.kibun.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kibun.data.KibunEntry
import com.example.kibun.data.KibunPlan
import com.example.kibun.data.KibunRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class KibunViewModel(private val repository: KibunRepository) : ViewModel() {

    private val _themeOverride = MutableStateFlow<String?>(null)
    val themeOverride = _themeOverride.asStateFlow()

    fun setThemeOverride(theme: String?) {
        _themeOverride.value = theme
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
        repository.insert(entry)
    }

    fun update(entry: KibunEntry) = viewModelScope.launch {
        repository.update(entry)
    }

    fun delete(entry: KibunEntry) = viewModelScope.launch {
        repository.delete(entry)
    }

    fun toggleFavorite(entry: KibunEntry) = viewModelScope.launch {
        val updatedEntry = entry.copy(isFavorite = !entry.isFavorite)
        repository.update(updatedEntry)
    }

    // Plans
    fun insertPlan(plan: KibunPlan) = viewModelScope.launch {
        repository.insertPlan(plan)
    }

    fun updatePlan(plan: KibunPlan) = viewModelScope.launch {
        repository.updatePlan(plan)
    }

    fun deletePlan(plan: KibunPlan) = viewModelScope.launch {
        repository.deletePlan(plan)
    }
}

class KibunViewModelFactory(private val repository: KibunRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KibunViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KibunViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
