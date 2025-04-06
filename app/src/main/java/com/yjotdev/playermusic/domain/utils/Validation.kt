package com.yjotdev.playermusic.domain.utils

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.domain.entity.MusicEntity

object Validation {
    /** Convierte duración de Long a String formato minutos:segundos **/
    fun durationFormat(duration: Int) : String {
        var seconds = duration/1000
        val minutes = seconds/60
        seconds %= 60
        return if (seconds < 10) "$minutes:0$seconds"
        else "$minutes:$seconds"
    }

    /** Obtiene imagen del album o deja imagen por defecto **/
    fun getAlbumUri(
        applicationContext: Context,
        albumImageUri: String
    ) : String{
        val albumArtExists = try{
            applicationContext.contentResolver.openInputStream(albumImageUri.toUri())?.close()
            true
        }
        catch (e: Exception) { false }
        return if(albumArtExists){ albumImageUri }
        else{
            Uri.parse("android.resource://" +
                    "${applicationContext.packageName}/" +
                    "${R.drawable.album_48}").toString()
        }
    }

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