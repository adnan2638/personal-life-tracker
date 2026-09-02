package com.adnan.personallifetracker.feature.tasks.domain.repository

import com.adnan.personallifetracker.feature.tasks.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeActiveTasks(): Flow<List<Task>>
    suspend fun findTask(id: String): Task?
    suspend fun create(task: Task)
    suspend fun update(task: Task)
    suspend fun archive(id: String, archivedAt: Long)
    suspend fun isActiveCategory(id: String): Boolean
}
