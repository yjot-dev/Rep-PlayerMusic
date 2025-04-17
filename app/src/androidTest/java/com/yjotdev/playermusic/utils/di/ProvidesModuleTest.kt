package com.yjotdev.playermusic.utils.di

import android.content.Context
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.testing.TestInstallIn
import com.yjotdev.playermusic.domain.port.ConfigRepository
import com.yjotdev.playermusic.domain.port.PlayListRepository
import com.yjotdev.playermusic.infrastructure.adapter.PlayListDatabase
import com.yjotdev.playermusic.infrastructure.di.ProvidesModule
import com.yjotdev.playermusic.utils.repositories.FakeConfigRepositoryImpl
import com.yjotdev.playermusic.utils.repositories.FakePlayListRepositoryImpl

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ProvidesModule::class] // Nombre del módulo real
)
object ProvidesModuleTest {
    @Singleton
    @Provides
    fun provideFakeDatabase(@ApplicationContext context: Context): PlayListDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            PlayListDatabase::class.java
        ).build()
    }

    @Singleton
    @Provides
    fun provideFakePlayListRepositoryImpl(database: PlayListDatabase): PlayListRepository =
        FakePlayListRepositoryImpl(database)

    @Singleton
    @Provides
    fun provideFakeConfigRepositoryImpl(): ConfigRepository =
        FakeConfigRepositoryImpl()

    @Singleton
    @Provides
    fun provideTestNavHostController(@ApplicationContext context: Context) =
        TestNavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
        }
}