package com.yjotdev.playermusic.infrastructure.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Binds
import javax.inject.Singleton
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import com.yjotdev.playermusic.domain.port.ConfigPort
import com.yjotdev.playermusic.domain.port.PlayListPort
import com.yjotdev.playermusic.domain.port.ArtistListPort
import com.yjotdev.playermusic.domain.port.MediaPlayerPort
import com.yjotdev.playermusic.domain.port.PlayerStatePort
import com.yjotdev.playermusic.infrastructure.datasource.dao.PlayListDao
import com.yjotdev.playermusic.infrastructure.datasource.database.PlayListDatabase
import com.yjotdev.playermusic.infrastructure.repositories.ArtistListRepository
import com.yjotdev.playermusic.infrastructure.repositories.ConfigRepository
import com.yjotdev.playermusic.infrastructure.repositories.PlayListRepository
import com.yjotdev.playermusic.infrastructure.repositories.MediaPlayerRepository
import com.yjotdev.playermusic.infrastructure.repositories.PlayerStateRepository

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class DiModules {
    @Binds
    @Singleton
    abstract fun bindPlayListRepository(
        impl: PlayListRepository
    ): PlayListPort

    @Binds
    @Singleton
    abstract fun bindArtistListRepository(
        impl: ArtistListRepository
    ): ArtistListPort

    @Binds
    @Singleton
    abstract fun bindConfigRepository(
        impl: ConfigRepository
    ): ConfigPort

    @Binds
    @Singleton
    abstract fun bindMediaPlayerRepository(
        impl: MediaPlayerRepository
    ): MediaPlayerPort

    @Binds
    @Singleton
    abstract fun bindPlayerStateRepository(
        impl: PlayerStateRepository
    ): PlayerStatePort

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): PlayListDatabase =
            Room.databaseBuilder(
                context,
                PlayListDatabase::class.java,
                PlayListDatabase.NAME
            ).build()

        @Provides
        @Singleton
        fun providePlayListDao(database: PlayListDatabase): PlayListDao =
            database.playListDao()
    }
}