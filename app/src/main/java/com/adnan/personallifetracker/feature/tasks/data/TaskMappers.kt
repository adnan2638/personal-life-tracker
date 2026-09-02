package com.adnan.personallifetracker.feature.tasks.data

import com.adnan.personallifetracker.feature.tasks.data.local.TaskCategoryEntity
import com.adnan.personallifetracker.feature.tasks.data.local.TaskEntity
import com.adnan.personallifetracker.feature.tasks.domain.model.Task
import com.adnan.personallifetracker.feature.tasks.domain.model.TaskCategory

internal fun Task.toEntity() = TaskEntity(id, title, notes, categoryId, date, startAt, deadlineAt, priority, estimatedDurationMinutes, status, completionTiming, completedAt, createdAt, updatedAt, deletedAt)
internal fun TaskEntity.toDomain() = Task(id, title, notes, categoryId, date, startAt, deadlineAt, priority, estimatedDurationMinutes, status, completionTiming, completedAt, createdAt, updatedAt, deletedAt)
internal fun TaskCategory.toEntity() = TaskCategoryEntity(id, name, name.trim().lowercase(), createdAt, updatedAt, archivedAt)
internal fun TaskCategoryEntity.toDomain() = TaskCategory(id, name, createdAt, updatedAt, archivedAt)
