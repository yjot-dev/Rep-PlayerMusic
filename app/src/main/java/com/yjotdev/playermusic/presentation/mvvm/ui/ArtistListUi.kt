package com.yjotdev.playermusic.presentation.mvvm.ui

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
import com.yjotdev.playermusic.presentation.components.artist_list_view.ItemArtist
import com.yjotdev.playermusic.presentation.theme.PlayerMusicTheme
import com.yjotdev.playermusic.domain.model.MusicListModel
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.presentation.utils.ComponentPreview
import com.yjotdev.playermusic.presentation.utils.TestTags

@Composable
fun ArtistListView(
    modifier: Modifier = Modifier,
    artistList: List<MusicListModel> = listOf(),
    itemClicked: (MusicListModel)-> Unit
){
    val tag = TestTags.ARTIST_ITEM.substring(0, 7)
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
                    .testTag("$tag$indexArtist"),
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
                MusicListModel(
                    name = "Artist Name",
                    totalArtistMusic = "9",
                    totalArtistAlbum = "3"
                )
            ),
            itemClicked = {}
        )
    }
}