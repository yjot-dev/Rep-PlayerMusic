package com.yjotdev.playermusic.presentation.components.current_music_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.presentation.theme.PlayerMusicTheme
import com.yjotdev.playermusic.presentation.utils.ComponentPreview
import com.yjotdev.playermusic.presentation.utils.Helper.durationFormat

@Composable
fun PlayerMusic(
    modifier: Modifier = Modifier,
    onPrevious: ()-> Unit,
    onPlay: ()-> Unit,
    onNext: ()-> Unit,
    isPlaying: Boolean,
    totalDuration: Int,
    currentDuration: Int,
    value: Float,
    onValue: (Float)-> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = durationFormat(currentDuration),
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            IconButton(
                onClick = onPrevious,
                modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.play_previous_48),
                    contentDescription = stringResource(R.string.cd_previous),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
                )
            }
            IconButton(
                onClick = onPlay,
                modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(if(isPlaying) R.drawable.pause_48 else R.drawable.play_48),
                    contentDescription = stringResource(R.string.cd_play),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
                )
            }
            IconButton(
                onClick = onNext,
                modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.play_next_48),
                    contentDescription = stringResource(R.string.cd_next),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
                )
            }
            Text(
                text = durationFormat(totalDuration),
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
        Slider(
            modifier = Modifier.testTag("slider"),
            value = value,
            onValueChange = { onValue(it) },
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

@ComponentPreview
@Composable
private fun PreviewPlayerMusic(){
    PlayerMusicTheme {
        PlayerMusic(
            onPrevious = {},
            onPlay = {},
            onNext = {},
            isPlaying = true,
            totalDuration = 1000 * 60 * 3,
            currentDuration = 1000 * 60 * 1,
            value = 0.2f,
            onValue = {},
            valueRange = 0f..1f,
            steps = (1000 * 60 * 3)/1000
        )
    }
}