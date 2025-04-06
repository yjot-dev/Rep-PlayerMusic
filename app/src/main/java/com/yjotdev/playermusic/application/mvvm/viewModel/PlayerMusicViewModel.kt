package com.yjotdev.playermusic.application.mvvm.viewModel

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjotdev.playermusic.application.mvvm.model.MusicListModel
import com.yjotdev.playermusic.application.mvvm.model.MusicModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.yjotdev.playermusic.infrastructure.adapter.MusicService
import com.yjotdev.playermusic.domain.utils.MediaPlayerManager
import com.yjotdev.playermusic.domain.usecase.DeletePlayListUseCase
import com.yjotdev.playermusic.domain.usecase.GetPlayListUseCase
import com.yjotdev.playermusic.domain.usecase.InsertPlayListUseCase
import com.yjotdev.playermusic.domain.usecase.UpdatePlayListUseCase
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.usecase.GetConfigUseCase
import com.yjotdev.playermusic.domain.usecase.SaveConfigUseCase
import com.yjotdev.playermusic.domain.utils.Validation
import com.yjotdev.playermusic.application.mvvm.model.RepeatOptions
import com.yjotdev.playermusic.application.mvvm.model.PlayerMusicModel
import com.yjotdev.playermusic.domain.entity.MusicEntity

@HiltViewModel
class PlayerMusicViewModel @Inject constructor(
    private val insertPlayListUseCase: InsertPlayListUseCase,
    private val updatePlayListUseCase: UpdatePlayListUseCase,
    private val deletePlayListUseCase: DeletePlayListUseCase,
    private val getPlayListUseCase: GetPlayListUseCase,
    private val saveConfigUseCase: SaveConfigUseCase,
    private val getConfigUseCase: GetConfigUseCase
): ViewModel(){
    //Estados mutables del ViewModel
    private val _uiState by lazy{ MutableStateFlow(PlayerMusicModel()) }
    //Estados de solo lectura del ViewModel
    val uiState = _uiState.asStateFlow()
    //Inicializa reproductor de multimedia y isPause
    private val mediaPlayer by lazy{ MediaPlayerManager.getMediaPlayer() }

    /** Actualiza estado de lista de artistas **/
    private fun setUiArtistList(valueList: List<MusicListModel>){
        _uiState.update { currentState ->
            currentState.copy(uiArtistList = valueList)
        }
    }
    /** Actualiza estado de lista de playlists **/
    private fun setUiPlayList(valueList: List<MusicListModel>){
        _uiState.update { currentState ->
            currentState.copy(uiPlayList = valueList)
        }
    }
    /** Actualiza estado de lista de música actual **/
    fun setUiMusicList(valueList: List<MusicModel>){
        _uiState.update { currentState ->
            currentState.copy(uiMusicList = valueList)
        }
    }
    /** Actualiza estado de repetir **/
    fun setUiRepeat(value: Int){
        val v = when(value){
            0 -> { RepeatOptions.Current }
            1 -> { RepeatOptions.All }
            else -> { RepeatOptions.Off }
        }
        _uiState.update { currentState ->
            currentState.copy(uiRepeat = v)
        }
    }
    /** Actualiza estado de aleatorio **/
    fun setUiIsShuffle(value: Boolean){
        _uiState.update { currentState ->
            currentState.copy(uiIsShuffle = value)
        }
    }
    /** Actualiza estado si es playlist **/
    fun setUiIsPlayList(value: Boolean){
        _uiState.update { currentState ->
            currentState.copy(uiIsPlayList = value)
        }
    }
    /** Actualiza estado de música completada **/
    fun setUiIsCompletion(value: Boolean){
        _uiState.update { currentState ->
            currentState.copy(uiIsCompletion = value)
        }
    }
    /** Actualiza estado de reiniciar app **/
    fun setUiIsRestartApp(value: Boolean){
        _uiState.update { currentState ->
            currentState.copy(uiIsRestartApp = value)
        }
    }
    /** Actualiza estado de indice de artista actual **/
    fun setUiCurrentArtistListIndex(value: Int){
        _uiState.update { currentState ->
            currentState.copy(uiCurrentArtistListIndex = value)
        }
    }
    /** Actualiza estado de indice de lista de reproducción actual **/
    fun setUiCurrentPlayListIndex(value: Int){
        _uiState.update { currentState ->
            currentState.copy(uiCurrentPlayListIndex = value)
        }
    }
    /** Actualiza estado de indice de música actual **/
    private fun setUiCurrentMusicListIndex(value: Int){
        _uiState.update { currentState ->
            currentState.copy(uiCurrentMusicListIndex = value)
        }
    }
    /** Cambia indice de la lista de música **/
    fun setChangeIndex(value: Int){
        val list = uiState.value.uiMusicList
        if(list.isNotEmpty()){
            val v = if(value == -1){ list.lastIndex }
            else{ value % list.size }
            setUiCurrentMusicListIndex(v)
        }
    }
    /** Actualiza estado de indice de lista de indices aleatorios **/
    private fun setUiCountIndex(value: Int){
        val list = uiState.value.uiMusicList
        val v = if(value == -1){ list.lastIndex }
        else{ value % list.size }
        _uiState.update { currentState ->
            currentState.copy(uiCountIndex = v)
        }
    }
    /** Actualiza estado manual de la duración de la música **/
    fun setUiManualDurationValue(value: Int){
        val valueInMilliseconds = value*1000
        mediaPlayer.seekTo(valueInMilliseconds)
        _uiState.update { currentState ->
            currentState.copy(uiCurrentDuration = valueInMilliseconds)
        }
    }
    /** Actualiza estado automático de la duración de la música **/
    private fun setUiAutoDurationValue(){
        viewModelScope.launch(Dispatchers.IO){
            val index = uiState.value.uiCurrentMusicListIndex
            val list = uiState.value.uiMusicList[index]
            val totalDurationSec = list.musicDuration/1000
            while(uiState.value.uiCurrentDuration/1000 < totalDurationSec) {
                if (mediaPlayer.isPlaying) {
                    delay(1000)
                    withContext(Dispatchers.Main) {
                        _uiState.update { currentState ->
                            currentState.copy(uiCurrentDuration = mediaPlayer.currentPosition)
                        }
                    }
                }
            }
        }
    }
    /** Crea una MusicList con los indices de ArtistList o PlayList segun corresponda **/
    fun getMusicList(){
        val artistList = uiState.value.uiArtistList
        val playList = uiState.value.uiPlayList
        val index0 = uiState.value.uiCurrentArtistListIndex
        val index1 = uiState.value.uiCurrentPlayListIndex
        if(uiState.value.uiIsPlayList){
            if(playList.isNotEmpty()){
                setUiMusicList(playList[index1].musicList)
            }
        }else{
            if(artistList.isNotEmpty()){
                setUiMusicList(artistList[index0].musicList)
            }
        }
    }
    /** Obtiene estado de pausa **/
    fun getIsPause() = MediaPlayerManager.uiIsPause.value
    /** Inicia servicio de notificación de la reproducción de música **/
    fun startServiceMusicPlayer(applicationContext: Context){
        val uiState = uiState.value
        val index = uiState.uiCurrentMusicListIndex
        val list = uiState.uiMusicList[index]
        val intent = Intent(applicationContext, MusicService::class.java).apply {
            putExtra("musicName", list.musicName)
            putExtra("artistName", list.artistName)
        }
        applicationContext.startService(intent)
    }
    /** Convertir lista MusicListEntity a lista MusicListModel **/
    private fun toListOfMLM(items: List<MusicListEntity>): List<MusicListModel>{
        val list = mutableListOf<MusicListModel>()
        items.forEach { objects ->
            val musicList = mutableListOf<MusicModel>()
            objects.musicList.forEach { obj ->
                musicList.add(MusicModel(
                    musicPath = obj.musicPath,
                    musicDuration = obj.musicDuration,
                    musicDurationFormat = obj.musicDurationFormat,
                    musicName = obj.musicName,
                    artistName = obj.artistName,
                    albumName = obj.albumName,
                    albumUri = obj.albumUri
                ))
            }
            list.add(MusicListModel(
                id = objects.id,
                name = objects.name,
                musicList = musicList
            ))
        }
        return list
    }
    /** Convertir MusicListModel a MusicListEntity **/
    private fun toMLE(item: MusicListModel): MusicListEntity{
        val musicList = mutableListOf<MusicEntity>()
        item.musicList.forEach { obj ->
            musicList.add(MusicEntity(
                musicPath = obj.musicPath,
                musicDuration = obj.musicDuration,
                musicDurationFormat = obj.musicDurationFormat,
                musicName = obj.musicName,
                artistName = obj.artistName,
                albumName = obj.albumName,
                albumUri = obj.albumUri
            ))
        }
        return MusicListEntity(
            id = item.id,
            name = item.name,
            musicList = musicList
        )
    }
    /** Obtiene las listas de reproducción guardadas en la BD local **/
    private suspend fun getPlayList(){
        getPlayListUseCase().collect{ playList ->
            withContext(Dispatchers.Main){
                setUiPlayList(toListOfMLM(playList))
            }
        }
    }
    /** Crea e inserta una lista de reproducción en la BD local **/
    fun insertPlayList(item: MusicListModel){
        viewModelScope.launch{ insertPlayListUseCase(toMLE(item)) }
    }
    /** Actualiza una lista de reproducción en la BD local **/
    fun updatePlayList(item: MusicListModel){
        viewModelScope.launch{ updatePlayListUseCase(toMLE(item)) }
    }
    /** Elimina una lista de reproducción en la BD local **/
    fun deletePlayList(item: MusicListModel){
        viewModelScope.launch{ deletePlayListUseCase(toMLE(item)) }
    }
    /** Guarda datos persistentes para botones aleatorio y repetir **/
    fun saveConfig(){
        val state = uiState.value
        saveConfigUseCase(
            valueRepeat = state.uiRepeat,
            isShuffle = state.uiIsShuffle,
            isPlayList = state.uiIsPlayList,
            index0 = state.uiCurrentArtistListIndex,
            index1 = state.uiCurrentPlayListIndex,
            index2 = state.uiCurrentMusicListIndex
        )
    }
    /** Obtiene las configuraciones del usuario **/
    fun getConfig(){
        getConfigUseCase().apply {
            setUiRepeat(getInt("repeat", 2))
            setUiIsShuffle(getBoolean("isShuffle", false))
            setUiIsPlayList(getBoolean("isPlayList", false))
            setUiCurrentArtistListIndex(getInt("index0", 0))
            setUiCurrentPlayListIndex(getInt("index1", 0))
            setUiCurrentMusicListIndex(getInt("index2", 0))
        }
    }
    /** Obtiene la lista de música del dispositivo móvil del usuario **/
    fun loadData(applicationContext: Context){
        viewModelScope.launch(Dispatchers.IO){
            launch { getArtistList(applicationContext) }
            launch { getPlayList() }
        }
    }
    /** Convierte duración de Long a String formato minutos:segundos **/
    fun durationFormat(duration: Int): String {
        return Validation.durationFormat(duration)
    }
    /** Obtiene imagen del album o deja imagen por defecto **/
    fun getAlbumUri(
        applicationContext: Context,
        albumImageUri: String
    ): String {
        return Validation.getAlbumUri(applicationContext, albumImageUri)
    }
    /** Establece música actual en el MediaPlayer **/
    fun refreshMusic(){
        val index = uiState.value.uiCurrentMusicListIndex
        val list = uiState.value.uiMusicList[index]
        mediaPlayer.reset()
        mediaPlayer.setDataSource(list.musicPath)
        mediaPlayer.prepare()
        setUiManualDurationValue(0)
        MediaPlayerManager.setUiIsPause(true)
    }
    /** Reproduce automaticamente la música **/
    private fun musicClicked(applicationContext: Context){
        val lastIndex = uiState.value.uiMusicList.lastIndex
        val indexRandom = (0..lastIndex).toList().shuffled()
        var shuffle: Boolean
        mediaPlayer.setOnCompletionListener {
            shuffle = uiState.value.uiIsShuffle
            val repeat = uiState.value.uiRepeat
            val index2 = uiState.value.uiCurrentMusicListIndex
            val indexAux = uiState.value.uiCountIndex
            setUiIsCompletion(true)
            if(repeat == RepeatOptions.Current && !shuffle){
                //Si repetir la misma música esta activa
                setChangeIndex(index2)
                refreshMusic()
                playClicked(applicationContext)
            }else if(shuffle && repeat == RepeatOptions.Current){
                //Si ambos estan activos caso 1
                setChangeIndex(index2)
                refreshMusic()
                playClicked(applicationContext)
            }else{
                if (shuffle && repeat == RepeatOptions.All) {
                    //Si ambos estan activos caso 2
                    setChangeIndex(indexRandom[indexAux])
                    refreshMusic()
                    playClicked(applicationContext)
                    setUiCountIndex(indexAux + 1)
                } else if (shuffle && repeat == RepeatOptions.Off) {
                    //Si aleatorio esta activo
                    setChangeIndex(indexRandom[indexAux])
                    refreshMusic()
                    playClicked(applicationContext)
                    setUiCountIndex(indexAux + 1)
                } else if (repeat == RepeatOptions.All && !shuffle) {
                    //Si repetir toda la lista esta activa
                    setChangeIndex(index2 + 1)
                    refreshMusic()
                    playClicked(applicationContext)
                } else MediaPlayerManager.setUiIsPause(true) //Si ninguno esta activo
            }
        }
    }
    /** Reproduce o pausa la música actual **/
    fun playClicked(applicationContext: Context){
        musicClicked(applicationContext)
        startServiceMusicPlayer(applicationContext)
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            MediaPlayerManager.setUiIsPause(true)
        } else {
            mediaPlayer.start()
            MediaPlayerManager.setUiIsPause(false)
            setUiAutoDurationValue()
        }
    }
    /** Obtiene música del dispositivo móvil del usuario **/
    private suspend fun getArtistList(applicationContext: Context){
        //Realiza la consulta en un hilo de fondo para optimizar el rendimiento
        val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val subUri = Uri.parse("content://media/external/audio/albumart")
        //Define la información a obtener
        val projection = arrayOf(
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val sortOrder = "${MediaStore.Audio.Media.TITLE} DESC"
        val artistList = mutableListOf<MusicListModel>()
        applicationContext.contentResolver.query(
            musicUri,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            //Obtiene la información de los audios del dispositivo móvil
            val musicPathColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val musicDurationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val musicNameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistNameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val albumNameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            while (cursor.moveToNext()) {
                val thisMusicPath = cursor.getString(musicPathColumn)
                val thisMusicDuration = cursor.getInt(musicDurationColumn)
                val thisMusicName = cursor.getString(musicNameColumn)
                val thisArtistName = cursor.getString(artistNameColumn)
                val thisAlbumName = cursor.getString(albumNameColumn)
                val thisAlbumId = cursor.getLong(albumIdColumn)
                val albumUri = ContentUris.withAppendedId(subUri, thisAlbumId).toString()
                val index = artistList.indexOfLast { it.name == thisArtistName }
                if (index == -1) {
                    //Si no hay el artista indicado
                    val musicList = listOf(
                        MusicModel(
                            musicPath = thisMusicPath,
                            musicDuration = thisMusicDuration,
                            musicDurationFormat = durationFormat(thisMusicDuration),
                            musicName = thisMusicName,
                            artistName = thisArtistName,
                            albumName = thisAlbumName,
                            albumUri = albumUri
                        )
                    )
                    artistList.add(
                        MusicListModel(
                            name = thisArtistName,
                            musicList = musicList
                        )
                    )
                } else {
                    //Si hay el artista indicado
                    val musicList = artistList[index].musicList.toMutableList()
                    musicList.add(
                        MusicModel(
                            musicPath = thisMusicPath,
                            musicDuration = thisMusicDuration,
                            musicDurationFormat = durationFormat(thisMusicDuration),
                            musicName = thisMusicName,
                            artistName = thisArtistName,
                            albumName = thisAlbumName,
                            albumUri = albumUri
                        )
                    )
                    artistList[index] = MusicListModel(
                        name = thisArtistName,
                        musicList = musicList
                    )
                }
            }
        }
        withContext(Dispatchers.Main){ setUiArtistList(artistList) }
    }
}