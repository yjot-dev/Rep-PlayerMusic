package com.yjotdev.playermusic.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Binds
import javax.inject.Singleton
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import com.yjotdev.playermusic.domain.repository.ConfigRepository
import com.yjotdev.playermusic.domain.repository.PlaylistRepository
import com.yjotdev.playermusic.domain.repository.ArtistListRepository
import com.yjotdev.playermusic.domain.repository.MediaPlayerRepository
import com.yjotdev.playermusic.domain.repository.PlayerStateRepository
import com.yjotdev.playermusic.data.repository.ConfigRepositoryImpl
import com.yjotdev.playermusic.data.repository.PlaylistRepositoryImpl
import com.yjotdev.playermusic.data.repository.ArtistListRepositoryImpl
import com.yjotdev.playermusic.data.repository.MediaPlayerRepositoryImpl
import com.yjotdev.playermusic.data.repository.PlayerStateRepositoryImpl
import com.yjotdev.playermusic.data.local.dao.PlaylistDao
import com.yjotdev.playermusic.data.local.database.PlaylistDatabase

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class DiModules {
    @Binds
    @Singleton
    abstract fun bindPlayListRepository(
        impl: PlaylistRepositoryImpl
    ): PlaylistRepository

    @Binds
    @Singleton
    abstract fun bindArtistListRepository(
        impl: ArtistListRepositoryImpl
    ): ArtistListRepository

    @Binds
    @Singleton
    abstract fun bindConfigRepository(
        impl: ConfigRepositoryImpl
    ): ConfigRepository

    @Binds
    @Singleton
    abstract fun bindMediaPlayerRepository(
        impl: MediaPlayerRepositoryImpl
    ): MediaPlayerRepository

    @Binds
    @Singleton
    abstract fun bindPlayerStateRepository(
        impl: PlayerStateRepositoryImpl
    ): PlayerStateRepository

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): PlaylistDatabase =
            Room.databaseBuilder(
                context,
                PlaylistDatabase::class.java,
                PlaylistDatabase.NAME
            ).build()

        @Provides
        @Singleton
        fun providePlayListDao(database: PlaylistDatabase): PlaylistDao =
            database.playListDao()
    }
}