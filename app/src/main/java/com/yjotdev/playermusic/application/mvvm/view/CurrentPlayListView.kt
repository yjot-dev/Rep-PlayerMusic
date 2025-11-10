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
import com.yjotdev.playermusic.application.components.current_playlist_view.ItemPlayListMusic
import com.yjotdev.playermusic.application.utils.Helper.durationFormat
import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.application.theme.PlayerMusicTheme
import com.yjotdev.playermusic.application.utils.ComponentPreview

@Composable
fun CurrentPlayListView(
    modifier: Modifier = Modifier,
    playListMusic: List<MusicEntity> = listOf(),
    itemPlaying: MusicEntity,
    itemClicked: (MusicEntity)-> Unit,
    removeMusicClicked: (MusicEntity)-> Unit
){
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        items(playListMusic.size) { indexPlayListMusic ->
            val item = playListMusic[indexPlayListMusic]
            ItemPlayListMusic(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.short_dp_1))
                    .fillMaxWidth()
                    .clickable { itemClicked(item) }
                    .testTag("music:$indexPlayListMusic"),
                index = indexPlayListMusic,
                title = item.musicName,
                artist = item.artistName,
                duration = durationFormat(item.musicDuration),
                isPlaying = item == itemPlaying,
                removeMusicClicked = { removeMusicClicked(item) }
            )
        }
    }
}

@ComponentPreview
@Composable
private fun PreviewCurrentPlayListView(){
    PlayerMusicTheme {
        CurrentPlayListView(
            playListMusic = listOf(),
            itemPlaying = MusicEntity(),
            itemClicked = {},
            removeMusicClicked = {}
        )
    }
}