package com.yjotdev.playermusic.ui.viewModel

import android.content.Context
import com.yjotdev.playermusic.data.ObjectsManager
import com.yjotdev.playermusic.ui.model.MusicListModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatabaseProcess {
    //Inicializa viewmodel
    private val mvPlayerMusic by lazy{ ObjectsManager.getVmPlayerMusic() }
    private val uiState = mvPlayerMusic.uiState.value
    //Inicializa MediaPlayerProcess
    private val mpProcess by lazy{ MediaPlayerProcess() }
    //Inicializa base de datos para guardar las playlist
    private val db by lazy{ ObjectsManager.getBD().playListDao() }

    /** Obtiene las listas de reproducción guardadas en la BD local **/
    private suspend fun playListFlowToList(){
        db.getPlayList().collect{ playList ->
            withContext(Dispatchers.Main){
                mvPlayerMusic.setUiPlayList(playList)
            }
        }
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
        isPlayList: Boolean? = null,
        index0: Int? = null,
        index1: Int? = null,
        index2: Int? = null
    ){
        val sp = applicationContext.getSharedPreferences("MyConfig", Context.MODE_PRIVATE)
        sp.edit().apply {
            if(valueRepeat != null) putInt("repeat", valueRepeat)
            if(isShuffle != null) putBoolean("isShuffle", isShuffle)
            if(isPlayList != null) putBoolean("isPlayList", isPlayList)
            if(index0 != null) putInt("index0", index0)
            if(index1 != null) putInt("index1", index1)
            if(index2 != null) putInt("index2", index2)
            apply()
        }
    }
    /** Obtiene datos persistentes de botones aleatorio y repetir **/
    private fun getConfig(applicationContext: Context){
        val artistList = uiState.uiArtistList
        val playList = uiState.uiPlayList
        val index0 = uiState.uiCurrentArtistListIndex
        val index1 = uiState.uiCurrentPlayListIndex
        val sp = applicationContext.getSharedPreferences("MyConfig", Context.MODE_PRIVATE)
        sp.apply {
            mvPlayerMusic.setUiRepeat(getInt("repeat", 2))
            mvPlayerMusic.setUiIsShuffle(getBoolean("isShuffle", false))
            mvPlayerMusic.setUiIsPlayList(getBoolean("isPlayList", false))
            mvPlayerMusic.setUiCurrentArtistListIndex(getInt("index0", 0))
            mvPlayerMusic.setUiCurrentPlayListIndex(getInt("index1", 0))
            //Crea una MusicList con los indices anteriores segun corresponda
            if(uiState.uiIsPlayList){
                if(playList.isNotEmpty()){
                    mvPlayerMusic.setUiMusicList(playList[index1].musicList)
                }
            }else{
                if(artistList.isNotEmpty()){
                    mvPlayerMusic.setUiMusicList(artistList[index0].musicList)
                }
            }
            //Obtiene el indice de musica actual
            mvPlayerMusic.setUiCurrentMusicListIndex(getInt("index2", 0))
        }
    }

    fun getData(applicationContext: Context){
        CoroutineScope(Dispatchers.IO).launch{
            launch {
                mpProcess.getArtistList(applicationContext)
                playListFlowToList()
            }.join()
            launch {
                getConfig(applicationContext)
            }
        }
    }
}