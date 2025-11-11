package com.yjotdev.playermusic.application.mvvm.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import com.yjotdev.playermusic.domain.usecase.playlist.DeletePlayListUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.GetPlayListUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.InsertPlayListUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.UpdatePlayListUseCase
import com.yjotdev.playermusic.domain.usecase.artist_list.GetArtistListUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.GetPlayerStateUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.NextTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.PauseTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.PlayTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.PreviousTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.ResumeTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.SeekToUseCase
import com.yjotdev.playermusic.domain.usecase.config.ConfigUseCase
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.domain.entity.PlayerEntity
import com.yjotdev.playermusic.domain.entity.RepeatOptions
import com.yjotdev.playermusic.application.mvvm.model.PlayerMusicModel

@HiltViewModel
class PlayerMusicViewModel @Inject constructor(
    private val insertPlayListUseCase: InsertPlayListUseCase,
    private val updatePlayListUseCase: UpdatePlayListUseCase,
    private val deletePlayListUseCase: DeletePlayListUseCase,
    getPlayListUseCase: GetPlayListUseCase,
    private val getArtistListUseCase: GetArtistListUseCase,
    private val configUseCase: ConfigUseCase,
    private val playTrackUseCase: PlayTrackUseCase,
    private val pauseTrackUseCase: PauseTrackUseCase,
    private val resumeTrackUseCase: ResumeTrackUseCase,
    private val nextTrackUseCase: NextTrackUseCase,
    private val previousTrackUseCase: PreviousTrackUseCase,
    private val seekToUseCase: SeekToUseCase,
    getPlayerStateUseCase: GetPlayerStateUseCase
): ViewModel(){
    // Para datos que no son un Flow continuo
    private val _artistListState = MutableStateFlow<List<MusicListEntity>>(emptyList())
    // Para el Flow reactivo de Room.
    private val _playListState: StateFlow<List<MusicListEntity>> = getPlayListUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    // Para el resto de estados de la UI.
    private val _uiState = MutableStateFlow(PlayerMusicModel())
    // ESTADO UNIFICADO PARA LA UI
    val uiState: StateFlow<PlayerMusicModel> = combine(
        _artistListState,
        _playListState,
        _uiState
    ) { artistList, playList, model ->
        model.copy(
            artistList = artistList,
            playList = playList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlayerMusicModel()
    )
    //Estado del reproductor
    val playerState: StateFlow<PlayerEntity> = getPlayerStateUseCase()

    init {
        loadArtistList()
        getConfig()
    }

    /** Carga los datos no reactivos del listado de artistas **/
    private fun loadArtistList() {
        viewModelScope.launch {
            _artistListState.value = getArtistListUseCase()
        }
    }

    /** Obtiene la lista de música seleccionada del artista **/
    fun setArtistListSelected(artist: MusicListEntity){
        _uiState.update { currentState ->
            currentState.copy(selectedArtistList = artist)
        }
    }
    /** Obtiene la lista de música seleccionada de la playlist **/
    fun setPlayListSelected(playlist: MusicListEntity){
        _uiState.update { currentState ->
            currentState.copy(selectedPlaylist = playlist)
        }
    }
    /** Actualiza estado de repetir **/
    fun setRepeat(value: Int){
        val v = when(value){
            0 -> { RepeatOptions.Current }
            1 -> { RepeatOptions.All }
            else -> { RepeatOptions.Shuffle }
        }
        _uiState.update { currentState ->
            currentState.copy(repeat = v)
        }
    }
    /** Actualiza estado del condicional si se reinicia la app **/
    fun setIsRestartApp(value: Boolean){
        _uiState.update { currentState ->
            currentState.copy(isRestartApp = value)
        }
    }
    /** Actualiza estado del condicional si es playlist **/
    fun setIsPlayList(value: Boolean){
        _uiState.update { currentState ->
            currentState.copy(isPlayList = value)
        }
    }
    /** Limpia la selección de las canciones **/
    fun cleanItemSelected(){
        _uiState.update { currentState ->
            currentState.copy(itemSelected = emptyList())
        }
    }
    /** Gestiona la selección de las canciones **/
    fun toggleSongSelection(song: MusicEntity) {
        val currentSelected = _uiState.value.itemSelected.toMutableList()
        if (currentSelected.contains(song)) {
            currentSelected.remove(song)
        } else {
            currentSelected.add(song)
        }
        _uiState.update { it.copy(itemSelected = currentSelected) }
    }
    /** Agrega las canciones seleccionadas a una playlist **/
    fun addSelectedSongsToPlaylist(name: String): Int {
        val playlist = _uiState.value.playList
        val existIndex = playlist.indexOfLast { it.name == name }
        return if(existIndex == -1){
            if(name.isEmpty()){
                1
            }else{
                //Caso 1: Agrego músicas a una nueva playlist
                val item = MusicListEntity(
                    name = name,
                    musicList = _uiState.value.itemSelected
                )
                insertPlayList(item)
                // Limpiar la selección después de la operación
                _uiState.update { it.copy(itemSelected = emptyList()) }
                2
            }
        }else{
            //Caso 2: Agrego músicas a una playlist existente
            val list = playlist[existIndex].musicList.toMutableList()
            list.addAll(_uiState.value.itemSelected)
            val item = MusicListEntity(
                id = playlist[existIndex].id,
                name = playlist[existIndex].name,
                musicList = list
            )
            updatePlayList(item)
            // Limpiar la selección después de la operación
            _uiState.update { it.copy(itemSelected = emptyList()) }
            3
        }
    }
    /** Elimina las canciones seleccionadas de una playlist **/
    fun removeSelectedSongsFromPlaylist(playList: MusicListEntity){
        val list = playList.musicList.toMutableList()
        list.removeAll(_uiState.value.itemSelected)
        val item = MusicListEntity(
            id = playList.id,
            name = playList.name,
            musicList = list
        )
        updatePlayList(item)
        // Limpiar la selección después de la operación
        _uiState.update { it.copy(itemSelected = emptyList()) }
    }
    /** Crea e inserta una lista de reproducción en la BD local **/
    fun insertPlayList(item: MusicListEntity){
        viewModelScope.launch{ insertPlayListUseCase(item) }
    }
    /** Actualiza una lista de reproducción en la BD local **/
    fun updatePlayList(item: MusicListEntity){
        viewModelScope.launch{ updatePlayListUseCase(item) }
    }
    /** Elimina una lista de reproducción en la BD local **/
    fun deletePlayList(item: MusicListEntity){
        viewModelScope.launch{ deletePlayListUseCase(item) }
    }
    /** Tocar musica **/
    fun onPlayTrack(track: MusicEntity) {
        val currentList = if(_uiState.value.isPlayList){
            _uiState.value.selectedPlaylist?.musicList ?: emptyList()
        }else {
            _uiState.value.selectedArtistList?.musicList ?: emptyList()
        }
        playTrackUseCase(track, currentList, _uiState.value.repeat)
    }
    /** Pausar musica **/
    fun onPauseTrack() {
        pauseTrackUseCase()
    }
    /** Reanudar musica **/
    fun onResumeTrack() {
        resumeTrackUseCase()
    }
    /** Obtener posicion de la musica **/
    fun onSeekTo(position: Float) {
        val positionToInt = (position * 1000).toInt() //Equivalente en milisegundos
        seekToUseCase(positionToInt)
    }
    /** Ir a la siguiente musica **/
    fun onNextTrack() {
        nextTrackUseCase()
    }
    /** Ir a la musica anterior **/
    fun onPreviousTrack() {
        previousTrackUseCase()
    }
    /** Guarda las configuraciones del usuario **/
    fun saveConfig(){
        val state = _uiState.value
        configUseCase.invoke(
            valueRepeat = state.repeat,
            isPlayList = state.isPlayList
        )
    }
    /** Obtiene las configuraciones del usuario **/
    fun getConfig(){
        val config = configUseCase.invoke()
        setRepeat(config["repeat"] as Int)
        setIsPlayList(config["isPlayList"] as Boolean)
    }
}