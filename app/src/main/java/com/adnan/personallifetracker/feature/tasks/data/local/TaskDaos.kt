package com.adnan.personallifetracker.feature.tasks.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE deletedAt IS NULL ORDER BY date ASC, createdAt ASC")
    fun observeActive(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun findById(id: String): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insert(task: TaskEntity)
    @Update suspend fun update(task: TaskEntity)
    @Query("UPDATE task SET deletedAt = :archivedAt, updatedAt = :archivedAt WHERE id = :id AND deletedAt IS NULL")
    suspend fun archive(id: String, archivedAt: Long)
}

@Dao
interface TaskCategoryDao {
    @Query("SELECT * FROM task_category WHERE archivedAt IS NULL ORDER BY name COLLATE NOCASE ASC")
    fun observeActive(): Flow<List<TaskCategoryEntity>>

    @Query("SELECT * FROM task_category WHERE id = :id")
    suspend fun findById(id: String): TaskCategoryEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insert(category: TaskCategoryEntity)
    @Update suspend fun update(category: TaskCategoryEntity)
    @Query("UPDATE task_category SET archivedAt = :archivedAt, updatedAt = :archivedAt, activeName = NULL WHERE id = :id AND archivedAt IS NULL")
    suspend fun archive(id: String, archivedAt: Long)
}
