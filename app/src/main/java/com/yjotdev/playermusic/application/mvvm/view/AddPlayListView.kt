package com.yjotdev.playermusic.application.mvvm.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.application.mvvm.model.MusicListModel

@Composable
fun AddPlayList(
    modifier: Modifier = Modifier,
    filter: List<MusicListModel>,
    playListName: String,
    playListNameChange: (String)-> Unit,
    playListSearch: ()-> Unit,
    addPlayListClicked: (String)-> Unit
){
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            HeaderAddPlayList(
                modifier = Modifier.fillMaxWidth(0.9f),
                playListName = playListName,
                playListNameChange = playListNameChange,
                playListSearch = playListSearch,
                addPlayListClicked = addPlayListClicked
            )
            Spacer(modifier = Modifier.sizeIn(
                minHeight = dimensionResource(id = R.dimen.short_dp_1),
                maxHeight = dimensionResource(id = R.dimen.short_dp_2)
            ))
        }
        items(filter.size) { index ->
            val item = filter[index]
            ItemAddPlayList(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.short_dp_1))
                    .fillMaxWidth(0.9f)
                    .testTag("addPlayList:$index"),
                namePlayList = item.name,
                totalArtistMusic = item.totalArtistMusic.uppercase(),
                onClick = { addPlayListClicked(item.name) }
            )
        }
    }
}

@Composable
private fun HeaderAddPlayList(
    modifier: Modifier = Modifier,
    playListName: String,
    playListNameChange: (String)-> Unit,
    playListSearch: ()-> Unit,
    addPlayListClicked: (String)-> Unit
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        TextField(
            modifier = Modifier.weight(1f).testTag("searchPlayList"),
            label = {
                Text(
                    text = stringResource(id = R.string.app_search),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            value = playListName,
            onValueChange = { name -> playListNameChange(name) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onSearch = { playListSearch() }
            )
        )
        IconButton(
            onClick = { addPlayListClicked(playListName) },
            modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
                .testTag("addPlayList")
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.playlist_add_48),
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
            )
        }
    }
}

@Composable
private fun ItemAddPlayList(
    modifier: Modifier = Modifier,
    namePlayList: String,
    totalArtistMusic: String,
    onClick: ()-> Unit
){
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(
            width = dimensionResource(id = R.dimen.short_dp_1),
            color = MaterialTheme.colorScheme.onSurface,
        )
    ){
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onClick() }
                .padding(dimensionResource(id = R.dimen.short_dp_3))
        ){
            Text(
                text = namePlayList,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = totalArtistMusic,
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }
    }
}