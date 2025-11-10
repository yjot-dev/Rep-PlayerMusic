package com.yjotdev.playermusic.application.mvvm.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import com.yjotdev.playermusic.application.components.music_list_view.ItemMusic
import com.yjotdev.playermusic.application.utils.Helper.durationFormat
import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.application.theme.PlayerMusicTheme
import com.yjotdev.playermusic.application.utils.ComponentPreview

@Composable
fun MusicListView(
    modifier: Modifier = Modifier,
    musicList: List<MusicEntity> = listOf(),
    itemPlaying: MusicEntity,
    itemClicked: (MusicEntity)-> Unit,
    addPlayListClicked: (MusicEntity)-> Unit
){
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        items(musicList.size) { indexMusic ->
            val item = musicList[indexMusic]
            ItemMusic(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.short_dp_1))
                    .fillMaxWidth()
                    .clickable{ itemClicked(item) }
                    .testTag("music:$indexMusic"),
                index = indexMusic,
                title = item.musicName,
                artist = item.artistName,
                duration = durationFormat(item.musicDuration),
                isPlaying = item == itemPlaying,
                addPlayListClicked = { addPlayListClicked(item) }
            )
        }
    }
}

@ComponentPreview
@Composable
private fun PreviewMusicListView(){
    PlayerMusicTheme {
        MusicListView(
            musicList = listOf(),
            itemPlaying = MusicEntity(),
            itemClicked = {},
            addPlayListClicked = {}
        )
    }
}