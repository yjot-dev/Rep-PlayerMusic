package com.yjotdev.playermusic.application.navigation

import androidx.annotation.StringRes
import com.yjotdev.playermusic.R

enum class ViewRoutes(@StringRes val idTitle: Int){
    ArtistList(idTitle = R.string.app_artistList),
    MusicList(idTitle = R.string.app_musicList),
    CurrentMusic1(idTitle = R.string.app_currentMusic),
    PlayList(idTitle = R.string.app_playList),
    CurrentPlayList(idTitle = R.string.app_musicList),
    CurrentMusic2(idTitle = R.string.app_currentMusic),
    AddPlayList(idTitle = R.string.app_addPlayList);
}