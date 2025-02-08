package com.yjotdev.playermusic.ui.view

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.ui.model.MusicModel
import com.yjotdev.playermusic.ui.model.RepeatOptions

@Composable
fun CurrentMusic(
    modifier: Modifier = Modifier,
    musicInfo: MusicModel,
    clickedPlayList: ()-> Unit,
    clickedShuffle: ()-> Unit,
    clickedRepeat: ()-> Unit,
    clickedPrevious: ()-> Unit,
    clickedPlay: ()-> Unit,
    clickedNext: ()-> Unit,
    isPause: Boolean,
    isShuffle: Boolean,
    isRepeat: RepeatOptions,
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
                .fillMaxHeight(0.4f),
            painter = rememberAsyncImagePainter(model = musicInfo.albumUri),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.primaryContainer,
                blendMode = BlendMode.DstOver
            )
        )
        ButtonsControl(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.short_dp_1))
                .fillMaxWidth(0.9f),
            isShuffle = isShuffle,
            isRepeat = isRepeat,
            idIcon1 = R.drawable.playlist_48,
            idContentDescription1 = R.string.cd_navigation_playlist,
            clicked1 = clickedPlayList,
            idIcon2 = R.drawable.shuffle_48,
            idContentDescription2 = R.string.cd_shuffle,
            clicked2 = clickedShuffle,
            idIcon3 = if(isRepeat == RepeatOptions.All || isRepeat == RepeatOptions.Off)
                          R.drawable.repeat_48 else R.drawable.repeat_one_48,
            idContentDescription3 = R.string.cd_repeat,
            clicked3 = clickedRepeat,
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
            clickedPrevious = clickedPrevious,
            clickedPlay = clickedPlay,
            clickedNext = clickedNext,
            isPause = isPause,
            duration = musicInfo.musicDurationFormat,
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
    @StringRes idContentDescription1: Int,
    @DrawableRes idIcon2: Int,
    @StringRes idContentDescription2: Int,
    @DrawableRes idIcon3: Int,
    @StringRes idContentDescription3: Int,
    isShuffle: Boolean = false,
    isRepeat: RepeatOptions = RepeatOptions.Off,
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
                textAlign = TextAlign.Start
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = clicked1,
            modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(idIcon1),
                contentDescription = stringResource(idContentDescription1),
                modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = clicked2,
            modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(idIcon2),
                contentDescription = stringResource(idContentDescription2),
                modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5)),
                tint = if(isShuffle) MaterialTheme.colorScheme.primary
                       else MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = clicked3,
            modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(idIcon3),
                contentDescription = stringResource(idContentDescription3),
                modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5)),
                tint = if(isRepeat == RepeatOptions.All || isRepeat == RepeatOptions.Current)
                            MaterialTheme.colorScheme.primary
                       else MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if(totalDuration != null){
            Text(
                text = totalDuration,
                textAlign = TextAlign.End
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
            idContentDescription1 = R.string.cd_previous,
            clicked1 = clickedPrevious,
            idIcon2 = if(isPause) R.drawable.play_48 else R.drawable.pause_48,
            idContentDescription2 = R.string.cd_play,
            clicked2 = clickedPlay,
            idIcon3 = R.drawable.play_next_48,
            idContentDescription3 = R.string.cd_next,
            clicked3 = clickedNext,
            totalDuration = duration,
            currentDuration = currentDuration
        )
        Slider(
            modifier = Modifier.testTag("slider"),
            value = durationValue,
            onValueChange = { onDurationChange(it) },
            valueRange = valueRange,
            steps = steps,
            colors = SliderDefaults.colors(
                activeTrackColor = MaterialTheme.colorScheme.primary,
                activeTickColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTickColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}

@Preview(
    showBackground = true,
    name = "MyComposable 1 Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewPlayerMusic(){
    PlayerMusic(
        clickedPrevious = {},
        clickedPlay = {},
        clickedNext = {},
        isPause = true,
        duration = "03:00",
        currentDuration = "00:00",
        durationValue = 40f,
        onDurationChange = {},
        valueRange = 0f..100f,
        steps = 100
    )
}

@Preview(
    showBackground = true,
    name = "MyComposable 2 Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewButtonsControl(){
    ButtonsControl(
        idIcon1 = R.drawable.playlist_48,
        idContentDescription1 = R.string.cd_navigation_playlist,
        clicked1 = {},
        idIcon2 = R.drawable.shuffle_48,
        idContentDescription2 = R.string.cd_shuffle,
        clicked2 = {},
        idIcon3 = R.drawable.repeat_48,
        idContentDescription3 = R.string.cd_repeat,
        clicked3 = {},
        isShuffle = false,
        isRepeat = RepeatOptions.All
    )
}