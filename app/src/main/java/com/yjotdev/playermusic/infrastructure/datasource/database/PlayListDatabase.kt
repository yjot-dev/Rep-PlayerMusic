package com.yjotdev.playermusic.infrastructure.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yjotdev.playermusic.infrastructure.datasource.model.MusicListModel
import com.yjotdev.playermusic.infrastructure.datasource.converters.MusicListConverter
import com.yjotdev.playermusic.infrastructure.datasource.dao.PlayListDao

@Database(entities = [MusicListModel::class], version = 1, exportSchema = false)
@TypeConverters(MusicListConverter::class)
abstract class PlayListDatabase: RoomDatabase() {

    companion object {
        const val NAME = "bd_playlist"
    }

    abstract fun playListDao(): PlayListDao
}