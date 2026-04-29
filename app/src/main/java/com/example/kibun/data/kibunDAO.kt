package com.example.kibun.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface KibunDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: KibunEntry)

    @Update
    suspend fun update(entry: KibunEntry)

    @Delete
    suspend fun delete(entry: KibunEntry)

    @Query("SELECT * FROM kibun_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<KibunEntry>>

    @Query("SELECT * FROM kibun_entries WHERE id = :id")
    fun getEntryById(id: Int): Flow<KibunEntry?>

    @Query(
        "SELECT * FROM kibun_entries " +
            "WHERE date >= :start AND date < :end " +
            "ORDER BY date ASC"
    )
    fun getEntriesForRange(start: Long, end: Long): Flow<List<KibunEntry>>
}
