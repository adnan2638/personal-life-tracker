package com.adnan.personallifetracker.feature.tasks.domain.model

data class CreateTaskCommand(
    val title: String,
    val notes: String? = null,
    val categoryId: String? = null,
    val date: String,
    val startAt: Long? = null,
    val deadlineAt: Long? = null,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val estimatedDurationMinutes: Int? = null,
)

data class UpdateTaskCommand(
    val id: String,
    val title: String,
    val notes: String? = null,
    val categoryId: String? = null,
    val date: String,
    val startAt: Long? = null,
    val deadlineAt: Long? = null,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val estimatedDurationMinutes: Int? = null,
)
