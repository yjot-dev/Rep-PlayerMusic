package com.yjotdev.playermusic.infrastructure.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.yjotdev.playermusic.domain.port.ConfigRepository
import com.yjotdev.playermusic.domain.port.PlayListRepository
import com.yjotdev.playermusic.infrastructure.adapter.PlayListDatabase
import com.yjotdev.playermusic.infrastructure.repositories.ConfigRepositoryImpl
import com.yjotdev.playermusic.infrastructure.repositories.PlayListRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object ProvidesModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PlayListDatabase {
        return Room.databaseBuilder(
            context,
            PlayListDatabase::class.java,
            PlayListDatabase.NAME
        ).build()
    }

    @Singleton
    @Provides
    fun providePlayListRepositoryImpl(database: PlayListDatabase): PlayListRepository =
        PlayListRepositoryImpl(database)

    @Singleton
    @Provides
    fun provideConfigRepositoryImpl(@ApplicationContext context: Context): ConfigRepository =
        ConfigRepositoryImpl(context)
}