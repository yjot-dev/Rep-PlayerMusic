package com.yjotdev.playermusic.utils.di

import android.content.Context
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dagger.Binds
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import com.yjotdev.playermusic.domain.port.ArtistListPort
import com.yjotdev.playermusic.domain.port.ConfigPort
import com.yjotdev.playermusic.domain.port.MediaPlayerPort
import com.yjotdev.playermusic.domain.port.PlayListPort
import com.yjotdev.playermusic.infrastructure.datasource.dao.PlayListDao
import com.yjotdev.playermusic.infrastructure.datasource.database.PlayListDatabase
import com.yjotdev.playermusic.utils.repositories.FakeArtistListRepository
import com.yjotdev.playermusic.utils.repositories.FakeConfigRepository
import com.yjotdev.playermusic.utils.repositories.FakeMediaPlayerRepository
import com.yjotdev.playermusic.utils.repositories.FakePlayListRepository
import com.yjotdev.playermusic.infrastructure.di.DiModules

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
        impl: FakePlayListRepository
    ): PlayListPort

    @Binds
    @Singleton
    abstract fun bindFakeArtistListRepository(
        impl: FakeArtistListRepository
    ): ArtistListPort

    @Binds
    @Singleton
    abstract fun bindFakeConfigRepository(
        impl: FakeConfigRepository
    ): ConfigPort

    @Binds
    @Singleton
    abstract fun bindFakeMediaPlayerRepository(
        impl: FakeMediaPlayerRepository
    ): MediaPlayerPort

    companion object {
        @Provides
        @Singleton
        fun provideFakeDatabase(@ApplicationContext context: Context): PlayListDatabase =
            Room.inMemoryDatabaseBuilder(
                context,
                PlayListDatabase::class.java
            ).build()

        @Provides
        @Singleton
        fun provideFakePlayListDao(database: PlayListDatabase): PlayListDao =
            database.playListDao()

        @Provides
        @Singleton
        fun provideTestNavHostController(@ApplicationContext context: Context) =
            TestNavHostController(context).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
    }
}