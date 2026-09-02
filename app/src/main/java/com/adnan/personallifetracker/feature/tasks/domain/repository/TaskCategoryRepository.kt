package com.adnan.personallifetracker.feature.tasks.domain.repository

import com.adnan.personallifetracker.feature.tasks.domain.model.TaskCategory
import kotlinx.coroutines.flow.Flow

interface TaskCategoryRepository {
    fun observeActiveCategories(): Flow<List<TaskCategory>>
    suspend fun findCategory(id: String): TaskCategory?
    suspend fun create(category: TaskCategory)
    suspend fun update(category: TaskCategory)
    suspend fun archive(id: String, archivedAt: Long)
}
