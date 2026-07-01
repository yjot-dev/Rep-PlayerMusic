package com.yjotdev.playermusic.presentation.mvvm.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import com.yjotdev.playermusic.presentation.components.add_playlist_view.ItemAddPlayList
import com.yjotdev.playermusic.presentation.components.add_playlist_view.HeaderAddPlaylist
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.presentation.theme.PlayerMusicTheme
import com.yjotdev.playermusic.presentation.utils.ComponentPreview
import com.yjotdev.playermusic.domain.model.MusicListModel
import com.yjotdev.playermusic.presentation.utils.TestTags

@Composable
fun AddPlayListView(
    modifier: Modifier = Modifier,
    filter: List<MusicListModel>,
    playListName: String,
    playListNameChange: (String)-> Unit,
    playListSearch: ()-> Unit,
    addPlayListClicked: (String)-> Unit
){
    val tag = TestTags.ADD_PLAYLIST_ITEM.substring(0, 12)
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            HeaderAddPlaylist(
                modifier = Modifier.fillMaxWidth(0.9f),
                playListName = playListName,
                playListNameChange = playListNameChange,
                playListSearch = playListSearch,
                addPlayListClicked = addPlayListClicked
            )
            Spacer(modifier = Modifier.sizeIn(
                minHeight = dimensionResource(id = R.dimen.short_dp_1),
                maxHeight = dimensionResource(id = R.dimen.short_dp_2)
            ))
        }
        items(filter.size) { index ->
            val item = filter[index]
            ItemAddPlayList(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.short_dp_1))
                    .fillMaxWidth(0.9f)
                    .testTag("$tag$index"),
                namePlayList = item.name,
                totalArtistMusic = item.totalArtistMusic.uppercase(),
                onClick = { addPlayListClicked(item.name) }
            )
        }
    }
}

@ComponentPreview
@Composable
private fun PreviewAddPlayListView(){
    PlayerMusicTheme {
        AddPlayListView(
            filter = listOf(),
            playListName = "",
            playListNameChange = {},
            playListSearch = {},
            addPlayListClicked = {}
        )
    }
}