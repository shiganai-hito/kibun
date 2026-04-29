package com.example.kibun.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [KibunEntry::class],
    version = 2,
    exportSchema = false
)
abstract class KibunDatabase : RoomDatabase() {
    abstract fun kibunDao(): KibunDao

    companion object {
        @Volatile
        private var INSTANCE: KibunDatabase? = null

        fun getInstance(context: Context): KibunDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    KibunDatabase::class.java,
                    "kibun.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}
