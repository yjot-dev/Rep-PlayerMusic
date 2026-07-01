package com.yjotdev.playermusic.presentation.mvvm.ui

import androidx.activity.compose.BackHandler
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
import com.yjotdev.playermusic.presentation.components.current_playlist_view.ItemPlayListMusic
import com.yjotdev.playermusic.presentation.utils.Helper.durationFormat
import com.yjotdev.playermusic.domain.model.MusicModel
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.presentation.theme.PlayerMusicTheme
import com.yjotdev.playermusic.presentation.utils.ComponentPreview
import com.yjotdev.playermusic.presentation.utils.TestTags

@Composable
fun CurrentPlayListView(
    modifier: Modifier = Modifier,
    playListMusic: List<MusicModel> = listOf(),
    itemPlaying: MusicModel,
    selectedItems: List<MusicModel>,
    onSelectionChanged: (MusicModel) -> Unit,
    itemClicked: (MusicModel)-> Unit,
    navigateUp: () -> Unit
){
    val tag = TestTags.MUSIC_ITEM.substring(0, 6)
    BackHandler { navigateUp() }
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        items(playListMusic.size) { indexPlayListMusic ->
            val item = playListMusic[indexPlayListMusic]
            val isSelected = selectedItems.contains(item)
            ItemPlayListMusic(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.short_dp_1))
                    .fillMaxWidth()
                    .clickable { itemClicked(item) }
                    .testTag("$tag$indexPlayListMusic"),
                title = item.musicName,
                artist = item.artistName,
                duration = durationFormat(item.musicDuration),
                isPlaying = item == itemPlaying,
                isSelected = isSelected,
                onSelectionChanged = { _ ->
                    onSelectionChanged(item)
                }
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
            itemPlaying = MusicModel(),
            selectedItems = listOf(),
            onSelectionChanged = {},
            itemClicked = {},
            navigateUp = {}
        )
    }
}