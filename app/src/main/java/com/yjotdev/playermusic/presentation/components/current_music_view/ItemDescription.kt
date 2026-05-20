package com.yjotdev.playermusic.presentation.components.current_music_view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.presentation.theme.PlayerMusicTheme
import com.yjotdev.playermusic.presentation.utils.ComponentPreview

@Composable
fun ItemDescription(
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
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )
        Text(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            text = textDescription,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary,
            ),
            maxLines = 1,
            overflow = TextOverflow.Clip
        )
    }
}

@ComponentPreview
@Composable
private fun PreviewItemDescription(){
    PlayerMusicTheme {
        ItemDescription(
            idIcon = R.drawable.artist_48,
            textDescription = "Artist Name"
        )
    }
}