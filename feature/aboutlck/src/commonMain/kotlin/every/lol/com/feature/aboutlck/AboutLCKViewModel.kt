package every.lol.com.feature.aboutlck

import every.lol.com.feature.aboutlck.model.AboutLCKIntent
import every.lol.com.feature.aboutlck.model.AboutLCKUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import moe.tlaster.precompose.viewmodel.ViewModel

sealed interface AboutLCKEvent{
    data class ShowToast(val message: String): AboutLCKEvent
}


class AboutLCKViewModel(

) : ViewModel() {

    private val _uiState = MutableStateFlow<AboutLCKUiState>(AboutLCKUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<AboutLCKEvent>(replay = 0)
    val event = _event.asSharedFlow()

    fun onIntent(intent: AboutLCKIntent) {
        when (intent) {
            AboutLCKIntent.LoadInitial -> loadInitial()
            else -> {}
        }
    }

    private fun loadInitial() {

    }

}