package com.adnan.personallifetracker.feature.tasks.di

import com.adnan.personallifetracker.feature.tasks.data.RoomTaskCategoryRepository
import com.adnan.personallifetracker.feature.tasks.data.RoomTaskRepository
import com.adnan.personallifetracker.feature.tasks.data.local.TaskCategoryDao
import com.adnan.personallifetracker.feature.tasks.data.local.TaskDao
import com.adnan.personallifetracker.feature.tasks.domain.repository.TaskCategoryRepository
import com.adnan.personallifetracker.feature.tasks.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object TasksModule {
    @Provides @Singleton fun provideTaskRepository(taskDao: TaskDao, categoryDao: TaskCategoryDao): TaskRepository = RoomTaskRepository(taskDao, categoryDao)
    @Provides @Singleton fun provideCategoryRepository(categoryDao: TaskCategoryDao): TaskCategoryRepository = RoomTaskCategoryRepository(categoryDao)
}
