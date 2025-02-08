package com.yjotdev.playermusic.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
<<<<<<< HEAD
=======
import androidx.compose.ui.platform.testTag
>>>>>>> master
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.ui.model.MusicListModel

@Composable
fun PlayList(
    modifier: Modifier = Modifier,
    playList: List<MusicListModel> = listOf(),
    itemClicked: (MusicListModel)-> Unit,
    editNamePlayListClicked: ()-> Unit,
    removePlayListClicked: (MusicListModel)-> Unit
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        playList.forEach { itemPlayList ->
<<<<<<< HEAD
=======
            val idTag = playList.indexOf(itemPlayList)
>>>>>>> master
            ItemPlayList(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.short_dp_1))
                    .fillMaxWidth()
<<<<<<< HEAD
                    .clickable { itemClicked(itemPlayList) },
=======
                    .clickable { itemClicked(itemPlayList) }
                    .testTag("playList:$idTag"),
>>>>>>> master
                playListName = itemPlayList.name,
                totalArtistMusic = itemPlayList.totalArtistMusic,
                totalArtistAlbum = itemPlayList.totalArtistAlbum,
                editNamePlayListClicked = { editNamePlayListClicked() },
                removePlayListClicked = { removePlayListClicked(itemPlayList) }
            )
        }
    }
}

@Composable
private fun ItemPlayList(
    modifier: Modifier = Modifier,
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