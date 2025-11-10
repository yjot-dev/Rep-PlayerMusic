package com.yjotdev.playermusic.application.mvvm.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
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
import com.yjotdev.playermusic.domain.usecase.media_player.SeekTrackUseCase
import com.yjotdev.playermusic.domain.usecase.config.ConfigUseCase
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.domain.entity.PlayerEntity
import com.yjotdev.playermusic.domain.entity.RepeatOptions
import com.yjotdev.playermusic.application.mvvm.model.PlayerMusicModel
import com.yjotdev.playermusic.domain.usecase.media_player.OnTrackCompletionUseCase

@HiltViewModel
class PlayerMusicViewModel @Inject constructor(
    private val insertPlayListUseCase: InsertPlayListUseCase,
    private val updatePlayListUseCase: UpdatePlayListUseCase,
    private val deletePlayListUseCase: DeletePlayListUseCase,
    private val getPlayListUseCase: GetPlayListUseCase,
    private val getArtistListUseCase: GetArtistListUseCase,
    private val configUseCase: ConfigUseCase,
    private val playTrackUseCase: PlayTrackUseCase,
    private val pauseTrackUseCase: PauseTrackUseCase,
    private val resumeTrackUseCase: ResumeTrackUseCase,
    private val nextTrackUseCase: NextTrackUseCase,
    private val previousTrackUseCase: PreviousTrackUseCase,
    private val seekTrackUseCase: SeekTrackUseCase,
    private val onTrackCompletion: OnTrackCompletionUseCase,
    getPlayerStateUseCase: GetPlayerStateUseCase
): ViewModel(){
    //Estados mutables del ViewModel
    private val _uiState = MutableStateFlow(PlayerMusicModel())
    //Estados de solo lectura del ViewModel
    val uiState = _uiState.asStateFlow()
    //Estado del reproductor
    val playerState: StateFlow<PlayerEntity> = getPlayerStateUseCase()

    init {
        loadData()
        observePlayerCompletion()
    }
    /** Obtiene la lista de música del dispositivo móvil del usuario **/
    fun loadData(){
        viewModelScope.launch{
            val artistList = getArtistListUseCase()
            val playListFlow = getPlayListUseCase()
            playListFlow.collect{ playList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        artistList = artistList,
                        playList = playList
                    )
                }
            }
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
        playTrackUseCase(track, currentList)
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
    fun onSeekTrack(position: Int) {
        seekTrackUseCase(position)
    }
    /** Ir a la siguiente musica **/
    fun onNextTrack() {
        nextTrackUseCase(_uiState.value.repeat)
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
    /** Observa el estado del reproductor y reacciona cuando una canción termina */
    private fun observePlayerCompletion() {
        viewModelScope.launch{
            playerState.collect { state ->
                if (state.hasCompleted) {
                    onTrackCompletion(_uiState.value.repeat)
                }
            }
        }
    }
}