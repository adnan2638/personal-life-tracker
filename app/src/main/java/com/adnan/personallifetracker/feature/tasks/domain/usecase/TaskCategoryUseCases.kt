package com.adnan.personallifetracker.feature.tasks.domain.usecase

import com.adnan.personallifetracker.feature.tasks.domain.model.TaskCategory
import com.adnan.personallifetracker.feature.tasks.domain.repository.TaskCategoryRepository
import java.util.UUID
import kotlinx.coroutines.flow.Flow

class CreateTaskCategoryUseCase(private val repository: TaskCategoryRepository, private val now: () -> Long = System::currentTimeMillis) {
    suspend operator fun invoke(name: String): TaskCategory {
        require(name.isNotBlank()) { "Category name cannot be blank." }
        val timestamp = now()
        val category = TaskCategory(UUID.randomUUID().toString(), name.trim(), timestamp, timestamp, null)
        repository.create(category)
        return category
    }
}
class UpdateTaskCategoryUseCase(private val repository: TaskCategoryRepository, private val now: () -> Long = System::currentTimeMillis) {
    suspend operator fun invoke(id: String, name: String): TaskCategory {
        require(name.isNotBlank()) { "Category name cannot be blank." }
        val category = requireNotNull(repository.findCategory(id)) { "Category does not exist." }
        require(category.archivedAt == null) { "Archived categories cannot be edited." }
        val updatedCategory = category.copy(name = name.trim(), updatedAt = now())
        repository.update(updatedCategory)
        return updatedCategory
    }
}
class ArchiveTaskCategoryUseCase(private val repository: TaskCategoryRepository, private val now: () -> Long = System::currentTimeMillis) { suspend operator fun invoke(id: String) = repository.archive(id, now()) }
class ObserveTaskCategoriesUseCase(private val repository: TaskCategoryRepository) { operator fun invoke(): Flow<List<TaskCategory>> = repository.observeActiveCategories() }
