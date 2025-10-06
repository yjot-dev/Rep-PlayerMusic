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
import com.yjotdev.playermusic.application.mvvm.model.MusicListModel
import com.yjotdev.playermusic.application.mvvm.model.MusicModel
import com.yjotdev.playermusic.application.mvvm.model.RepeatOptions
import com.yjotdev.playermusic.application.mvvm.view.AddPlayList
import com.yjotdev.playermusic.application.mvvm.view.ArtistList
import com.yjotdev.playermusic.application.mvvm.view.CurrentMusic
import com.yjotdev.playermusic.application.mvvm.view.CurrentPlayList
import com.yjotdev.playermusic.application.mvvm.view.MusicList
import com.yjotdev.playermusic.application.mvvm.view.PlayList
import com.yjotdev.playermusic.application.mvvm.viewModel.PlayerMusicViewModel
import com.yjotdev.playermusic.application.composable.MyAlertDialog

@Composable
fun NavigationView(
    vmPlayerMusic: PlayerMusicViewModel,
    navController: NavHostController
){
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiStatePlayerMusic by vmPlayerMusic.uiState.collectAsState()
    //Vista ToolBarMenu
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ViewRoutes.valueOf(
        backStackEntry?.destination?.route ?: ViewRoutes.ArtistList.name
    )
    //Indices
    val index0 = uiStatePlayerMusic.uiCurrentArtistListIndex
    val index1 = uiStatePlayerMusic.uiCurrentPlayListIndex
    val index2 = uiStatePlayerMusic.uiCurrentMusicListIndex
    //Vista ArtistList
    val artistList = uiStatePlayerMusic.uiArtistList
    //Vista PlayList
    val playList = uiStatePlayerMusic.uiPlayList
    //Lista de reproduccion
    val musicList = uiStatePlayerMusic.uiMusicList
    //Es playlist o artistlist
    val isPlayList = uiStatePlayerMusic.uiIsPlayList
    //Repetir, aleatorio y pausa
    val isRepeat = uiStatePlayerMusic.uiRepeat
    val isShuffle = uiStatePlayerMusic.uiIsShuffle
    val isPausa = vmPlayerMusic.getIsPause()
    //Verificadores
    val isCompletion = uiStatePlayerMusic.uiIsCompletion
    val isRestartApp = uiStatePlayerMusic.uiIsRestartApp
    //Variables locales
    var playListName by remember{ mutableStateOf("") }
    var selectedMusic by remember{ mutableStateOf(MusicModel()) }
    var selectedItem by remember{ mutableStateOf(MusicModel()) }
    var selectedPlaylist by remember{ mutableStateOf(MusicListModel()) }
    var editPlaylistName by remember{ mutableStateOf(false) }
    var removePlaylist by remember{ mutableStateOf(false) }
    var removeMusic by remember{ mutableStateOf(false) }
    var filter by remember{ mutableStateOf(emptyList<MusicListModel>()) }
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
                                ViewRoutes.MusicList -> artistList[index0].name
                                ViewRoutes.CurrentPlayList -> playList[index1].name
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
        LaunchedEffect(
            isShuffle, isRepeat, isPlayList, index0, index1, index2,
            block = {
                Log.d("LogD","Shuffle: $isShuffle\n" +
                         "Repeat: $isRepeat\n" +
                         "Playlist: $isPlayList\n" +
                         "Index0: $index0\n" +
                         "Index1: $index1\n" +
                         "Index2: $index2\n")
                vmPlayerMusic.saveConfig() }
        )
        LaunchedEffect(
            key1 = isCompletion,
            key2 = isRestartApp,
            block = {
                when{
                    isCompletion -> {
                        selectedMusic = if(isPlayList){
                            playList[index1].musicList[index2]
                        }else{
                            artistList[index0].musicList[index2]
                        }
                        vmPlayerMusic.setUiIsCompletion(false)
                    }
                    isRestartApp -> {
                        selectedMusic = if(isPlayList){
                            navController.apply {
                                navigate(ViewRoutes.PlayList.name)
                                navigate(ViewRoutes.CurrentPlayList.name)
                                navigate(ViewRoutes.CurrentMusic2.name)
                            }
                            playList[index1].musicList[index2]
                        }else{
                            navController.apply {
                                navigate(ViewRoutes.ArtistList.name)
                                navigate(ViewRoutes.MusicList.name)
                                navigate(ViewRoutes.CurrentMusic1.name)
                            }
                            artistList[index0].musicList[index2]
                        }
                        vmPlayerMusic.setUiIsRestartApp(false)
                    }
                }
            }
        )
        NavHost(
            navController = navController,
            startDestination = ViewRoutes.ArtistList.name,
            modifier = Modifier.padding(innerPadding))
        {
            composable(route = ViewRoutes.ArtistList.name){
                ArtistList(
                    modifier = Modifier.fillMaxSize(),
                    artistList = artistList,
                    itemClicked = { itemArtist ->
                        val index = artistList.indexOf(itemArtist)
                        vmPlayerMusic.setUiCurrentArtistListIndex(index)
                        //Navega a la lista de música del artista seleccionado
                        navController.navigate(ViewRoutes.MusicList.name)
                    }
                )
            }
            composable(route = ViewRoutes.MusicList.name){
                MusicList(
                    modifier = Modifier.fillMaxSize(),
                    musicList = artistList[index0].musicList,
                    itemPlaying = selectedMusic,
                    itemClicked = { itemMusic ->
                        val index = artistList[index0].musicList.indexOf(itemMusic)
                        if(itemMusic != selectedMusic){
                            vmPlayerMusic.setUiMusicList(artistList[index0].musicList)
                            vmPlayerMusic.setChangeIndex(index)
                            vmPlayerMusic.refreshMusic()
                        }else{
                            if(artistList[index0].musicList != musicList){
                                vmPlayerMusic.setUiMusicList(artistList[index0].musicList)
                                vmPlayerMusic.setChangeIndex(index)
                                vmPlayerMusic.refreshMusic()
                            }
                        }
                        //Navega a la música seleccionada
                        navController.navigate(ViewRoutes.CurrentMusic1.name)
                    },
                    addPlayListClicked = { itemMusic ->
                        selectedItem = itemMusic
                        navController.navigate(ViewRoutes.AddPlayList.name)
                    }
                )
            }
            composable(route = ViewRoutes.CurrentMusic1.name){
                CurrentMusic(
                    modifier = Modifier.fillMaxSize(),
                    musicInfo = MusicModel(
                        artistName = artistList[index0].musicList[index2].artistName,
                        musicName = artistList[index0].musicList[index2].musicName,
                        albumName = artistList[index0].musicList[index2].albumName,
                        albumUri = vmPlayerMusic.getAlbumUri(
                            context.applicationContext,
                            artistList[index0].musicList[index2].albumUri
                        ),
                        musicDurationFormat = artistList[index0].musicList[index2].musicDurationFormat
                    ),
                    clickedPlayList = {
                        //Navega a la lista de playlist
                        navController.navigate(ViewRoutes.PlayList.name)
                    },
                    clickedShuffle = {
                        if(!isShuffle){
                            vmPlayerMusic.setUiIsShuffle(true)
                            Toast.makeText(context, context.getString(R.string.toast_shuffle1), Toast.LENGTH_SHORT).show()
                        }else{
                            vmPlayerMusic.setUiIsShuffle(false)
                            Toast.makeText(context, context.getString(R.string.toast_shuffle2), Toast.LENGTH_SHORT).show()
                        }
                    },
                    clickedRepeat = {
                        vmPlayerMusic.setUiRepeat(
                            when(isRepeat){
                                RepeatOptions.Current -> {
                                    Toast.makeText(context, context.getString(R.string.toast_repeat1), Toast.LENGTH_SHORT).show()
                                    1
                                }
                                RepeatOptions.All -> {
                                    Toast.makeText(context, context.getString(R.string.toast_repeat2), Toast.LENGTH_SHORT).show()
                                    2
                                }
                                RepeatOptions.Off -> {
                                    Toast.makeText(context, context.getString(R.string.toast_repeat3), Toast.LENGTH_SHORT).show()
                                    0
                                }
                            }
                        )
                    },
                    clickedPrevious = {
                        vmPlayerMusic.setChangeIndex(index2 - 1)
                        vmPlayerMusic.refreshMusic()
                        vmPlayerMusic.startServiceMusicPlayer(context.applicationContext)
                    },
                    clickedPlay = {
                        selectedMusic = artistList[index0].musicList[index2]
                        vmPlayerMusic.playClicked(context.applicationContext)
                        vmPlayerMusic.setUiIsPlayList(false)
                        vmPlayerMusic.setUiCurrentPlayListIndex(0)
                    },
                    clickedNext = {
                        vmPlayerMusic.setChangeIndex(index2 + 1)
                        vmPlayerMusic.refreshMusic()
                        vmPlayerMusic.startServiceMusicPlayer(context.applicationContext)
                    },
                    isPause = isPausa,
                    isShuffle = isShuffle,
                    isRepeat = isRepeat,
                    currentDuration = vmPlayerMusic.durationFormat(uiStatePlayerMusic.uiCurrentDuration),
                    durationValue = (uiStatePlayerMusic.uiCurrentDuration/1000).toFloat(),
                    onDurationChange = { vmPlayerMusic.setUiManualDurationValue(it.toInt()) },
                    valueRange = 0f..(artistList[index0].musicList[index2].musicDuration/1000).toFloat(),
                    steps = artistList[index0].musicList[index2].musicDuration/1000
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
                            val item = MusicListModel(
                                id = playList[index1].id,
                                name = playListName,
                                musicList = playList[index1].musicList
                            )
                            vmPlayerMusic.updatePlayList(item)
                            Toast.makeText(context, smsAlert1, Toast.LENGTH_SHORT).show()
                            editPlaylistName = false
                            playListName = ""
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
                            selectedPlaylist = MusicListModel()
                        },
                        dismissClicked = {
                            removePlaylist = false
                            playListName = ""
                            selectedPlaylist = MusicListModel()
                        },
                    )
                }
                PlayList(
                    modifier = Modifier.fillMaxSize(),
                    playList = playList,
                    itemClicked = { itemPlayList ->
                        val index = playList.indexOf(itemPlayList)
                        vmPlayerMusic.setUiCurrentPlayListIndex(index)
                        //Navega a la lista de música del playlist seleccionado
                        navController.navigate(ViewRoutes.CurrentPlayList.name)
                    },
                    editNamePlayListClicked = {
                        editPlaylistName = true
                    },
                    removePlayListClicked = { itemPlayList ->
                        removePlaylist = true
                        playListName = itemPlayList.name
                        selectedPlaylist = itemPlayList
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
                            val list = playList[index1].musicList.toMutableList()
                            list.remove(selectedItem)
                            val item = MusicListModel(
                                id = playList[index1].id,
                                name = playList[index1].name,
                                musicList = list
                            )
                            vmPlayerMusic.updatePlayList(item)
                            vmPlayerMusic.setUiMusicList(playList[index1].musicList)
                            Toast.makeText(context, smsAlert3, Toast.LENGTH_SHORT).show()
                            removeMusic = false
                            selectedItem = MusicModel()
                        },
                        dismissClicked = {
                            removeMusic = false
                            selectedItem = MusicModel()
                        },
                    )
                }
                CurrentPlayList(
                    modifier = Modifier.fillMaxSize(),
                    playListMusic = playList[index1].musicList,
                    itemPlaying = selectedMusic,
                    itemClicked = { itemPlayListMusic ->
                        val index = playList[index1].musicList.indexOf(itemPlayListMusic)
                        if(itemPlayListMusic != selectedMusic) {
                            vmPlayerMusic.setUiMusicList(playList[index1].musicList)
                            vmPlayerMusic.setChangeIndex(index)
                            vmPlayerMusic.refreshMusic()
                        }else{
                            if(playList[index1].musicList != musicList){
                                vmPlayerMusic.setUiMusicList(playList[index1].musicList)
                                vmPlayerMusic.setChangeIndex(index)
                                vmPlayerMusic.refreshMusic()
                            }
                        }
                        //Navega a la música seleccionada
                        navController.navigate(ViewRoutes.CurrentMusic2.name)
                    },
                    removeMusicClicked = { itemPlayListMusic ->
                        removeMusic = true
                        selectedItem = itemPlayListMusic
                    }
                )
            }
            composable(route = ViewRoutes.CurrentMusic2.name){
                CurrentMusic(
                    modifier = Modifier.fillMaxSize(),
                    musicInfo = MusicModel(
                        artistName = playList[index1].musicList[index2].artistName,
                        musicName = playList[index1].musicList[index2].musicName,
                        albumName = playList[index1].musicList[index2].albumName,
                        albumUri = vmPlayerMusic.getAlbumUri(
                            context.applicationContext,
                            playList[index1].musicList[index2].albumUri
                        ),
                        musicDurationFormat = playList[index1].musicList[index2].musicDurationFormat
                    ),
                    clickedPlayList = {
                        //Navega al inicio ArtistView
                        navController.popBackStack(ViewRoutes.ArtistList.name, false)
                    },
                    clickedShuffle = {
                        if(!isShuffle){
                            vmPlayerMusic.setUiIsShuffle(true)
                            Toast.makeText(context, context.getString(R.string.toast_shuffle1), Toast.LENGTH_SHORT).show()
                        }else{
                            vmPlayerMusic.setUiIsShuffle(false)
                            Toast.makeText(context, context.getString(R.string.toast_shuffle2), Toast.LENGTH_SHORT).show()
                        }
                    },
                    clickedRepeat = {
                        vmPlayerMusic.setUiRepeat(
                            when(isRepeat){
                                RepeatOptions.Current -> {
                                    Toast.makeText(context, context.getString(R.string.toast_repeat1), Toast.LENGTH_SHORT).show()
                                    1
                                }
                                RepeatOptions.All -> {
                                    Toast.makeText(context, context.getString(R.string.toast_repeat2), Toast.LENGTH_SHORT).show()
                                    2
                                }
                                RepeatOptions.Off -> {
                                    Toast.makeText(context, context.getString(R.string.toast_repeat3), Toast.LENGTH_SHORT).show()
                                    0
                                }
                            }
                        )
                    },
                    clickedPrevious = {
                        vmPlayerMusic.setChangeIndex(index2 - 1)
                        vmPlayerMusic.refreshMusic()
                        vmPlayerMusic.startServiceMusicPlayer(context.applicationContext)
                    },
                    clickedPlay = {
                        selectedMusic = playList[index1].musicList[index2]
                        vmPlayerMusic.playClicked(context.applicationContext)
                        vmPlayerMusic.setUiIsPlayList(true)
                        vmPlayerMusic.setUiCurrentArtistListIndex(0)
                    },
                    clickedNext = {
                        vmPlayerMusic.setChangeIndex(index2 + 1)
                        vmPlayerMusic.refreshMusic()
                        vmPlayerMusic.startServiceMusicPlayer(context.applicationContext)
                    },
                    isPause = isPausa,
                    isShuffle = isShuffle,
                    isRepeat = isRepeat,
                    currentDuration = vmPlayerMusic.durationFormat(uiStatePlayerMusic.uiCurrentDuration),
                    durationValue = (uiStatePlayerMusic.uiCurrentDuration/1000).toFloat(),
                    onDurationChange = { vmPlayerMusic.setUiManualDurationValue(it.toInt()) },
                    valueRange = 0f..(playList[index1].musicList[index2].musicDuration/1000).toFloat(),
                    steps = playList[index1].musicList[index2].musicDuration/1000
                )
            }
            composable(route = ViewRoutes.AddPlayList.name){
                AddPlayList(
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
                                val item = MusicListModel(
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
                            val item = MusicListModel(
                                id = playList[existIndex].id,
                                name = playList[existIndex].name,
                                musicList = list
                            )
                            vmPlayerMusic.updatePlayList(item)
                            message = context.getString(R.string.toast_addMusicPlayList)
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        playListName = ""
                        selectedItem = MusicModel()
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