package com.yjotdev.playermusic.presentation.utils

/**
 * Constantes para identificar los componentes en los tests instrumentales.
 */
object TestTags {
    // Etiquetas estáticas
    const val CONFIRM = "Confirm"
    const val CHANGE_NAME_FROM_PLAYLIST = "ChangeNameFromPlaylist"
    const val SEARCH_PLAYLIST = "searchPlayList"
    const val ADD_PLAYLIST = "addPlayList"
    const val SLIDER = "slider"

    // Etiquetas dinámicas (prefijos con ":")
    const val SELECT_SONG_CHECKBOX = "selectSongCheckbox:Song"
    const val EDIT_NAME_PLAYLIST = "editNamePlaylist:0"
    const val REMOVE_PLAYLIST = "removePlaylist:0"
    const val ADD_PLAYLIST_ITEM = "addPlayList:0"
    const val ARTIST_ITEM = "artist:0"
    const val MUSIC_ITEM = "music:0"
    const val PLAYLIST_ITEM = "playList:0"
}