package every.lol.com.feature.aboutlck

import every.lol.com.core.domain.usecase.GetHomeRankingUseCase
import every.lol.com.core.domain.usecase.GetSupportTeamUseCase
import every.lol.com.core.domain.usecase.aboutlck.GetAboutLCKMatchUseCase
import every.lol.com.core.model.Ranking
import every.lol.com.core.model.aboutlck.match.AboutLCKMatch
import every.lol.com.core.model.aboutlck.match.MatchDetail
import every.lol.com.feature.aboutlck.model.AboutLCKIntent
import every.lol.com.feature.aboutlck.model.AboutLCKUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import kotlin.time.Clock
import kotlin.time.Instant

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
            is AboutLCKIntent.Match -> loadMatchPage(intent.matchId, intent.matchData)
            is AboutLCKIntent.ChangeDate -> loadDate(intent.date)
            else -> {}
        }
    }

    private var cachedRanking: Ranking? = null
    private var cachedMatch: AboutLCKMatch? = null
    private var cachedSupportTeam: List<Int>? = null
    private var cachedDate: String? = null
    private var isInitialLoaded = false

    private fun loadInitial() {

        _uiState.update { state ->
            if (state is AboutLCKUiState.AboutLCK) {
                state
            } else {
                AboutLCKUiState.AboutLCK(
                    ranking = cachedRanking,
                    match = cachedMatch,
                    supportTeam = cachedSupportTeam ?: emptyList(),
                    date = cachedDate ?: ""
                )
            }
        }

        if (isInitialLoaded) return

        isInitialLoaded = true
        loadDate()
        loadRanking()
    }

    private fun loadDate(date: String? = null) {
        val targetDate = if (date != null) {
            date
        } else {
            val nowMs = Clock.System.now().toEpochMilliseconds()
            val instant = Instant.fromEpochMilliseconds(nowMs)
            instant.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
        }
        cachedDate = targetDate

        _uiState.update { state ->
            val currentState = state as? AboutLCKUiState.AboutLCK ?: AboutLCKUiState.AboutLCK()
            currentState.copy(
                isLoading = true,
                date = targetDate
            )
        }
        getMatch(targetDate)
    }

    private fun loadMatchPage(matchId: Int, matchData: MatchDetail) {
        _uiState.update {
            AboutLCKUiState.Match(
                matchId = matchId,
                matchData = matchData,
                isLoading = false
            )
        }
    }
    private fun getMatch(date:String){
        viewModelScope.launch {
            getAboutLCKMatchUseCase(date).onSuccess { result ->
                cachedMatch = result
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
                    cachedRanking = result
                    cachedSupportTeam = supportTeam
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