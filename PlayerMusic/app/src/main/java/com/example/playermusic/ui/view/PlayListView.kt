package com.example.playermusic.ui.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.playermusic.R
import com.example.playermusic.ui.model.MusicListModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayList(
    modifier: Modifier = Modifier,
    playList: List<MusicListModel> = listOf(),
    itemClicked: (MusicListModel)-> Unit,
    removePlayListClicked: (MusicListModel)-> Unit
){
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        items(playList){ itemPlayList ->
            ItemPlayList(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.short_dp_1))
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = { itemClicked(itemPlayList) },
                        onLongClick = { removePlayListClicked(itemPlayList) }
                    ),
                playListName = itemPlayList.name,
                totalArtistMusic = itemPlayList.totalArtistMusic,
                totalArtistAlbum = itemPlayList.totalArtistAlbum
            )
        }
    }
}

@Composable
private fun ItemPlayList(
    modifier: Modifier = Modifier,
    playListName: String,
    totalArtistMusic: String,
    totalArtistAlbum: String
){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.short_dp_3))
    ){
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.short_dp_2)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = playListName.uppercase(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.sizeIn(
                minHeight = dimensionResource(id = R.dimen.short_dp_1),
                maxHeight = dimensionResource(id = R.dimen.short_dp_2)
            ))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = totalArtistMusic.uppercase(),
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.sizeIn(
                    minWidth = dimensionResource(id = R.dimen.short_dp_1),
                    maxWidth = dimensionResource(id = R.dimen.short_dp_2)
                ))
                Text(
                    text = totalArtistAlbum.uppercase(),
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}