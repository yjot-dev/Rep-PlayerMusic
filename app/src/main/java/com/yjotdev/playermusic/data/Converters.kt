package com.yjotdev.playermusic.data

import androidx.room.TypeConverter
import com.yjotdev.playermusic.ui.model.MusicModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromList(list: List<MusicModel>): String{
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(data: String): List<MusicModel> {
        val listType = object : TypeToken<List<MusicModel>>() {}.type
        return Gson().fromJson(data, listType)
    }
}