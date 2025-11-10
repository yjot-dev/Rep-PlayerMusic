package com.yjotdev.playermusic.application.components.playlist_view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.application.theme.PlayerMusicTheme
import com.yjotdev.playermusic.application.utils.ComponentPreview

@Composable
fun ItemPlayList(
    modifier: Modifier = Modifier,
    index: Int,
    playListName: String,
    totalArtistMusic: String,
    totalArtistAlbum: String,
    editNamePlayListClicked: ()-> Unit,
    removePlayListClicked: ()-> Unit
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
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .weight(0.96f),
                    text = playListName.uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Spacer(modifier = Modifier.weight(0.01f))
                IconButton(
                    onClick = editNamePlayListClicked,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_4))
                        .testTag("editNamePlaylist:$index")
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.playlist_edit_48),
                        contentDescription = null,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_4))
                    )
                }
                Spacer(modifier = Modifier.weight(0.03f))
                IconButton(
                    onClick = removePlayListClicked,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_4))
                        .testTag("removePlaylist:$index")
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.playlist_remove_48),
                        contentDescription = null,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_4))
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = totalArtistMusic.uppercase(),
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = totalArtistAlbum.uppercase(),
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun PreviewItemPlayList(){
    PlayerMusicTheme {
        ItemPlayList(
            index = 0,
            playListName = "PlayList Name",
            totalArtistMusic = "9",
            totalArtistAlbum = "3",
            editNamePlayListClicked = {},
            removePlayListClicked = {}
        )
    }
}