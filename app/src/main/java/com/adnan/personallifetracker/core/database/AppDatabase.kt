package com.adnan.personallifetracker.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.adnan.personallifetracker.feature.tasks.data.local.TaskCategoryDao
import com.adnan.personallifetracker.feature.tasks.data.local.TaskCategoryEntity
import com.adnan.personallifetracker.feature.tasks.data.local.TaskDao
import com.adnan.personallifetracker.feature.tasks.data.local.TaskEntity

@Database(entities = [TaskEntity::class, TaskCategoryEntity::class], version = 1, exportSchema = true)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun taskCategoryDao(): TaskCategoryDao
}
