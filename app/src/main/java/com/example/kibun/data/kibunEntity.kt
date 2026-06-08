package com.example.kibun.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kibun_entries")
data class KibunEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val date: Long,
    val mood: String = "",
    val category: String = "日常",
    val imageUri: String? = null,
    val isFavorite: Boolean = false
)

@Entity(tableName = "kibun_plans")
data class KibunPlan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val date: Long,
    val time: String = ""
)
