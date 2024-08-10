package com.example.playermusic.ui.viewModel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playermusic.MainApplication
import com.example.playermusic.service.MusicService
import com.example.playermusic.R
import com.example.playermusic.data.MediaPlayerManager
import com.example.playermusic.ui.model.MusicListModel
import com.example.playermusic.ui.model.MusicModel
import com.example.playermusic.ui.model.UiPlayerMusicModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerMusicViewModel: ViewModel(){
    //Estados mutables del ViewModel
    private val _uiState = MutableStateFlow(UiPlayerMusicModel())
    //Estados de solo lectura del ViewModel
    val uiState: StateFlow<UiPlayerMusicModel> = _uiState.asStateFlow()
    //Inicializa reproductor de multimedia
    private val mp by lazy{ MediaPlayerManager.getMediaPlayer() }
    //Inicializa lista de indices aleatorios no repetibles
    private val listRandom by lazy{
        (0..uiState.value.uiMusicList.lastIndex).toList().shuffled()
    }
    //Base de datos para guardar las playlist
    private val db by lazy{ MainApplication.database.playListDao() }

    override fun onCleared() {
        super.onCleared()
        MediaPlayerManager.releaseMediaPlayer()
        resetPlayerMusic()
        MainApplication.database.close()
    }

    /** Metodos de estados **/
    fun setUiArtistList(valueList: List<MusicListModel>){
        _uiState.update { currentState ->
            currentState.copy(uiArtistList = valueList)
        }
    }

    fun setUiMusicList(value: MusicListModel){
        val list1 = uiState.value.uiArtistList
        val index = list1.indexOf(value)
        setUiCurrentArtistIndex(index)
        _uiState.update { currentState ->
            currentState.copy(uiMusicList = value.musicList)
        }
    }

    private fun setUiPlayList(valueList: List<MusicListModel>){
        _uiState.update { currentState ->
            currentState.copy(uiPlayList = valueList)
        }
    }

    fun setUiPlayListMusic(value: MusicListModel){
        val list2 = uiState.value.uiPlayList
        val index = list2.indexOf(value)
        setUiCurrentPlayListIndex(index)
        _uiState.update { currentState ->
            currentState.copy(uiPlayListMusic = value.musicList)
        }
    }

    private fun setUiIsPause(value: Boolean){
        _uiState.update { currentState ->
            currentState.copy(uiIsPause = value)
        }
    }

    fun setUiIsShuffle(value: Boolean){
        _uiState.update { currentState ->
            currentState.copy(uiIsShuffle = value)
        }
    }

    fun setUiIsRepeat(value: Boolean){
        _uiState.update { currentState ->
            currentState.copy(uiIsRepeat = value)
        }
    }

    private fun setUiCountIndex(value: Int, isPlayList: Boolean){
        val list = if(isPlayList) uiState.value.uiPlayListMusic
        else uiState.value.uiMusicList
        val v = if(value == -1){ list.lastIndex }
        else{ value % list.size }
        _uiState.update { currentState ->
            currentState.copy(uiCountIndex = v)
        }
    }

    private fun setUiCurrentArtistIndex(value: Int){
        val list = uiState.value.uiArtistList
        val v = if(value == -1){ list.lastIndex }
        else{ value % list.size }
        _uiState.update { currentState ->
            currentState.copy(uiCurrentArtistIndex = v)
        }
    }

    private fun setUiCurrentPlayListIndex(value: Int){
        val list = uiState.value.uiPlayList
        val v = if(value == -1){ list.lastIndex }
        else{ value % list.size }
        _uiState.update { currentState ->
            currentState.copy(uiCurrentPlayListIndex = v)
        }
    }

    private fun setUiCurrentMusicIndex(value: Int, isPlayList: Boolean){
        val list = if(isPlayList) uiState.value.uiPlayListMusic
        else uiState.value.uiMusicList
        val v = if(value == -1){ list.lastIndex }
        else{ value % list.size }
        _uiState.update { currentState ->
            currentState.copy(uiCurrentMusicIndex = v)
        }
    }

    //Cambio manual de la duración
    fun setUiManualDurationValue(value: Int){
        mp.seekTo(value*1000)
        _uiState.update { currentState ->
            currentState.copy(uiCurrentDuration = value*1000)
        }
    }
    //Cambio automático de la duración
    fun setUiAutoDurationValue(isPlayList: Boolean){
        viewModelScope.launch(Dispatchers.IO){
            val index = uiState.value.uiCurrentMusicIndex
            val list = if(isPlayList) uiState.value.uiPlayListMusic
            else uiState.value.uiMusicList
            val totalDurationSec = list[index].musicDuration/1000
            val currentDurationSec = uiState.value.uiCurrentDuration/1000
            val condition = currentDurationSec < totalDurationSec
            while(condition){
                _uiState.update { currentState ->
                    currentState.copy(uiCurrentDuration = mp.currentPosition)
                }
                mp.setOnCompletionListener{ setUiIsPause(true) }
                delay(1000)
            }
        }
    }

    fun setUiPlayListName(value: String){
        _uiState.update{ currentState ->
            currentState.copy(uiPlayListName = value)
        }
    }

    fun setUiSelectedMusic(value: MusicModel){
        _uiState.update{ currentState ->
            currentState.copy(uiSelectedMusic = value)
        }
    }

    fun setUiFilter(value: List<MusicListModel>){
        _uiState.update{ currentState ->
            currentState.copy(uiFilter = value)
        }
    }

    private fun resetPlayerMusic(){
        _uiState.value = UiPlayerMusicModel()
    }

    /** Metodos de lógica de la app **/
    fun durationFormat(duration: Int) : String {
        var seconds = duration/1000
        val minutes = seconds/60
        seconds %= 60
        return if (seconds < 10) "$minutes:0$seconds"
        else "$minutes:$seconds"
    }

    private fun refreshMusic(isPlayList: Boolean){
        val index = uiState.value.uiCurrentMusicIndex
        val list = if(isPlayList) uiState.value.uiPlayListMusic
        else uiState.value.uiMusicList
        MediaPlayerManager.setCurrentMusic(
            musicName = list[index].musicName,
            artistName = list[index].artistName
        )
        mp.reset()
        mp.setDataSource(list[index].musicPath)
        mp.prepare()
    }

    fun musicClicked(index: Int, isPlayList: Boolean){
        setUiCurrentMusicIndex(index, isPlayList)
        setUiManualDurationValue(0)
        setUiIsPause(true)
        refreshMusic(isPlayList)
    }

    fun playClicked(){
        if(mp.isPlaying) {
            mp.pause()
            setUiIsPause(true)
        }
        else {
            mp.start()
            setUiIsPause(false)
        }
    }

    fun shuffleOrRepeatClicked(isPlayList: Boolean){
        val list = if(isPlayList) uiState.value.uiPlayListMusic
        else uiState.value.uiMusicList
        mp.setOnCompletionListener{
            if(uiState.value.uiCountIndex == list.lastIndex){
                listRandom.shuffled()
            }
            if(uiState.value.uiIsShuffle && !uiState.value.uiIsRepeat){
                //Si aleatorio esta activo
                val randomIndex = listRandom
                musicClicked(randomIndex[uiState.value.uiCountIndex], isPlayList)
                playClicked()
                if(uiState.value.uiCountIndex + 1 == list.size){
                    mp.stop()
                }else{
                    setUiCountIndex(uiState.value.uiCountIndex + 1, isPlayList)
                }
            }else if(!uiState.value.uiIsShuffle && uiState.value.uiIsRepeat){
                //Si repetir esta activo
                val index = uiState.value.uiCurrentMusicIndex
                musicClicked(index + 1, isPlayList)
                playClicked()
            }else if(uiState.value.uiIsShuffle && uiState.value.uiIsRepeat){
                //Si aleatorio y repetir estan activos
                val randomIndex = listRandom
                musicClicked(randomIndex[uiState.value.uiCountIndex], isPlayList)
                playClicked()
                setUiCountIndex(uiState.value.uiCountIndex + 1, isPlayList)
            }
        }
    }

    fun checkAlbumUri(
        applicationContext: Context,
        albumImageUri: String
    ) : String{
        val albumArtExists = try{
            applicationContext.contentResolver.openInputStream(albumImageUri.toUri())?.close()
            true
        }
        catch (e: Exception) { false }
        return if(albumArtExists){ albumImageUri }
        else{
            Uri.parse("android.resource://" +
                    "${applicationContext.packageName}/" +
                    "${R.drawable.album_art}").toString()
        }
    }

    fun startServiceMusicPlayer(applicationContext: Context){
        val intent = Intent(applicationContext, MusicService::class.java)
        ContextCompat.startForegroundService(applicationContext, intent)
    }

    /** Metodos de la BD de la app **/
    fun refreshPlayList(){
        viewModelScope.launch(Dispatchers.IO){
            val data = db.refreshPlayList()
            withContext(Dispatchers.Main){
                setUiPlayList(data)
                setUiFilter(data)
            }
        }
    }

    fun insertPlayList(item: MusicListModel){
        viewModelScope.launch(Dispatchers.IO) {
            db.insertPlayList(item)
            refreshPlayList()
        }
    }

    fun updatePlayList(item: MusicListModel){
        viewModelScope.launch(Dispatchers.IO) {
            db.updatePlayList(item)
            refreshPlayList()
        }
    }

    fun deletePlayList(item: MusicListModel){
        viewModelScope.launch(Dispatchers.IO) {
            db.deletePlayList(item)
            refreshPlayList()
        }
    }
}