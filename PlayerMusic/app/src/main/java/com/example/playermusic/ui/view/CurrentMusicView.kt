package com.example.playermusic.ui.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import com.example.playermusic.R

@Composable
fun CurrentMusic(
    modifier: Modifier = Modifier,
    artistName: String,
    musicName: String,
    albumName: String,
    albumUri: String,
    clickedPlayList: ()-> Unit,
    clickedShuffle: ()-> Unit,
    clickedRepeat: ()-> Unit,
    clickedPrevious: ()-> Unit,
    clickedPlay: ()-> Unit,
    clickedNext: ()-> Unit,
    isPause: Boolean,
    isShuffle: Boolean,
    isRepeat: Boolean,
    duration: String,
    currentDuration: String,
    durationValue: Float,
    onDurationChange: (Float)-> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.short_dp_1))
                .fillMaxHeight(0.5f),
            painter = rememberAsyncImagePainter(model = albumUri),
            contentScale = ContentScale.Fit,
            contentDescription = null
        )
        ButtonsControl(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.short_dp_1))
                .fillMaxWidth(0.9f),
            isShuffle = isShuffle,
            isRepeat = isRepeat,
            idIcon1 = R.drawable.playlist_48,
            clicked1 = clickedPlayList,
            idIcon2 = R.drawable.shuffle_48,
            clicked2 = clickedShuffle,
            idIcon3 = R.drawable.repeat_48,
            clicked3 = clickedRepeat,
        )
        ItemDescription(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.short_dp_1))
                .fillMaxWidth(0.9f),
            idIcon = R.drawable.artist_48,
            textDescription = artistName
        )
        ItemDescription(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.short_dp_1))
                .fillMaxWidth(0.9f),
            idIcon = R.drawable.music_48,
            textDescription = musicName
        )
        ItemDescription(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.short_dp_1))
                .fillMaxWidth(0.9f),
            idIcon = R.drawable.album_48,
            textDescription = albumName
        )
        PlayerMusic(
            modifier = Modifier.fillMaxWidth(0.9f),
            clickedPrevious = clickedPrevious,
            clickedPlay = clickedPlay,
            clickedNext = clickedNext,
            isPause = isPause,
            duration = duration,
            currentDuration = currentDuration,
            durationValue = durationValue,
            onDurationChange = onDurationChange,
            valueRange = valueRange,
            steps = steps
        )
    }
}

@Composable
private fun ButtonsControl(
    modifier: Modifier = Modifier,
    @DrawableRes idIcon1: Int,
    @DrawableRes idIcon2: Int,
    @DrawableRes idIcon3: Int,
    isShuffle: Boolean = false,
    isRepeat: Boolean = false,
    totalDuration: String? = null,
    currentDuration: String? = null,
    clicked1: ()-> Unit,
    clicked2: ()-> Unit,
    clicked3: ()-> Unit
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center.apply {
            Arrangement.spacedBy(dimensionResource(id = R.dimen.short_dp_2))
        },
        verticalAlignment = Alignment.CenterVertically
    ){
        if(currentDuration != null){
            Text(
                text = currentDuration,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
        }
        IconButton(
            onClick = clicked1,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(idIcon1),
                contentDescription = null
            )
        }
        IconButton(
            onClick = clicked2,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(idIcon2),
                contentDescription = null,
                tint = if(isShuffle) Color.Blue else Color.Black
            )
        }
        IconButton(
            onClick = clicked3,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(idIcon3),
                contentDescription = null,
                tint = if(isRepeat) Color.Blue else Color.Black
            )
        }
        if(totalDuration != null){
            Text(
                text = totalDuration,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ItemDescription(
    modifier: Modifier = Modifier,
    @DrawableRes idIcon: Int,
    textDescription: String
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start.apply {
            Arrangement.spacedBy(dimensionResource(id = R.dimen.short_dp_2))
        },
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            modifier = Modifier.fillMaxWidth(0.1f),
            imageVector = ImageVector.vectorResource(idIcon),
            contentDescription = null
        )
        Text(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            text = textDescription,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Clip
        )
    }
}

@Composable
private fun PlayerMusic(
    modifier: Modifier = Modifier,
    clickedPrevious: ()-> Unit,
    clickedPlay: ()-> Unit,
    clickedNext: ()-> Unit,
    isPause: Boolean,
    duration: String,
    currentDuration: String,
    durationValue: Float,
    onDurationChange: (Float)-> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        ButtonsControl(
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.short_dp_1)),
            idIcon1 = R.drawable.play_previous_48,
            clicked1 = clickedPrevious,
            idIcon2 = if(isPause) R.drawable.play_48 else R.drawable.pause_48,
            clicked2 = clickedPlay,
            idIcon3 = R.drawable.play_next_48,
            clicked3 = clickedNext,
            totalDuration = duration,
            currentDuration = currentDuration
        )
        Slider(
            value = durationValue,
            onValueChange = { onDurationChange(it) },
            valueRange = valueRange,
            steps = steps
        )
    }
}