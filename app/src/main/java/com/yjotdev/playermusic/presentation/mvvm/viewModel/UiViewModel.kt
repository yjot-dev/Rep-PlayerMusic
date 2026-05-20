package com.yjotdev.playermusic.presentation.mvvm.viewModel

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
import com.yjotdev.playermusic.domain.usecase.playlist.DeletePlaylistUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.GetPlaylistUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.InsertPlaylistUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.UpdatePlaylistUseCase
import com.yjotdev.playermusic.domain.usecase.artist_list.GetArtistListUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.GetPlayerStateUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.NextTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.PauseTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.PlayTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.PreviousTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.ResumeTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.SeekToUseCase
import com.yjotdev.playermusic.domain.usecase.config.ConfigUseCase
import com.yjotdev.playermusic.domain.model.MusicListModel
import com.yjotdev.playermusic.domain.model.MusicModel
import com.yjotdev.playermusic.domain.model.PlayerModel
import com.yjotdev.playermusic.domain.utils.RepeatOptions
import com.yjotdev.playermusic.presentation.mvvm.state.UiState

@HiltViewModel
class UiViewModel @Inject constructor(
    private val insertPlayListUseCase: InsertPlaylistUseCase,
    private val updatePlayListUseCase: UpdatePlaylistUseCase,
    private val deletePlayListUseCase: DeletePlaylistUseCase,
    getPlayListUseCase: GetPlaylistUseCase,
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
    private val _artistListState = MutableStateFlow<List<MusicListModel>>(emptyList())
    // Para el Flow reactivo de Room.
    private val _playListState = getPlayListUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    // Para el resto de estados de la UI.
    private val _uiState = MutableStateFlow(UiState())
    // Estado unificado de la UI
    val uiState: StateFlow<UiState> = combine(
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
        initialValue = UiState()
    )
    //Estado del reproductor
    private val _playerState = getPlayerStateUseCase()
    val playerState: StateFlow<PlayerModel> = _playerState

    init { getConfig() }

    /** Carga los datos no reactivos del listado de artistas **/
    fun loadArtistList() {
        viewModelScope.launch {
            _artistListState.value = getArtistListUseCase()
        }
    }

    /** Obtiene la lista de música seleccionada del artista **/
    fun setArtistListSelected(artist: MusicListModel){
        _uiState.update { currentState ->
            currentState.copy(selectedArtistList = artist)
        }
    }
    /** Obtiene la lista de música seleccionada de la playlist **/
    fun setPlayListSelected(playlist: MusicListModel){
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
    fun toggleSongSelection(song: MusicModel) {
        _uiState.update { currentState ->
            val currentSelected = currentState.itemSelected.toMutableList()
            if (currentSelected.contains(song)) {
                currentSelected.remove(song)
            } else {
                currentSelected.add(song)
            }
            currentState.copy(itemSelected = currentSelected)
        }
    }
    /** Agrega las canciones seleccionadas a una playlist **/
    fun addMusicsToPlaylist(name: String): Int {
        val playlist = uiState.value.playList
        val selectedSongs = uiState.value.itemSelected
        val existIndex = playlist.indexOfLast { it.name == name }
        return if(existIndex == -1){
            if(name.isEmpty()){
                1
            }else{
                //Caso 1: Agrego músicas a una nueva playlist
                val item = MusicListModel(
                    name = name,
                    musicList = selectedSongs
                )
                insertPlayList(item)
                cleanItemSelected()
                2
            }
        }else{
            //Caso 2: Agrego músicas a una playlist existente
            val existingPlaylist = playlist[existIndex]
            val updatedMusicList = existingPlaylist.musicList.toMutableList()
            updatedMusicList.addAll(selectedSongs)
            val item = existingPlaylist.copy(
                musicList = updatedMusicList
            )
            updatePlayList(item)
            cleanItemSelected()
            3
        }
    }
    /** Elimina las canciones seleccionadas de una playlist **/
    fun removeMusicsFromPlaylist(playList: MusicListModel){
        val removeMusicFromList = playList.musicList.toMutableList()
        val selectedSongs = uiState.value.itemSelected
        removeMusicFromList.removeAll(selectedSongs)
        val item = MusicListModel(
            id = playList.id,
            name = playList.name,
            musicList = removeMusicFromList
        )
        setPlayListSelected(item)
        updatePlayList(item)
        cleanItemSelected()
    }
    /** Edita el nombre de una playlist **/
    fun editNameFromPlaylist(playlistName: String, playList: MusicListModel){
        val item = MusicListModel(
            id = playList.id,
            name = playlistName,
            musicList = playList.musicList
        )
        updatePlayList(item)
    }
    /** Crea e inserta una lista de reproducción en la BD local **/
    private fun insertPlayList(item: MusicListModel){
        viewModelScope.launch{ insertPlayListUseCase(item) }
    }
    /** Actualiza una lista de reproducción en la BD local **/
    private fun updatePlayList(item: MusicListModel){
        viewModelScope.launch{ updatePlayListUseCase(item) }
    }
    /** Elimina una lista de reproducción en la BD local **/
    fun deletePlayList(item: MusicListModel){
        viewModelScope.launch{ deletePlayListUseCase(item) }
    }
    /** Tocar musica **/
    fun onPlayTrack(track: MusicModel) {
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