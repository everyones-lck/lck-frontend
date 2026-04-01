package every.lol.com.feature.home

import every.lol.com.feature.home.model.HomeIntent
import every.lol.com.feature.home.model.HomeUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import moe.tlaster.precompose.viewmodel.ViewModel

sealed interface HomeEvent{
    data class ShowToast(val message: String): HomeEvent
}

class HomeViewModel(

): ViewModel(){

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<HomeEvent>(replay = 0)
    val event = _event.asSharedFlow()

    init {
        onIntent(HomeIntent.LoadInitial)
    }

    fun onIntent(intent: HomeIntent){
        when(intent){
            HomeIntent.LoadInitial -> loadInitial()
            HomeIntent.RefreshHome -> refreshHome()
            else -> {}
        }
    }

    private fun loadInitial(){

    }

    private fun refreshHome(){

    }



}
