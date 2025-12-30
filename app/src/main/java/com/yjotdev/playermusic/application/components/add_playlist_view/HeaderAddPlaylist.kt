package com.yjotdev.playermusic.application.components.add_playlist_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.application.theme.PlayerMusicTheme
import com.yjotdev.playermusic.application.utils.ComponentPreview

@Composable
fun HeaderAddPlaylist(
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

@ComponentPreview
@Composable
private fun PreviewHeaderAddPlayList(){
    PlayerMusicTheme {
        HeaderAddPlaylist(
            playListName = "PlayList",
            playListNameChange = {},
            playListSearch = {},
            addPlayListClicked = {}
        )
    }
}