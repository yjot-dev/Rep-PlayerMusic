package com.yjotdev.playermusic.application.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.domain.entity.RepeatOptions
import com.yjotdev.playermusic.application.mvvm.view.AddPlayListView
import com.yjotdev.playermusic.application.mvvm.view.ArtistListView
import com.yjotdev.playermusic.application.mvvm.view.CurrentMusicView
import com.yjotdev.playermusic.application.mvvm.view.CurrentPlayListView
import com.yjotdev.playermusic.application.mvvm.view.MusicListView
import com.yjotdev.playermusic.application.mvvm.view.PlayListView
import com.yjotdev.playermusic.application.mvvm.viewModel.PlayerMusicViewModel
import com.yjotdev.playermusic.application.components.MyAlertDialog
import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.domain.entity.MusicListEntity

@Composable
fun NavigationView(
    vmPlayerMusic: PlayerMusicViewModel,
    navController: NavHostController
){
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState by vmPlayerMusic.uiState.collectAsState()
    val playerState by vmPlayerMusic.playerState.collectAsState()
    //Vista ToolBarMenu
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ViewRoutes.valueOf(
        backStackEntry?.destination?.route ?: ViewRoutes.ArtistList.name
    )
    //Vista ArtistList
    val artistList = uiState.artistList
    val selectedArtistList = uiState.selectedArtistList
    //Vista PlayList
    val playList = uiState.playList
    val selectedPlayList = uiState.selectedPlaylist
    //Repetir en aleatorio, lineal o la misma musica
    val isRepeat = uiState.repeat
    //Variables locales
    var playListName by remember{ mutableStateOf("") }
    var selectedItem by remember{ mutableStateOf(playerState.currentTrack) }
    var selectedPlaylist by remember{ mutableStateOf(MusicListEntity()) }
    var editPlaylistName by remember{ mutableStateOf(false) }
    var removePlaylist by remember{ mutableStateOf(false) }
    var removeMusic by remember{ mutableStateOf(false) }
    var filter by remember{ mutableStateOf(emptyList<MusicListEntity>()) }
    filter = playList
    //Texto de AlertDialogs
    val smsTitle = stringResource(R.string.ad_title)
    val smsMessage1 = stringResource(R.string.ad_editNamePlayList)
    val smsAlert1 = stringResource(R.string.toast_editNamePlayList)
    val smsEditName = stringResource(R.string.ad_editName)
    val smsMessage2 = stringResource(R.string.ad_removePlayList)
    val smsAlert2 = stringResource(R.string.toast_removePlayList, playListName)
    val smsMessage3 = stringResource(R.string.ad_deleteMusicPlayList)
    val smsAlert3 = stringResource(R.string.toast_deleteMusicPlayList)
    val smsYes = stringResource(R.string.ad_yes)
    val smsNo = stringResource(R.string.ad_no)
    Scaffold(
        topBar = {
            ToolBarMenu(
                routeTitles = currentScreen,
                routeArg = when(currentScreen){
                                ViewRoutes.MusicList -> selectedArtistList!!.name
                                ViewRoutes.CurrentPlayList -> selectedPlayList!!.name
                                else -> ""
                           },
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {
                    //Navega hacia atras
                    navController.navigateUp()
                },
                goPlayList = {
                    //Navega a la lista de playlist
                    navController.navigate(ViewRoutes.PlayList.name)
                }
            )
        }
    ){ innerPadding ->
        ObserveViewModelState(
            vmPlayerMusic = vmPlayerMusic,
            navController = navController
        )
        NavHost(
            navController = navController,
            startDestination = ViewRoutes.ArtistList.name,
            modifier = Modifier.padding(innerPadding))
        {
            composable(route = ViewRoutes.ArtistList.name){
                ArtistListView(
                    modifier = Modifier.fillMaxSize(),
                    artistList = artistList,
                    itemClicked = { itemArtist ->
                        //Informa al ViewModel de la selección
                        vmPlayerMusic.setArtistListSelected(itemArtist)
                        //Navega a la lista de música del artista seleccionado
                        navController.navigate(ViewRoutes.MusicList.name)
                    }
                )
            }
            composable(route = ViewRoutes.MusicList.name){
                MusicListView(
                    modifier = Modifier.fillMaxSize(),
                    musicList = selectedArtistList?.musicList ?: emptyList(),
                    itemPlaying = playerState.currentTrack,
                    itemClicked = { item ->
                        //Informa al ViewModel que esta canción debe sonar
                        if (playerState.currentTrack != item) {
                            vmPlayerMusic.onPlayTrack(item)
                        } else {
                            vmPlayerMusic.onResumeTrack()
                        }
                        vmPlayerMusic.setIsPlayList(false)
                        //Navega a la música seleccionada
                        navController.navigate(ViewRoutes.CurrentMusic1.name)
                    },
                    addPlayListClicked = { item ->
                        selectedItem = item
                        navController.navigate(ViewRoutes.AddPlayList.name)
                    }
                )
            }
            composable(route = ViewRoutes.CurrentMusic1.name){
                CurrentMusicView(
                    modifier = Modifier.fillMaxSize(),
                    musicInfo = playerState.currentTrack,
                    onNavigateToView = {
                        //Navega a la lista de playlist
                        navController.navigate(ViewRoutes.PlayList.name)
                    },
                    onRepeat = {
                        vmPlayerMusic.setRepeat(
                            when(isRepeat){
                                RepeatOptions.Current -> {
                                    Toast.makeText(context, context.getString(R.string.toast_repeat1), Toast.LENGTH_SHORT).show()
                                    1
                                }
                                RepeatOptions.All -> {
                                    Toast.makeText(context, context.getString(R.string.toast_repeat2), Toast.LENGTH_SHORT).show()
                                    2
                                }
                                RepeatOptions.Shuffle -> {
                                    Toast.makeText(context, context.getString(R.string.toast_repeat3), Toast.LENGTH_SHORT).show()
                                    0
                                }
                            }
                        )
                    },
                    clickedPrevious = {
                        vmPlayerMusic.onPreviousTrack()
                    },
                    onPlay = {
                        if(playerState.isPlaying){
                            vmPlayerMusic.onPauseTrack()
                        }else{
                            vmPlayerMusic.onResumeTrack()
                        }
                    },
                    onNext = {
                        vmPlayerMusic.onNextTrack()
                    },
                    isPlaying = playerState.isPlaying,
                    isRepeat = isRepeat,
                    totalDuration = playerState.totalDuration,
                    currentDuration = playerState.currentPosition,
                    value = (playerState.currentPosition/1000).toFloat(), //Equivalente en segundos
                    onValue = { position -> vmPlayerMusic.onSeekTrack(position.toInt()) },
                    valueRange = 0f..(playerState.totalDuration/1000).toFloat(),
                    steps = playerState.totalDuration/1000
                )
            }
            composable(route = ViewRoutes.PlayList.name){
                //Aviso para editar el nombre de playlist seleccionada
                if(editPlaylistName){
                    MyAlertDialog(
                        confirm = smsYes,
                        dismiss = smsNo,
                        title = smsTitle,
                        message = smsMessage1,
                        confirmClicked = {
                            selectedPlayList?.let { playList ->
                                val item = MusicListEntity(
                                    id = playList.id,
                                    name = playListName,
                                    musicList = playList.musicList
                                )
                                vmPlayerMusic.updatePlayList(item)
                                Toast.makeText(context, smsAlert1, Toast.LENGTH_SHORT).show()
                                editPlaylistName = false
                                playListName = ""
                            }
                        },
                        dismissClicked = {
                            editPlaylistName = false
                            playListName = ""
                        },
                        case = 2,
                        value = playListName,
                        label = smsEditName,
                        onValue = { name -> playListName = name }
                    )
                }
                //Aviso para eliminar la playlist seleccionada
                if(removePlaylist){
                    MyAlertDialog(
                        confirm = smsYes,
                        dismiss = smsNo,
                        title = smsTitle,
                        message = smsMessage2,
                        confirmClicked = {
                            vmPlayerMusic.deletePlayList(selectedPlaylist)
                            Toast.makeText(context, smsAlert2, Toast.LENGTH_SHORT).show()
                            removePlaylist = false
                            playListName = ""
                            selectedPlaylist = MusicListEntity()
                        },
                        dismissClicked = {
                            removePlaylist = false
                            playListName = ""
                            selectedPlaylist = MusicListEntity()
                        },
                    )
                }
                PlayListView(
                    modifier = Modifier.fillMaxSize(),
                    playList = playList,
                    itemClicked = { item ->
                        //Informa al ViewModel de la selección
                        vmPlayerMusic.setPlayListSelected(item)
                        //Navega a la lista de música del playlist seleccionado
                        navController.navigate(ViewRoutes.CurrentPlayList.name)
                    },
                    editNamePlayListClicked = {
                        editPlaylistName = true
                    },
                    removePlayListClicked = { item ->
                        removePlaylist = true
                        playListName = item.name
                        selectedPlaylist = item
                    }
                )
            }
            composable(route = ViewRoutes.CurrentPlayList.name){
                //Aviso para eliminar una música de la playlist seleccionada
                if(removeMusic){
                    MyAlertDialog(
                        confirm = smsYes,
                        dismiss = smsNo,
                        title = smsTitle,
                        message = smsMessage3,
                        confirmClicked = {
                            selectedPlayList?.let { playList ->
                                val list = playList.musicList.toMutableList()
                                list.remove(selectedItem)
                                val item = MusicListEntity(
                                    id = playList.id,
                                    name = playList.name,
                                    musicList = list
                                )
                                vmPlayerMusic.updatePlayList(item)
                                Toast.makeText(context, smsAlert3, Toast.LENGTH_SHORT).show()
                                removeMusic = false
                                selectedItem = MusicEntity()
                            }
                        },
                        dismissClicked = {
                            removeMusic = false
                            selectedItem = MusicEntity()
                        },
                    )
                }
                CurrentPlayListView(
                    modifier = Modifier.fillMaxSize(),
                    playListMusic = selectedPlayList?.musicList ?: emptyList(),
                    itemPlaying = playerState.currentTrack,
                    itemClicked = { item ->
                        //Informa al ViewModel que esta canción debe sonar
                        if (playerState.currentTrack != item) {
                            vmPlayerMusic.onPlayTrack(item)
                        } else {
                            vmPlayerMusic.onResumeTrack()
                        }
                        vmPlayerMusic.setIsPlayList(true)
                        //Navega a la música seleccionada
                        navController.navigate(ViewRoutes.CurrentMusic2.name)
                    },
                    removeMusicClicked = { item ->
                        removeMusic = true
                        selectedItem = item
                    }
                )
            }
            composable(route = ViewRoutes.CurrentMusic2.name){
                CurrentMusicView(
                    modifier = Modifier.fillMaxSize(),
                    musicInfo = playerState.currentTrack,
                    onNavigateToView = {
                        //Navega al inicio ArtistView
                        navController.popBackStack(ViewRoutes.ArtistList.name, false)
                    },
                    onRepeat = {
                        vmPlayerMusic.setRepeat(
                            when(isRepeat){
                                RepeatOptions.Current -> {
                                    Toast.makeText(context, context.getString(R.string.toast_repeat1), Toast.LENGTH_SHORT).show()
                                    1
                                }
                                RepeatOptions.All -> {
                                    Toast.makeText(context, context.getString(R.string.toast_repeat2), Toast.LENGTH_SHORT).show()
                                    2
                                }
                                RepeatOptions.Shuffle -> {
                                    Toast.makeText(context, context.getString(R.string.toast_repeat3), Toast.LENGTH_SHORT).show()
                                    0
                                }
                            }
                        )
                    },
                    clickedPrevious = {
                        vmPlayerMusic.onPreviousTrack()
                    },
                    onPlay = {
                        if(playerState.isPlaying){
                            vmPlayerMusic.onPauseTrack()
                        }else{
                            vmPlayerMusic.onResumeTrack()
                        }
                    },
                    onNext = {
                        vmPlayerMusic.onNextTrack()
                    },
                    isPlaying = playerState.isPlaying,
                    isRepeat = isRepeat,
                    totalDuration = playerState.totalDuration,
                    currentDuration = playerState.currentPosition,
                    value = (playerState.currentPosition/1000).toFloat(), //Equivalente en segundos
                    onValue = { position -> vmPlayerMusic.onSeekTrack(position.toInt()) },
                    valueRange = 0f..(playerState.totalDuration/1000).toFloat(),
                    steps = playerState.totalDuration/1000
                )
            }
            composable(route = ViewRoutes.AddPlayList.name){
                AddPlayListView(
                    modifier = Modifier.fillMaxSize(),
                    filter = filter,
                    playListName = playListName,
                    playListNameChange = { name -> playListName = name },
                    playListSearch = {
                        //Busqueda por nombre de playlist
                        filter = playList.filter{ it.name == playListName }
                            .ifEmpty { playList }
                        keyboardController?.hide()
                    },
                    addPlayListClicked = { name ->
                        //Agrega o actualiza una playlist
                        val existIndex = playList.indexOfLast { it.name == name }
                        val message: String
                        if(existIndex == -1){
                            message = if(name.isEmpty()){
                                context.getString(R.string.toast_playListName)
                            }else{
                                //Caso 1 si agrego una música a una nueva playlist
                                val list = listOf(selectedItem)
                                val item = MusicListEntity(
                                    name = name,
                                    musicList = list
                                )
                                vmPlayerMusic.insertPlayList(item)
                                context.getString(R.string.toast_insertPlayList, name)
                            }
                        }else{
                            //Caso 2 si agrego una música a una playlist existente
                            val list = playList[existIndex].musicList.toMutableList()
                            list.add(selectedItem)
                            val item = MusicListEntity(
                                id = playList[existIndex].id,
                                name = playList[existIndex].name,
                                musicList = list
                            )
                            vmPlayerMusic.updatePlayList(item)
                            message = context.getString(R.string.toast_addMusicPlayList)
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        playListName = ""
                        selectedItem = MusicEntity()
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolBarMenu(
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

@Composable
private fun ObserveViewModelState(
    vmPlayerMusic: PlayerMusicViewModel,
    navController: NavHostController
){
    val state by vmPlayerMusic.uiState.collectAsState()
    LaunchedEffect(
        state.repeat, state.isPlayList,
        state.isRestartApp,
    ){
        if(state.isRestartApp) {
            vmPlayerMusic.setIsRestartApp(false)
            if(state.isPlayList){
                navController.apply {
                    navigate(ViewRoutes.CurrentMusic2.name)
                }
            }else{
                navController.apply {
                    navigate(ViewRoutes.CurrentMusic1.name)
                }
            }
        }
        vmPlayerMusic.saveConfig()
        Log.d("LogD","Repeat: ${state.repeat}\n" +
                "IsPlayList: ${state.isPlayList}")
    }
}