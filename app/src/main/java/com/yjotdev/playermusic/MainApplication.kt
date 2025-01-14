package com.yjotdev.playermusic

import android.app.Application
import androidx.room.Room
import com.yjotdev.playermusic.data.PlayListDatabase

class MainApplication: Application() {
    companion object{
        lateinit var database: PlayListDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            PlayListDatabase::class.java,
            PlayListDatabase.NAME
        ).build()
    }
}