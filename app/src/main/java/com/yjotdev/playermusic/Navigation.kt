package com.yjotdev.playermusic

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
<<<<<<< HEAD
import androidx.navigation.compose.rememberNavController
=======
>>>>>>> master
import com.yjotdev.playermusic.ui.model.MusicListModel
import com.yjotdev.playermusic.ui.model.MusicModel
import com.yjotdev.playermusic.ui.model.RepeatOptions
import com.yjotdev.playermusic.ui.view.AddPlayList
import com.yjotdev.playermusic.ui.view.ArtistList
import com.yjotdev.playermusic.ui.view.CurrentMusic
import com.yjotdev.playermusic.ui.view.CurrentPlayList
import com.yjotdev.playermusic.ui.view.MusicList
import com.yjotdev.playermusic.ui.view.MyAlertDialog
import com.yjotdev.playermusic.ui.view.PlayList
import com.yjotdev.playermusic.ui.viewModel.DatabaseProcess
import com.yjotdev.playermusic.ui.viewModel.MediaPlayerProcess
import com.yjotdev.playermusic.ui.viewModel.PlayerMusicViewModel

<<<<<<< HEAD
private enum class RouteViews(@StringRes val idTitle: Int){
=======
enum class RouteViews(@StringRes val idTitle: Int){
>>>>>>> master
    ArtistList(idTitle = R.string.app_artistList),
    MusicList(idTitle = R.string.app_musicList),
    CurrentMusic1(idTitle = R.string.app_currentMusic),
    PlayList(idTitle = R.string.app_playList),
    CurrentPlayList(idTitle = R.string.app_musicList),
    CurrentMusic2(idTitle = R.string.app_currentMusic),
    AddPlayList(idTitle = R.string.app_addPlayList);
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolBarMenu(
    routeTitles: RouteViews,
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
                    modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
<<<<<<< HEAD
                        contentDescription = null,
=======
                        contentDescription = stringResource(R.string.cd_navigation_back),
>>>>>>> master
                        modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_4))
                    )
                }
            }
        },
        actions = {
            if(routeTitles.name == RouteViews.ArtistList.name){
                IconButton(
                    onClick = goPlayList,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.playlist_48),
<<<<<<< HEAD
                        contentDescription = null,
=======
                        contentDescription = stringResource(R.string.cd_navigation_playlist),
>>>>>>> master
                        modifier = Modifier.size(dimensionResource(id = R.dimen.short_dp_5))
                    )
                }
            }
        }
    )
}

@Composable
fun AppScreen(
    vmPlayerMusic: PlayerMusicViewModel,
<<<<<<< HEAD
    navController: NavHostController = rememberNavController()
=======
    navController: NavHostController,
    dbProcess: DatabaseProcess
>>>>>>> master
){
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiStatePlayerMusic by vmPlayerMusic.uiState.collectAsState()
<<<<<<< HEAD
    //Inicializa DatabaseProcess
    val dbProcess = DatabaseProcess()
=======
>>>>>>> master
    //Inicializa MediaPlayerProcess
    val mpProcess = MediaPlayerProcess()
    //Vista ToolBarMenu
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = RouteViews.valueOf(
        backStackEntry?.destination?.route ?: RouteViews.ArtistList.name
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
    val isPausa = uiStatePlayerMusic.uiIsPause
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
    //Scrolls verticales de las listas
    val scrollArtistList = rememberScrollState()
    val scrollPlayList = rememberScrollState()
    val scrollMusicList1 = rememberScrollState()
    val scrollMusicList2 = rememberScrollState()
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
                                RouteViews.MusicList -> artistList[index0].name
                                RouteViews.CurrentPlayList -> playList[index1].name
                                else -> ""
                           },
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {
                    //Navega hacia atras
                    navController.navigateUp()
                },
                goPlayList = {
                    //Navega a la lista de playlist
                    navController.navigate(RouteViews.PlayList.name)
                }
            )
        }
    ){ innerPadding ->
        LaunchedEffect(
            key1 = uiStatePlayerMusic.uiIsCompletion,
            key2 = uiStatePlayerMusic.uiIsRestartApp,
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
            block = {
=======
            key3 = uiStatePlayerMusic.uiCurrentDuration,
            block = {
                vmPlayerMusic.setUiAutoDurationValue()
>>>>>>> master
=======
            block = {
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
=======
            key3 = uiStatePlayerMusic.uiCurrentDuration,
            block = {
                vmPlayerMusic.setUiAutoDurationValue()
>>>>>>> master
=======
            key3 = uiStatePlayerMusic.uiCurrentDuration,
            block = {
                vmPlayerMusic.setUiAutoDurationValue()
>>>>>>> master
                when{
                    uiStatePlayerMusic.uiIsCompletion -> {
                        selectedMusic = if(isPlayList){
                            playList[index1].musicList[index2]
                        }else{
                            artistList[index0].musicList[index2]
                        }
                        vmPlayerMusic.setUiIsCompletion(false)
                    }
                    uiStatePlayerMusic.uiIsRestartApp -> {
                        selectedMusic = if(isPlayList){
                            navController.apply {
                                navigate(RouteViews.PlayList.name)
                                navigate(RouteViews.CurrentPlayList.name)
                                navigate(RouteViews.CurrentMusic2.name)
                            }
                            playList[index1].musicList[index2]
                        }else{
                            navController.apply {
                                navigate(RouteViews.ArtistList.name)
                                navigate(RouteViews.MusicList.name)
                                navigate(RouteViews.CurrentMusic1.name)
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
            startDestination = RouteViews.ArtistList.name,
            modifier = Modifier.padding(innerPadding))
        {
            composable(route = RouteViews.ArtistList.name){
                ArtistList(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollArtistList),
                    artistList = artistList,
                    itemClicked = { itemArtist ->
                        val index = artistList.indexOf(itemArtist)
                        vmPlayerMusic.setUiCurrentArtistListIndex(index)
                        //Navega a la lista de música del artista seleccionado
                        navController.navigate(RouteViews.MusicList.name)
                    }
                )
            }
            composable(route = RouteViews.MusicList.name){
                MusicList(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollMusicList1),
                    musicList = artistList[index0].musicList,
                    itemPlaying = selectedMusic,
                    itemClicked = { itemMusic ->
                        val index = artistList[index0].musicList.indexOf(itemMusic)
                        if(itemMusic != selectedMusic){
                            vmPlayerMusic.setUiMusicList(artistList[index0].musicList)
                            vmPlayerMusic.setUiCurrentMusicListIndex(index)
                            mpProcess.refreshMusic()
                        }else{
                            if(artistList[index0].musicList != musicList){
                                vmPlayerMusic.setUiMusicList(artistList[index0].musicList)
                                vmPlayerMusic.setUiCurrentMusicListIndex(index)
                                mpProcess.refreshMusic()
                            }
                        }
                        //Navega a la música seleccionada
                        navController.navigate(RouteViews.CurrentMusic1.name)
                    },
                    addPlayListClicked = { itemMusic ->
                        selectedItem = itemMusic
                        navController.navigate(RouteViews.AddPlayList.name)
                    }
                )
            }
            composable(route = RouteViews.CurrentMusic1.name){
                CurrentMusic(
                    modifier = Modifier.fillMaxSize(),
                    musicInfo = MusicModel(
                        artistName = artistList[index0].musicList[index2].artistName,
                        musicName = artistList[index0].musicList[index2].musicName,
                        albumName = artistList[index0].musicList[index2].albumName,
                        albumUri = mpProcess.getAlbumUri(
                            context.applicationContext,
                            artistList[index0].musicList[index2].albumUri
                        ),
                        musicDurationFormat = artistList[index0].musicList[index2].musicDurationFormat
                    ),
                    clickedPlayList = {
                        //Navega a la lista de playlist
                        navController.navigate(RouteViews.PlayList.name)
                    },
                    clickedShuffle = {
                        if(!isShuffle){
                            vmPlayerMusic.setUiIsShuffle(true)
                            Toast.makeText(context, context.getString(R.string.toast_shuffle1), Toast.LENGTH_SHORT).show()
                        }else{
                            vmPlayerMusic.setUiIsShuffle(false)
                            Toast.makeText(context, context.getString(R.string.toast_shuffle2), Toast.LENGTH_SHORT).show()
                        }
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
                        dbProcess.saveConfig(context.applicationContext, isShuffle = !isShuffle)
                    },
                    clickedRepeat = {
                        dbProcess.saveConfig(context.applicationContext, valueRepeat = when(isRepeat){
                            RepeatOptions.Current -> {
                                vmPlayerMusic.setUiRepeat(1)
                                Toast.makeText(context, context.getString(R.string.toast_repeat1), Toast.LENGTH_SHORT).show()
                                1
                            }
                            RepeatOptions.All -> {
                                vmPlayerMusic.setUiRepeat(2)
                                Toast.makeText(context, context.getString(R.string.toast_repeat2), Toast.LENGTH_SHORT).show()
                                2
                            }
                            RepeatOptions.Off -> {
                                vmPlayerMusic.setUiRepeat(0)
                                Toast.makeText(context, context.getString(R.string.toast_repeat3), Toast.LENGTH_SHORT).show()
                                0
                            }
                        })
<<<<<<< HEAD
=======
=======
>>>>>>> master
=======
>>>>>>> master
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
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
=======
>>>>>>> master
=======
>>>>>>> master
                    },
                    clickedPrevious = {
                        vmPlayerMusic.setUiCurrentMusicListIndex(index2 - 1)
                        mpProcess.refreshMusic()
                        vmPlayerMusic.startServiceMusicPlayer(context.applicationContext)
                    },
                    clickedPlay = {
                        selectedMusic = artistList[index0].musicList[index2]
                        mpProcess.playClicked(context.applicationContext)
                        vmPlayerMusic.setUiIsPlayList(false)
                    },
                    clickedNext = {
                        vmPlayerMusic.setUiCurrentMusicListIndex(index2 + 1)
                        mpProcess.refreshMusic()
                        vmPlayerMusic.startServiceMusicPlayer(context.applicationContext)
                    },
                    isPause = isPausa,
                    isShuffle = isShuffle,
                    isRepeat = isRepeat,
                    currentDuration = mpProcess.durationFormat(uiStatePlayerMusic.uiCurrentDuration),
                    durationValue = (uiStatePlayerMusic.uiCurrentDuration/1000).toFloat(),
                    onDurationChange = { vmPlayerMusic.setUiManualDurationValue(it.toInt()) },
                    valueRange = 0f..(artistList[index0].musicList[index2].musicDuration/1000).toFloat(),
                    steps = artistList[index0].musicList[index2].musicDuration/1000
                )
            }
            composable(route = RouteViews.PlayList.name){
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
                            dbProcess.updatePlayList(item)
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
                            dbProcess.deletePlayList(selectedPlaylist)
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
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollPlayList),
                    playList = playList,
                    itemClicked = { itemPlayList ->
                        val index = playList.indexOf(itemPlayList)
                        vmPlayerMusic.setUiCurrentPlayListIndex(index)
                        //Navega a la lista de música del playlist seleccionado
                        navController.navigate(RouteViews.CurrentPlayList.name)
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
            composable(route = RouteViews.CurrentPlayList.name){
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
                            dbProcess.updatePlayList(item)
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
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollMusicList2),
                    playListMusic = playList[index1].musicList,
                    itemPlaying = selectedMusic,
                    itemClicked = { itemPlayListMusic ->
                        val index = playList[index1].musicList.indexOf(itemPlayListMusic)
                        if(itemPlayListMusic != selectedMusic) {
                            vmPlayerMusic.setUiMusicList(playList[index1].musicList)
                            vmPlayerMusic.setUiCurrentMusicListIndex(index)
                            mpProcess.refreshMusic()
                        }else{
                            if(playList[index1].musicList != musicList){
                                vmPlayerMusic.setUiMusicList(playList[index1].musicList)
                                vmPlayerMusic.setUiCurrentMusicListIndex(index)
                                mpProcess.refreshMusic()
                            }
                        }
                        //Navega a la música seleccionada
                        navController.navigate(RouteViews.CurrentMusic2.name)
                    },
                    removeMusicClicked = { itemPlayListMusic ->
                        removeMusic = true
                        selectedItem = itemPlayListMusic
                    }
                )
            }
            composable(route = RouteViews.CurrentMusic2.name){
                CurrentMusic(
                    modifier = Modifier.fillMaxSize(),
                    musicInfo = MusicModel(
                        artistName = playList[index1].musicList[index2].artistName,
                        musicName = playList[index1].musicList[index2].musicName,
                        albumName = playList[index1].musicList[index2].albumName,
                        albumUri = mpProcess.getAlbumUri(
                            context.applicationContext,
                            playList[index1].musicList[index2].albumUri
                        ),
                        musicDurationFormat = playList[index1].musicList[index2].musicDurationFormat
                    ),
                    clickedPlayList = {
                        //Navega al inicio ArtistView
                        navController.popBackStack(RouteViews.ArtistList.name, false)
                    },
                    clickedShuffle = {
                        if(!isShuffle){
                            vmPlayerMusic.setUiIsShuffle(true)
                            Toast.makeText(context, context.getString(R.string.toast_shuffle1), Toast.LENGTH_SHORT).show()
                        }else{
                            vmPlayerMusic.setUiIsShuffle(false)
                            Toast.makeText(context, context.getString(R.string.toast_shuffle2), Toast.LENGTH_SHORT).show()
                        }
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
                        dbProcess.saveConfig(context.applicationContext, isShuffle = !isShuffle)
                    },
                    clickedRepeat = {
                        dbProcess.saveConfig(context.applicationContext, valueRepeat = when(isRepeat){
                            RepeatOptions.Current -> {
                                vmPlayerMusic.setUiRepeat(1)
                                Toast.makeText(context, context.getString(R.string.toast_repeat1), Toast.LENGTH_SHORT).show()
                                1
                            }
                            RepeatOptions.All -> {
                                vmPlayerMusic.setUiRepeat(2)
                                Toast.makeText(context, context.getString(R.string.toast_repeat2), Toast.LENGTH_SHORT).show()
                                2
                            }
                            RepeatOptions.Off -> {
                                vmPlayerMusic.setUiRepeat(0)
                                Toast.makeText(context, context.getString(R.string.toast_repeat3), Toast.LENGTH_SHORT).show()
                                0
                            }
                        })
<<<<<<< HEAD
=======
=======
>>>>>>> master
=======
>>>>>>> master
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
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
=======
>>>>>>> master
=======
>>>>>>> master
                    },
                    clickedPrevious = {
                        vmPlayerMusic.setUiCurrentMusicListIndex(index2 - 1)
                        mpProcess.refreshMusic()
                        vmPlayerMusic.startServiceMusicPlayer(context.applicationContext)
                    },
                    clickedPlay = {
                        selectedMusic = playList[index1].musicList[index2]
                        mpProcess.playClicked(context.applicationContext)
                        vmPlayerMusic.setUiIsPlayList(true)
                    },
                    clickedNext = {
                        vmPlayerMusic.setUiCurrentMusicListIndex(index2 + 1)
                        mpProcess.refreshMusic()
                        vmPlayerMusic.startServiceMusicPlayer(context.applicationContext)
                    },
                    isPause = isPausa,
                    isShuffle = isShuffle,
                    isRepeat = isRepeat,
                    currentDuration = mpProcess.durationFormat(uiStatePlayerMusic.uiCurrentDuration),
                    durationValue = (uiStatePlayerMusic.uiCurrentDuration/1000).toFloat(),
                    onDurationChange = { vmPlayerMusic.setUiManualDurationValue(it.toInt()) },
                    valueRange = 0f..(playList[index1].musicList[index2].musicDuration/1000).toFloat(),
                    steps = playList[index1].musicList[index2].musicDuration/1000
                )
            }
            composable(route = RouteViews.AddPlayList.name){
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
                                dbProcess.insertPlayList(item)
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
                            dbProcess.updatePlayList(item)
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