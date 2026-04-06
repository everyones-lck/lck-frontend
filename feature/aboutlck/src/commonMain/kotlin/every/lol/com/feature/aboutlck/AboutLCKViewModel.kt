package every.lol.com.feature.aboutlck

import every.lol.com.core.domain.usecase.GetHomeRankingUseCase
import every.lol.com.core.domain.usecase.GetSupportTeamUseCase
import every.lol.com.core.domain.usecase.aboutlck.GetAboutLCKMatchUseCase
import every.lol.com.feature.aboutlck.model.AboutLCKIntent
import every.lol.com.feature.aboutlck.model.AboutLCKUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

sealed interface AboutLCKEvent{
    data class ShowToast(val message: String): AboutLCKEvent
}


class AboutLCKViewModel(
    private val getAboutLCKMatchUseCase: GetAboutLCKMatchUseCase,
    private val getHomeRankingUseCase: GetHomeRankingUseCase,
    private val getSupportTeamUseCase: GetSupportTeamUseCase
    ) : ViewModel() {

    private val _uiState = MutableStateFlow<AboutLCKUiState>(AboutLCKUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<AboutLCKEvent>(replay = 0)
    val event = _event.asSharedFlow()


    init {
        onIntent(AboutLCKIntent.LoadInitial)
    }

    fun onIntent(intent: AboutLCKIntent) {
        when (intent) {
            AboutLCKIntent.LoadInitial -> loadInitial()
            else -> {}
        }
    }

    private var isInitialLoaded = false

    private fun loadInitial() {
        if (isInitialLoaded) return
        isInitialLoaded = true

        println("loadInitial")
        getMatch("2026-04-05")
        loadRanking()
    }

    private fun getMatch(date:String){
        viewModelScope.launch {
            getAboutLCKMatchUseCase(date).onSuccess { result ->
                _uiState.update { state ->
                    val currentState = state as? AboutLCKUiState.AboutLCK ?:AboutLCKUiState.AboutLCK()
                    currentState.copy(
                        isLoading = false,
                        match = result
                    )
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    val currentState = state as? AboutLCKUiState.AboutLCK ?:AboutLCKUiState.AboutLCK()
                    currentState.copy(isLoading = false)
                }
                isInitialLoaded = false
                println(error)
                _event.emit(AboutLCKEvent.ShowToast("잠시 후 다시 시도해주세요."))
            }
        }
    }

    private fun loadRanking() {
        viewModelScope.launch {
            getHomeRankingUseCase().onSuccess { result ->
                getSupportTeamUseCase().onSuccess { supportTeam ->
                    _uiState.update { state ->
                        val currentState = state as? AboutLCKUiState.AboutLCK ?:AboutLCKUiState.AboutLCK()
                        currentState.copy(
                            isLoading = false,
                            ranking = result,
                            supportTeam = supportTeam
                        )
                    }
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    val currentState = state as? AboutLCKUiState.AboutLCK ?:AboutLCKUiState.AboutLCK()
                    currentState.copy(isLoading = false)
                }
                isInitialLoaded = false
                println(error)
                _event.emit(AboutLCKEvent.ShowToast("잠시 후 다시 시도해주세요."))
            }
        }

    }

}