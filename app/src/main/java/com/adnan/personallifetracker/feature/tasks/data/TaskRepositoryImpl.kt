package com.adnan.personallifetracker.feature.tasks.data

import com.adnan.personallifetracker.feature.tasks.data.local.TaskCategoryDao
import com.adnan.personallifetracker.feature.tasks.data.local.TaskDao
import com.adnan.personallifetracker.feature.tasks.domain.model.Task
import com.adnan.personallifetracker.feature.tasks.domain.model.TaskCategory
import com.adnan.personallifetracker.feature.tasks.domain.repository.TaskCategoryRepository
import com.adnan.personallifetracker.feature.tasks.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomTaskRepository(private val taskDao: TaskDao, private val categoryDao: TaskCategoryDao) : TaskRepository {
    override fun observeActiveTasks(): Flow<List<Task>> = taskDao.observeActive().map { tasks -> tasks.map { it.toDomain() } }
    override suspend fun findTask(id: String): Task? = taskDao.findById(id)?.toDomain()
    override suspend fun create(task: Task) = taskDao.insert(task.toEntity())
    override suspend fun update(task: Task) = taskDao.update(task.toEntity())
    override suspend fun archive(id: String, archivedAt: Long) = taskDao.archive(id, archivedAt)
    override suspend fun isActiveCategory(id: String): Boolean = categoryDao.findById(id)?.archivedAt == null
}

class RoomTaskCategoryRepository(private val categoryDao: TaskCategoryDao) : TaskCategoryRepository {
    override fun observeActiveCategories(): Flow<List<TaskCategory>> = categoryDao.observeActive().map { categories -> categories.map { it.toDomain() } }
    override suspend fun findCategory(id: String): TaskCategory? = categoryDao.findById(id)?.toDomain()
    override suspend fun create(category: TaskCategory) = categoryDao.insert(category.toEntity())
    override suspend fun update(category: TaskCategory) = categoryDao.update(category.toEntity())
    override suspend fun archive(id: String, archivedAt: Long) = categoryDao.archive(id, archivedAt)
}
