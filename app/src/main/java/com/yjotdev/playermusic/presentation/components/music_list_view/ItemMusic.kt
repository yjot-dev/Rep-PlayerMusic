package com.yjotdev.playermusic.presentation.components.music_list_view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.presentation.theme.PlayerMusicTheme
import com.yjotdev.playermusic.presentation.utils.ComponentPreview

@Composable
fun ItemMusic(
    modifier: Modifier = Modifier,
    title: String,
    artist: String,
    duration: String,
    isPlaying: Boolean,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit
){
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(
            width = dimensionResource(id = R.dimen.short_dp_1),
            color = MaterialTheme.colorScheme.onSurface,
        )
    ){
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.short_dp_3)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .weight(0.9f),
                    text = title.uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                if(isPlaying){
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.music_48),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.short_dp_2))
                            .size(dimensionResource(id = R.dimen.short_dp_3))
                    )
                }
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = onSelectionChanged,
                    modifier = Modifier
                        .weight(0.1f)
                        .testTag("selectSongCheckbox:$title")
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .weight(0.8f),
                    text = artist.uppercase(),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Text(
                    modifier = Modifier.weight(0.2f),
                    text = duration,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.End
                    )
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun PreviewItemMusic(){
    PlayerMusicTheme {
        ItemMusic(
            title = "Music Name",
            artist = "Artist Name",
            duration = "3:00",
            isPlaying = true,
            isSelected = true,
            onSelectionChanged = {}
        )
    }
}