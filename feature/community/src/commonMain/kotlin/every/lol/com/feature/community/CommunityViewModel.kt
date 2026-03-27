package every.lol.com.feature.community

import every.lol.com.core.domain.usecase.GetCommunityPostsUseCase
import every.lol.com.feature.community.model.CommunityIntent
import every.lol.com.feature.community.model.CommunityUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

sealed interface CommunityEvent{
    data object NavigateWrite: CommunityEvent
}

class CommunityViewModel(
    private val getCommunityPostsUseCase: GetCommunityPostsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CommunityUiState>(CommunityUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CommunityEvent>()
    val event = _event.asSharedFlow()

    init {
        onIntent(CommunityIntent.Loading)
    }

    fun onIntent(intent: CommunityIntent){
        when(intent){
            CommunityIntent.Loading -> loadCommunityData()
            is CommunityIntent.ClickTab -> handleTabClick(intent.tab)
            CommunityIntent.FetchPosts -> loadCommunityData()
            else -> {}
        }
    }

    fun handleTabClick(tab: CommunityUiState.CommunityTab) {
        _uiState.update { state ->
            if (state is CommunityUiState.Community) {
                state.copy(selectedTab = tab)} else state
        }

        loadCommunityData(tab)
    }

    private fun loadCommunityData(tab: CommunityUiState.CommunityTab = CommunityUiState.CommunityTab.ALL) {
        _uiState.update { state ->if (state is CommunityUiState.Community) {
            state.copy(isLoading = true, selectedTab = tab)
        } else {
            CommunityUiState.Community(isLoading = true, selectedTab = tab)
        }
        }
        viewModelScope.launch {
            val page = 0
            getCommunityPostsUseCase("잡담", page, 10).onSuccess { posts ->
                _uiState.update { state ->
                    if (state is CommunityUiState.Community) {
                        state.copy(
                            isLoading = false,
                            posts = posts.postDetailList
                        )
                    } else state
                }
            }.onFailure { throwable ->

            }
        }
    }
}