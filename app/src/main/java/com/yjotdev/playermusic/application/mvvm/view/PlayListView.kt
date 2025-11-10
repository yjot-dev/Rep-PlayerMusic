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
import com.yjotdev.playermusic.application.components.playlist_view.ItemPlayList
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.application.theme.PlayerMusicTheme
import com.yjotdev.playermusic.application.utils.ComponentPreview

@Composable
fun PlayListView(
    modifier: Modifier = Modifier,
    playList: List<MusicListEntity> = listOf(),
    itemClicked: (MusicListEntity)-> Unit,
    editNamePlayListClicked: ()-> Unit,
    removePlayListClicked: (MusicListEntity)-> Unit
){
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        items(playList.size) { indexPlayList ->
            val item = playList[indexPlayList]
            ItemPlayList(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.short_dp_1))
                    .fillMaxWidth()
                    .clickable { itemClicked(item) }
                    .testTag("playList:$indexPlayList"),
                index = indexPlayList,
                playListName = item.name,
                totalArtistMusic = item.totalArtistMusic,
                totalArtistAlbum = item.totalArtistAlbum,
                editNamePlayListClicked = { editNamePlayListClicked() },
                removePlayListClicked = { removePlayListClicked(item) }
            )
        }
    }
}

@ComponentPreview
@Composable
private fun PreviewPlayListView(){
    PlayerMusicTheme {
        PlayListView(
            playList = listOf(),
            itemClicked = {},
            editNamePlayListClicked = {},
            removePlayListClicked = {}
        )
    }
}