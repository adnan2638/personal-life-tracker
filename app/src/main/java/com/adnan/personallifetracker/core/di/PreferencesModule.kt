package com.adnan.personallifetracker.core.di

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.appPreferencesDataStore by preferencesDataStore(name = "app_preferences")

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {
    @Provides @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.appPreferencesDataStore
}
