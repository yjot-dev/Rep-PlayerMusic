package com.yjotdev.playermusic.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yjotdev.playermusic.domain.model.MusicModel

/**
 * TypeConverter para que Room pueda persistir una lista de MusicEntity.
 * Pertenece a la capa de Infraestructura.
 */
@Suppress("unused")
class MusicListConverter {
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