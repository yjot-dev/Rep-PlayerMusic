package com.yjotdev.playermusic.infrastructure.datasource.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yjotdev.playermusic.domain.entity.MusicEntity

/**
 * TypeConverter para que Room pueda persistir una lista de MusicEntity.
 * Pertenece a la capa de Infraestructura.
 */
class MusicListConverter {
    @TypeConverter
    fun fromList(list: List<MusicEntity>): String{
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(data: String): List<MusicEntity> {
        val listType = object : TypeToken<List<MusicEntity>>() {}.type
        return Gson().fromJson(data, listType)
    }
}