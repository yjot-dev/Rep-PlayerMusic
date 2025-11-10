package com.yjotdev.playermusic.application.components.add_playlist_view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.application.theme.PlayerMusicTheme
import com.yjotdev.playermusic.application.utils.ComponentPreview

@Composable
fun ItemAddPlayList(
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

@ComponentPreview
@Composable
private fun PreviewItemAddPlayList(){
    PlayerMusicTheme {
        ItemAddPlayList(
            namePlayList = "PlayList",
            totalArtistMusic = "10",
            onClick = {}
        )
    }
}