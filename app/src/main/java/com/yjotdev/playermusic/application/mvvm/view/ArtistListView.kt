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
import com.yjotdev.playermusic.application.components.artist_list_view.ItemArtist
import com.yjotdev.playermusic.application.theme.PlayerMusicTheme
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.application.utils.ComponentPreview

@Composable
fun ArtistListView(
    modifier: Modifier = Modifier,
    artistList: List<MusicListEntity> = listOf(),
    itemClicked: (MusicListEntity)-> Unit
){
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        items(artistList.size) { indexArtist ->
            val item = artistList[indexArtist]
            ItemArtist(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.short_dp_1))
                    .fillMaxWidth()
                    .clickable { itemClicked(item) }
                    .testTag("artist:$indexArtist"),
                artistName = item.name,
                totalArtistMusic = item.totalArtistMusic,
                totalArtistAlbum = item.totalArtistAlbum
            )
        }
    }
}

@ComponentPreview
@Composable
private fun PreviewArtistListView(){
    PlayerMusicTheme {
        ArtistListView(
            artistList = listOf(
                MusicListEntity(
                    name = "Artist Name",
                    totalArtistMusic = "9",
                    totalArtistAlbum = "3"
                )
            ),
            itemClicked = {}
        )
    }
}