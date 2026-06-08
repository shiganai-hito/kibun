package com.example.kibun.data

import kotlinx.coroutines.flow.Flow

class KibunRepository(private val kibunDao: KibunDao) {
    val allEntries: Flow<List<KibunEntry>> = kibunDao.getAllEntries()

    suspend fun insert(entry: KibunEntry) {
        kibunDao.insert(entry)
    }

    suspend fun update(entry: KibunEntry) {
        kibunDao.update(entry)
    }

    suspend fun delete(entry: KibunEntry) {
        kibunDao.delete(entry)
    }

    fun getEntryById(id: Int): Flow<KibunEntry?> {
        return kibunDao.getEntryById(id)
    }

    fun getEntriesForRange(start: Long, end: Long): Flow<List<KibunEntry>> {
        return kibunDao.getEntriesForRange(start, end)
    }

    // Plans
    val allPlans: Flow<List<KibunPlan>> = kibunDao.getAllPlans()

    suspend fun insertPlan(plan: KibunPlan) {
        kibunDao.insertPlan(plan)
    }

    suspend fun updatePlan(plan: KibunPlan) {
        kibunDao.updatePlan(plan)
    }

    suspend fun deletePlan(plan: KibunPlan) {
        kibunDao.deletePlan(plan)
    }

    fun getPlansForRange(start: Long, end: Long): Flow<List<KibunPlan>> {
        return kibunDao.getPlansForRange(start, end)
    }
}
