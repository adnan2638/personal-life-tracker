package com.adnan.personallifetracker.feature.tasks.domain

import com.adnan.personallifetracker.feature.tasks.domain.model.CompletionTiming
import com.adnan.personallifetracker.feature.tasks.domain.model.TaskStatus

object TaskValidation {
    fun validateDetails(title: String, startAt: Long?, deadlineAt: Long?, estimatedDurationMinutes: Int?) {
        require(title.isNotBlank()) { "Task title cannot be blank." }
        require(startAt == null || deadlineAt == null || startAt <= deadlineAt) {
            "Task start time cannot be after its deadline."
        }
        require(estimatedDurationMinutes == null || estimatedDurationMinutes > 0) {
            "Estimated duration must be positive when supplied."
        }
    }

    fun validateCompletion(status: TaskStatus, timing: CompletionTiming?, completedAt: Long?) {
        if (status == TaskStatus.COMPLETED) {
            require(timing != null && completedAt != null) { "Completed tasks require timing and completion time." }
        } else {
            require(timing == null && completedAt == null) { "Only completed tasks can have completion timing." }
        }
    }
}
