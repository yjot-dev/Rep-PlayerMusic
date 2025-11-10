package com.yjotdev.playermusic.application.components.current_music_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.domain.entity.RepeatOptions
import com.yjotdev.playermusic.application.theme.PlayerMusicTheme
import com.yjotdev.playermusic.application.utils.ComponentPreview

@Composable
fun ButtonsControl(
    modifier: Modifier = Modifier,
    isRepeat: RepeatOptions = RepeatOptions.All,
    onNavigateToPlayList: ()-> Unit,
    onRepeat: ()-> Unit
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = onNavigateToPlayList,
            modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.playlist_48),
                contentDescription = stringResource(R.string.cd_navigation_playlist),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
            )
        }
        IconButton(
            onClick = onRepeat,
            modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    when(isRepeat){
                        RepeatOptions.All -> R.drawable.repeat_48
                        RepeatOptions.Shuffle -> R.drawable.shuffle_48
                        RepeatOptions.Current -> R.drawable.repeat_one_48
                    }
                ),
                contentDescription = stringResource(R.string.cd_repeat),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
            )
        }
    }
}

@ComponentPreview
@Composable
private fun PreviewButtonsControl(){
    PlayerMusicTheme {
        ButtonsControl(
            onNavigateToPlayList = {},
            onRepeat = {}
        )
    }
}