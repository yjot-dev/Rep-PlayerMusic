package com.yjotdev.playermusic.application.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.application.navigation.ViewRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBarMenu(
    routeTitles: ViewRoutes,
    routeArg: String,
    canNavigateBack: Boolean,
    navigateUp: ()-> Unit,
    goPlayList: ()-> Unit
){
    TopAppBar(
        title = {
            Text(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                text = stringResource(routeTitles.idTitle, routeArg),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        },
        navigationIcon = {
            if(canNavigateBack){
                IconButton(
                    onClick = navigateUp,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.short_dp_5))
                        .testTag(stringResource(R.string.cd_navigation_back))
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.arrow_back_48),
                        contentDescription = null,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_4))
                    )
                }
            }
        },
        actions = {
            if(routeTitles.name == ViewRoutes.ArtistList.name){
                IconButton(
                    onClick = goPlayList,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.short_dp_5))
                        .testTag(stringResource(R.string.cd_navigation_playlist))
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.playlist_48),
                        contentDescription = null,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
                    )
                }
            }
        }
    )
}