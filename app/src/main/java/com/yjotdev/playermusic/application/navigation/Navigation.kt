package com.yjotdev.playermusic.application.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
import com.yjotdev.playermusic.application.components.ToolBarMenu
import com.yjotdev.playermusic.domain.entity.MusicListEntity

@Composable
fun Navigation(
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
    //Observa estados asincronicos
    ObserveViewModelState(
        vmPlayerMusic = vmPlayerMusic,
        navController = navController
    )
    //UI
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
                    vmPlayerMusic.cleanItemSelected()
                    navController.navigateUp()
                },
                goPlayList = {
                    //Navega a la lista de playlist
                    navController.navigate(ViewRoutes.PlayList.name)
                }
            )
        },
        floatingActionButton = {
            // El botón de "añadir" o "quitar" solo aparece si hay canciones seleccionadas
            if (uiState.itemSelected.isNotEmpty()) {
                when(currentScreen){
                    ViewRoutes.MusicList -> {
                        FloatingActionButton(onClick = {
                            navController.navigate(ViewRoutes.AddPlayList.name)
                        }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.playlist_add_48),
                                contentDescription = "Agregar a playlist"
                            )
                        }
                    }
                    ViewRoutes.CurrentPlayList -> {
                        FloatingActionButton(onClick = {
                            removeMusic = true
                        }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.remove_48),
                                contentDescription = "Quitar de playlist"
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    ){ innerPadding ->
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
                    selectedItems = uiState.itemSelected,
                    onSelectionChanged = { song ->
                        vmPlayerMusic.toggleSongSelection(song)
                    },
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
                    navigateUp = {
                        vmPlayerMusic.cleanItemSelected()
                        navController.navigateUp()
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
                    onValue = { position -> vmPlayerMusic.onSeekTo(position) },
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
                                vmPlayerMusic.removeSelectedSongsFromPlaylist(playList)
                                Toast.makeText(context, smsAlert3, Toast.LENGTH_SHORT).show()
                                removeMusic = false
                            }
                        },
                        dismissClicked = {
                            removeMusic = false
                            vmPlayerMusic.cleanItemSelected()
                        },
                    )
                }
                CurrentPlayListView(
                    modifier = Modifier.fillMaxSize(),
                    playListMusic = selectedPlayList?.musicList ?: emptyList(),
                    itemPlaying = playerState.currentTrack,
                    selectedItems = uiState.itemSelected,
                    onSelectionChanged = { song ->
                        vmPlayerMusic.toggleSongSelection(song)
                    },
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
                    navigateUp = {
                        vmPlayerMusic.cleanItemSelected()
                        navController.navigateUp()
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
                    onValue = { position -> vmPlayerMusic.onSeekTo(position) },
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
                        val message = when(vmPlayerMusic.addSelectedSongsToPlaylist(name)){
                            1 -> { context.getString(R.string.toast_playListName) }
                            2 -> { context.getString(R.string.toast_insertPlayList, name) }
                            else -> { context.getString(R.string.toast_addMusicPlayList) }
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        playListName = ""
                        navController.navigateUp()
                    }
                )
            }
        }
    }
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