package com.yjotdev.playermusic.presentation.mvvm.state

import com.yjotdev.playermusic.domain.model.MusicModel
import com.yjotdev.playermusic.domain.model.MusicListModel
import com.yjotdev.playermusic.domain.utils.RepeatOptions

data class UiState(
    //Estados de la UI
    val artistList: List<MusicListModel> = listOf(),
    val playList: List<MusicListModel> = listOf(),
    val selectedArtistList: MusicListModel? = null,
    val selectedPlaylist: MusicListModel? = null,
    val repeat: RepeatOptions = RepeatOptions.Current,
    //Estados auxiliares
    val isRestartApp: Boolean = false,
    val isPlayList: Boolean = false,
    val itemSelected: List<MusicModel> = listOf()
)