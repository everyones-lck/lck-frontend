package every.lol.com.feature.home

import every.lol.com.core.domain.usecase.GetHomeAlertsUseCase
import every.lol.com.core.domain.usecase.GetHomeNewsUseCase
import every.lol.com.core.domain.usecase.GetHomeRankingUseCase
import every.lol.com.core.domain.usecase.GetHomeTodayMatchUseCase
import every.lol.com.core.domain.usecase.GetMatchVoteRateUseCase
import every.lol.com.core.domain.usecase.GetSupportTeamUseCase
import every.lol.com.feature.home.model.HomeIntent
import every.lol.com.feature.home.model.HomeUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

sealed interface HomeEvent{
    data class ShowToast(val message: String): HomeEvent
}

class HomeViewModel(
    private val getHomeTodayMatchUseCase: GetHomeTodayMatchUseCase,
    private val getHomeRankingUseCase: GetHomeRankingUseCase,
    private val getSupportTeamUseCase: GetSupportTeamUseCase,
    private val getHomeNewsUseCase: GetHomeNewsUseCase,
    private val getHomeAlertsUseCase: GetHomeAlertsUseCase,
    private val getMatchVotestUseCase: GetMatchVoteRateUseCase
    ): ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<HomeEvent>(replay = 0)
    val event = _event.asSharedFlow()

    init {
        onIntent(HomeIntent.LoadInitial)
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadInitial -> loadInitial()
            HomeIntent.RefreshHome -> refreshHome()
            HomeIntent.LoadTodayMatchHome -> loadTodayMatchHome()
            else -> {}
        }
    }

    private var isInitialLoaded = false

    private fun loadInitial() {
        if (isInitialLoaded) return
        isInitialLoaded = true

        println("loadInitial")
        loadTodayMatchHome()
        loadRanking()
        loadNews()
        loadAlerts()
    }

    private fun refreshHome() {

    }

    private fun loadTodayMatchHome() {
        val state = _uiState.value
        if (state is HomeUiState.Home && state.isLoading) return

        _uiState.update { state ->
            when (state) {
                is HomeUiState.Home -> state.copy(isLoading = true)
                else -> HomeUiState.Home(isLoading = true)
            }
        }

        viewModelScope.launch {
            getHomeTodayMatchUseCase().onSuccess { result ->
                _uiState.update { state ->
                    val currentState = state as? HomeUiState.Home ?: HomeUiState.Home()

                    currentState.copy(
                        isLoading = false,
                        matches = result,
                        isRefreshing = false
                    )
                }
                result.matches.forEach { match ->
                    loadMatchVoteRate(match.matchId)
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    val currentState = state as? HomeUiState.Home ?: HomeUiState.Home()
                    currentState.copy(isLoading = false)
                }
                isInitialLoaded = false
                println(error)
                _event.emit(HomeEvent.ShowToast(error.message ?: "데이터를 불러오지 못했습니다."))
            }
        }
    }

    private fun loadMatchVoteRate(matchId: Long) {
        viewModelScope.launch {
            getMatchVotestUseCase(matchId).onSuccess { rate ->
                _uiState.update { state ->
                    val currentState = state as? HomeUiState.Home ?: HomeUiState.Home()
                    val updatedRates = currentState.matchRates.toMutableMap().apply {
                        put(matchId, rate)
                    }

                    currentState.copy(
                        matchRates = updatedRates
                    )
                }
            }.onFailure {
                _uiState.update { state ->
                    val currentState = state as? HomeUiState.Home ?: HomeUiState.Home()
                    currentState.copy(isLoading = false)
                }
                isInitialLoaded = false
                println(it)
            }
        }
    }

    private fun loadRanking() {
        viewModelScope.launch {
            getHomeRankingUseCase().onSuccess { result ->
                getSupportTeamUseCase().onSuccess { supportTeam ->
                    _uiState.update { state ->
                        val currentState = state as? HomeUiState.Home ?: HomeUiState.Home()
                        currentState.copy(
                            isLoading = false,
                            ranking = result,
                            supportTeam = supportTeam
                        )
                    }
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    val currentState = state as? HomeUiState.Home ?: HomeUiState.Home()
                    currentState.copy(isLoading = false)
                }
                isInitialLoaded = false
                println(error)
                _event.emit(HomeEvent.ShowToast(error.message ?: "데이터를 불러오지 못했습니다."))
            }
        }

    }

    private fun loadNews() {
        viewModelScope.launch {
            getHomeNewsUseCase().onSuccess { result ->
                _uiState.update { state ->
                    val currentState = state as? HomeUiState.Home ?: HomeUiState.Home()
                    currentState.copy(
                        isLoading = false,
                        news = result,
                        isRefreshing = false
                    )
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    val currentState = state as? HomeUiState.Home ?: HomeUiState.Home()
                    currentState.copy(isLoading = false)
                }
                isInitialLoaded = false
                println(error)
                _event.emit(HomeEvent.ShowToast(error.message ?: "데이터를 불러오지 못했습니다."))
            }
        }
    }

    private fun loadAlerts(){
        viewModelScope.launch {
            getHomeAlertsUseCase().onSuccess { result ->
                _uiState.update { state ->
                    val currentState = state as? HomeUiState.Home ?: HomeUiState.Home()
                    currentState.copy(
                        isLoading = false,
                        alerts = result,
                        isRefreshing = false,
                        alertsMessage = result.alerts.firstOrNull()?.message.orEmpty()
                    )
                }
            }.onFailure {
                _uiState.update {
                    val currentState = it as? HomeUiState.Home ?: HomeUiState.Home()
                    currentState.copy(isLoading = false)
                }
                isInitialLoaded = false
                println(it)
            }
        }
    }

}