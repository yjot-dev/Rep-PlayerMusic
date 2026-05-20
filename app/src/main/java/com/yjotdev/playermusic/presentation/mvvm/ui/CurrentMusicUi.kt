package com.yjotdev.playermusic.presentation.mvvm.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import coil.compose.rememberAsyncImagePainter
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.presentation.components.current_music_view.ButtonsControl
import com.yjotdev.playermusic.presentation.components.current_music_view.PlayerMusic
import com.yjotdev.playermusic.presentation.components.current_music_view.ItemDescription
import com.yjotdev.playermusic.presentation.theme.PlayerMusicTheme
import com.yjotdev.playermusic.presentation.utils.ComponentPreview
import com.yjotdev.playermusic.presentation.utils.Helper.getAlbumUri
import com.yjotdev.playermusic.domain.utils.RepeatOptions
import com.yjotdev.playermusic.domain.model.MusicModel

@Composable
fun CurrentMusicView(
    modifier: Modifier = Modifier,
    musicInfo: MusicModel,
    onNavigateToView: ()-> Unit,
    onRepeat: ()-> Unit,
    clickedPrevious: ()-> Unit,
    onPlay: ()-> Unit,
    onNext: ()-> Unit,
    isPlaying: Boolean,
    isRepeat: RepeatOptions,
    totalDuration: Int,
    currentDuration: Int,
    value: Float,
    onValue: (Float)-> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int
){
    val context = LocalContext.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.short_dp_1))
                .fillMaxHeight(0.4f),
            painter = rememberAsyncImagePainter(model = getAlbumUri(context, musicInfo.albumUri)),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.primary,
                blendMode = BlendMode.DstOver
            )
        )
        ButtonsControl(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.short_dp_1))
                .fillMaxWidth(0.9f),
            isRepeat = isRepeat,
            onNavigateToPlayList = onNavigateToView,
            onRepeat = onRepeat
        )
        ItemDescription(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.short_dp_1))
                .fillMaxWidth(0.9f),
            idIcon = R.drawable.artist_48,
            textDescription = musicInfo.artistName
        )
        ItemDescription(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.short_dp_1))
                .fillMaxWidth(0.9f),
            idIcon = R.drawable.music_48,
            textDescription = musicInfo.musicName
        )
        ItemDescription(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.short_dp_1))
                .fillMaxWidth(0.9f),
            idIcon = R.drawable.album_48,
            textDescription = musicInfo.albumName
        )
        PlayerMusic(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.short_dp_1))
                .fillMaxWidth(0.9f),
            onPrevious = clickedPrevious,
            onPlay = onPlay,
            onNext = onNext,
            isPlaying = isPlaying,
            totalDuration = totalDuration,
            currentDuration = currentDuration,
            value = value,
            onValue = onValue,
            valueRange = valueRange,
            steps = steps
        )
    }
}

@ComponentPreview
@Composable
private fun PreviewCurrentMusicView(){
    PlayerMusicTheme {
        CurrentMusicView(
            musicInfo = MusicModel(
                musicName = "Music Name",
                artistName = "Artist Name",
                albumName = "Album Name"),
            onNavigateToView = {},
            onRepeat = {},
            clickedPrevious = {},
            onPlay = {},
            onNext = {},
            isPlaying = true,
            isRepeat = RepeatOptions.All,
            totalDuration = 1000 * 60 * 3,
            currentDuration = 1000 * 60 * 1,
            value = 0.2f,
            onValue = {},
            valueRange = 0f..1f,
            steps = (1000 * 60 * 3)/1000
        )
    }
}