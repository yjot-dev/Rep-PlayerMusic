package com.example.playermusic.ui.viewModel

import android.content.Context
import com.example.playermusic.data.ObjectsManager
import com.example.playermusic.ui.model.MusicListModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatabaseProcess {
    //Inicializa viewmodel
    private val mvPlayerMusic by lazy{ ObjectsManager.getVmPlayerMusic() }
    private val uiState = mvPlayerMusic.uiState
    //Inicializa MediaPlayerProcess
    private val mpProcess by lazy{ MediaPlayerProcess() }
    //Inicializa base de datos para guardar las playlist
    private val db by lazy{ ObjectsManager.getBD().playListDao() }

    /** Obtiene las listas de reproducción guardadas en la BD local **/
    fun getPlayList() = db.getPlayList()

    fun playListFlowToList(): List<MusicListModel>{
        var playlist = emptyList<MusicListModel>()
        CoroutineScope(Dispatchers.IO).launch {
            getPlayList().collect{ list ->
                playlist = withContext(Dispatchers.Main){ list }
            }
        }
        return playlist
    }
    /** Crea e inserta una lista de reproducción en la BD local **/
    fun insertPlayList(item: MusicListModel){
        CoroutineScope(Dispatchers.IO).launch {
            db.insertPlayList(item)
        }
    }
    /** Actualiza una lista de reproducción en la BD local **/
    fun updatePlayList(item: MusicListModel){
        CoroutineScope(Dispatchers.IO).launch {
            db.updatePlayList(item)
        }
    }
    /** Elimina una lista de reproducción en la BD local **/
    fun deletePlayList(item: MusicListModel){
        CoroutineScope(Dispatchers.IO).launch {
            db.deletePlayList(item)
        }
    }
    /** Guarda datos persistentes para botones aleatorio y repetir **/
    fun saveConfig(
        applicationContext: Context,
        valueRepeat: Int? = null,
        isShuffle: Boolean? = null,
        isRestartApp: Boolean? = null,
        isPlayList: Boolean? = null,
        index0: Int? = null,
        index1: Int? = null,
        index2: Int? = null
    ){
        val sp = applicationContext.getSharedPreferences("MyConfig", Context.MODE_PRIVATE)
        sp.edit().apply {
            if(valueRepeat != null) putInt("repeat", valueRepeat)
            if(isShuffle != null) putBoolean("isShuffle", isShuffle)
            if(isRestartApp != null) putBoolean("restartApp", isRestartApp)
            if(isPlayList != null) putBoolean("isPlayList", isPlayList)
            if(index0 != null) putInt("index0", index0)
            if(index1 != null) putInt("index1", index1)
            if(index2 != null) putInt("index2", index2)
            apply()
        }
    }
    /** Obtiene datos persistentes de botones aleatorio y repetir **/
    private suspend fun getConfig(applicationContext: Context){
        val sp = applicationContext.getSharedPreferences("MyConfig", Context.MODE_PRIVATE)
        sp.apply {
            withContext(Dispatchers.Main){
                mvPlayerMusic.setUiRepeat(getInt("repeat", 2))
                mvPlayerMusic.setUiIsShuffle(getBoolean("isShuffle", false))
                mvPlayerMusic.setUiIsRestartApp(getBoolean("restartApp", false))
                mvPlayerMusic.setUiIsPlayList(getBoolean("isPlayList", false))
                mvPlayerMusic.setUiCurrentArtistListIndex(getInt("index0", 0))
                mvPlayerMusic.setUiCurrentPlayListIndex(getInt("index1", 0))
                //Crea una MusicList con los indices anteriores segun corresponda
                if(uiState.value.uiIsPlayList){
                    val playList = playListFlowToList()
                    if(playList.isNotEmpty()){
                        mvPlayerMusic.setUiMusicList(
                            playList[uiState.value.uiCurrentPlayListIndex].musicList
                        )
                    }
                }else{
                    if(uiState.value.uiArtistList.isNotEmpty()){
                        mvPlayerMusic.setUiMusicList(
                            uiState.value.uiArtistList[uiState.value.uiCurrentArtistListIndex].musicList
                        )
                    }
                }
                //Obtiene el indice de musica actual de la MusicList
                mvPlayerMusic.setUiCurrentMusicListIndex(getInt("index2", 0))
            }
        }
    }

    fun getData(applicationContext: Context){
        CoroutineScope(Dispatchers.IO).launch{
            launch {
                mpProcess.getArtistList(applicationContext)
                getPlayList()
            }.join()
            launch {
                getConfig(applicationContext)
            }
        }
    }
}