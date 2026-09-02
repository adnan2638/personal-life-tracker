package com.adnan.personallifetracker.core.di

import android.content.Context
import androidx.room.Room
import com.adnan.personallifetracker.core.database.AppDatabase
import com.adnan.personallifetracker.feature.tasks.data.local.TaskCategoryDao
import com.adnan.personallifetracker.feature.tasks.data.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton fun provideDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "personal-life-tracker.db").build()
    @Provides fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()
    @Provides fun provideTaskCategoryDao(database: AppDatabase): TaskCategoryDao = database.taskCategoryDao()
}
