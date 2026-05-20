package com.yjotdev.playermusic.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yjotdev.playermusic.data.local.entity.MusicListEntity
import com.yjotdev.playermusic.data.local.converter.MusicListConverter
import com.yjotdev.playermusic.data.local.dao.PlaylistDao

@Database(entities = [MusicListEntity::class], version = 1, exportSchema = false)
@TypeConverters(MusicListConverter::class)
abstract class PlaylistDatabase: RoomDatabase() {

    companion object {
        const val NAME = "bd_playlist"
    }

    abstract fun playListDao(): PlaylistDao
}