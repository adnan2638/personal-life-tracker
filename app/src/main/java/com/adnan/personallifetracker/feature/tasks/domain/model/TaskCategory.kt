package com.adnan.personallifetracker.feature.tasks.domain.model

data class TaskCategory(
    val id: String,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
    val archivedAt: Long?,
)
