package com.adnan.personallifetracker.feature.tasks.domain.model

data class Task(
    val id: String,
    val title: String,
    val notes: String?,
    val categoryId: String?,
    val date: String,
    val startAt: Long?,
    val deadlineAt: Long?,
    val priority: TaskPriority,
    val estimatedDurationMinutes: Int?,
    val status: TaskStatus,
    val completionTiming: CompletionTiming?,
    val completedAt: Long?,
    val createdAt: Long,
    val updatedAt: Long,
    val deletedAt: Long?,
)
