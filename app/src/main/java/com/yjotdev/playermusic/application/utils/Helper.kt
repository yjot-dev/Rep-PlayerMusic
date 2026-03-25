package com.yjotdev.playermusic.application.utils

import android.content.Context
import androidx.core.net.toUri
import com.yjotdev.playermusic.R

/**
 * Contiene funciones de utilidad relacionadas con la presentación de datos en la UI.
 * Pertenece a la capa de Application.
 */
object Helper {
    /** Valida si el nombre de la playlist es válido **/
    fun isValidWordsAndNumbers(input: String): Boolean{
        return Regex("^[A-Za-z0-9 ]+\$").matches(input)
    }

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
        val albumArtExists =
            try{
                applicationContext.contentResolver.openInputStream(albumImageUri.toUri())?.close()
                true
            }
            catch (_: Exception) { false }
        return if(albumArtExists){ albumImageUri }
               else{ getDefaultAlbumUri(applicationContext) }
    }

    private fun getDefaultAlbumUri(context: Context): String {
        return ("android.resource://" +
                "${context.packageName}/" +
                "${R.drawable.album_48}").toUri().toString()
    }
}