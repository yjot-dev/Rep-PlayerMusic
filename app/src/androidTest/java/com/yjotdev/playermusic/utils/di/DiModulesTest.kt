package com.yjotdev.playermusic.utils.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dagger.Binds
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import com.yjotdev.playermusic.domain.repository.ArtistListRepository
import com.yjotdev.playermusic.domain.repository.ConfigRepository
import com.yjotdev.playermusic.domain.repository.MediaPlayerRepository
import com.yjotdev.playermusic.domain.repository.PlaylistRepository
import com.yjotdev.playermusic.domain.repository.PlayerStateRepository
import com.yjotdev.playermusic.data.local.dao.PlaylistDao
import com.yjotdev.playermusic.data.local.database.PlaylistDatabase
import com.yjotdev.playermusic.data.di.DiModules
import com.yjotdev.playermusic.utils.repositories.FakeArtistListRepositoryImpl
import com.yjotdev.playermusic.utils.repositories.FakeConfigRepositoryImpl
import com.yjotdev.playermusic.utils.repositories.FakeMediaPlayerRepositoryImpl
import com.yjotdev.playermusic.utils.repositories.FakePlaylistRepositoryImpl
import com.yjotdev.playermusic.utils.repositories.FakePlayerStateRepositoryImpl

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DiModules::class] // Nombre del módulo real
)
@Suppress("unused")
abstract class DiModulesTest {
    @Binds
    @Singleton
    abstract fun bindFakePlayListRepository(
        impl: FakePlaylistRepositoryImpl
    ): PlaylistRepository

    @Binds
    @Singleton
    abstract fun bindFakeArtistListRepository(
        impl: FakeArtistListRepositoryImpl
    ): ArtistListRepository

    @Binds
    @Singleton
    abstract fun bindFakeConfigRepository(
        impl: FakeConfigRepositoryImpl
    ): ConfigRepository

    @Binds
    @Singleton
    abstract fun bindFakeMediaPlayerRepository(
        impl: FakeMediaPlayerRepositoryImpl
    ): MediaPlayerRepository

    @Binds
    @Singleton
    abstract fun bindFakePlayerStateRepository(
        impl: FakePlayerStateRepositoryImpl
    ): PlayerStateRepository

    companion object {
        @Provides
        @Singleton
        fun provideFakeDatabase(@ApplicationContext context: Context): PlaylistDatabase =
            Room.inMemoryDatabaseBuilder(
                context,
                PlaylistDatabase::class.java
            ).build()

        @Provides
        @Singleton
        fun provideFakePlayListDao(database: PlaylistDatabase): PlaylistDao =
            database.playListDao()
    }
}