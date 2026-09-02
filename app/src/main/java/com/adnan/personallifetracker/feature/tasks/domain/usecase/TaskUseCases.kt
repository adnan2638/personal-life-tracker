package com.adnan.personallifetracker.feature.tasks.domain.usecase

import com.adnan.personallifetracker.feature.tasks.domain.TaskValidation
import com.adnan.personallifetracker.feature.tasks.domain.model.CompletionTiming
import com.adnan.personallifetracker.feature.tasks.domain.model.CreateTaskCommand
import com.adnan.personallifetracker.feature.tasks.domain.model.Task
import com.adnan.personallifetracker.feature.tasks.domain.model.TaskStatus
import com.adnan.personallifetracker.feature.tasks.domain.model.UpdateTaskCommand
import com.adnan.personallifetracker.feature.tasks.domain.repository.TaskRepository
import java.util.UUID
import kotlinx.coroutines.flow.Flow

class CreateTaskUseCase(private val repository: TaskRepository, private val now: () -> Long = System::currentTimeMillis) {
    suspend operator fun invoke(command: CreateTaskCommand): Task {
        TaskValidation.validateDetails(command.title, command.startAt, command.deadlineAt, command.estimatedDurationMinutes)
        require(command.categoryId == null || repository.isActiveCategory(command.categoryId)) { "Task category must be active." }
        val timestamp = now()
        val task = Task(UUID.randomUUID().toString(), command.title.trim(), command.notes?.trim()?.ifBlank { null }, command.categoryId, command.date, command.startAt, command.deadlineAt, command.priority, command.estimatedDurationMinutes, TaskStatus.PLANNED, null, null, timestamp, timestamp, null)
        repository.create(task)
        return task
    }
}

class UpdateTaskUseCase(private val repository: TaskRepository, private val now: () -> Long = System::currentTimeMillis) {
    suspend operator fun invoke(command: UpdateTaskCommand): Task {
        TaskValidation.validateDetails(command.title, command.startAt, command.deadlineAt, command.estimatedDurationMinutes)
        val existing = requireNotNull(repository.findTask(command.id)) { "Task does not exist." }
        require(existing.deletedAt == null) { "Archived tasks cannot be edited." }
        require(command.categoryId == null || repository.isActiveCategory(command.categoryId)) { "Task category must be active." }
        val task = existing.copy(title = command.title.trim(), notes = command.notes?.trim()?.ifBlank { null }, categoryId = command.categoryId, date = command.date, startAt = command.startAt, deadlineAt = command.deadlineAt, priority = command.priority, estimatedDurationMinutes = command.estimatedDurationMinutes, updatedAt = now())
        repository.update(task)
        return task
    }
}

class CompleteTaskUseCase(private val repository: TaskRepository, private val now: () -> Long = System::currentTimeMillis) {
    suspend operator fun invoke(id: String): Task {
        val task = requireNotNull(repository.findTask(id)) { "Task does not exist." }
        require(task.deletedAt == null) { "Archived tasks cannot be completed." }
        val completedAt = now()
        val timing = if (task.deadlineAt == null || completedAt <= task.deadlineAt) CompletionTiming.ON_TIME else CompletionTiming.LATE
        val completedTask = task.copy(status = TaskStatus.COMPLETED, completionTiming = timing, completedAt = completedAt, updatedAt = completedAt)
        repository.update(completedTask)
        return completedTask
    }
}

class MarkTaskPartialUseCase(private val repository: TaskRepository, private val now: () -> Long = System::currentTimeMillis) {
    suspend operator fun invoke(id: String): Task {
        val task = requireNotNull(repository.findTask(id)) { "Task does not exist." }
        require(task.deletedAt == null) { "Archived tasks cannot be changed." }
        val partialTask = task.copy(status = TaskStatus.PARTIAL, completionTiming = null, completedAt = null, updatedAt = now())
        repository.update(partialTask)
        return partialTask
    }
}

class ArchiveTaskUseCase(private val repository: TaskRepository, private val now: () -> Long = System::currentTimeMillis) { suspend operator fun invoke(id: String) = repository.archive(id, now()) }
class ObserveTasksUseCase(private val repository: TaskRepository) { operator fun invoke(): Flow<List<Task>> = repository.observeActiveTasks() }
