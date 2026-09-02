package com.adnan.personallifetracker.core.database

import androidx.room.TypeConverter
import com.adnan.personallifetracker.feature.tasks.domain.model.CompletionTiming
import com.adnan.personallifetracker.feature.tasks.domain.model.TaskStatus
import com.adnan.personallifetracker.feature.tasks.domain.model.TaskPriority

class DatabaseConverters {
    @TypeConverter fun taskStatusToString(value: TaskStatus): String = value.name
    @TypeConverter fun stringToTaskStatus(value: String): TaskStatus = TaskStatus.valueOf(value)
    @TypeConverter fun taskPriorityToString(value: TaskPriority): String = value.name
    @TypeConverter fun stringToTaskPriority(value: String): TaskPriority = TaskPriority.valueOf(value)
    @TypeConverter fun completionTimingToString(value: CompletionTiming?): String? = value?.name
    @TypeConverter fun stringToCompletionTiming(value: String?): CompletionTiming? = value?.let(CompletionTiming::valueOf)
}
