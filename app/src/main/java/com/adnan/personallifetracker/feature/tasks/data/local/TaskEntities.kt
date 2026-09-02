package com.adnan.personallifetracker.feature.tasks.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.adnan.personallifetracker.feature.tasks.domain.model.CompletionTiming
import com.adnan.personallifetracker.feature.tasks.domain.model.TaskStatus
import com.adnan.personallifetracker.feature.tasks.domain.model.TaskPriority

@Entity(tableName = "task_category", indices = [Index(value = ["activeName"], unique = true)])
data class TaskCategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    /** Lower-cased active name; null once archived to permit a future category with the old name. */
    val activeName: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val archivedAt: Long?,
)

@Entity(
    tableName = "task",
    foreignKeys = [ForeignKey(entity = TaskCategoryEntity::class, parentColumns = ["id"], childColumns = ["categoryId"], onDelete = ForeignKey.NO_ACTION)],
    indices = [Index("categoryId"), Index("deadlineAt"), Index("deletedAt")],
)
data class TaskEntity(
    @PrimaryKey val id: String,
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
