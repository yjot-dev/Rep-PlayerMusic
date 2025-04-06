package com.yjotdev.playermusic.infrastructure.adapter

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.port.PlayListRepository
import com.yjotdev.playermusic.domain.utils.Validation

@Database(entities = [MusicListEntity::class], version = 1, exportSchema = false)
@TypeConverters(Validation::class)
abstract class PlayListDatabase: RoomDatabase() {

    companion object {
        const val NAME = "bd_playlist"
    }

    abstract fun playListDao(): PlayListRepository
}