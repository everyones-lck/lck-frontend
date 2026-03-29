package every.lol.com.feature.community

import every.lol.com.core.domain.usecase.GetCommunityPostsUseCase
import every.lol.com.core.domain.usecase.GetReadPostUseCase
import every.lol.com.core.domain.usecase.PostCommunityPostUseCase
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
    data object NavigateCommunityHome: CommunityEvent
    data object WriteSuccess: CommunityEvent
}

class CommunityViewModel(
    private val getCommunityPostsUseCase: GetCommunityPostsUseCase,
    private val getReadPostUseCase: GetReadPostUseCase,
    private val postCommunityPostUseCase: PostCommunityPostUseCase,
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
            is CommunityIntent.ClickWriteTab -> handleWriteTabClick(intent.tab)
            CommunityIntent.FetchPosts -> loadCommunityData()
            is CommunityIntent.DetailPost -> loadReadPost(intent.postId)
            is CommunityIntent.ChangeTitle -> {
                val currentState = uiState.value
                if (currentState is CommunityUiState.Write) {
                    _uiState.value = currentState.copy(title = intent.title)
                }
            }
            is CommunityIntent.ChangeContent -> {
                val currentState = uiState.value
                if (currentState is CommunityUiState.Write) {
                    _uiState.value = currentState.copy(content = intent.content)
                }
            }
            is CommunityIntent.WritePost -> {
                handleWritePost(intent.title, intent.content)
            }
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

    private fun handleWriteTabClick(tab: CommunityUiState.WriteTab) {
        _uiState.update { state ->
            if (state is CommunityUiState.Write) {
                state.copy(selectedTab = tab)
            } else {
                CommunityUiState.Write(
                    selectedTab = tab,
                    title = "",
                    content = "",
                    isLoading = false
                )
            }
        }
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

    private fun loadReadPost(postId: Int){

        val state = _uiState.value
        if (state is CommunityUiState.Read && state.postId == postId) return

        _uiState.value = CommunityUiState.Read(postId = postId, isLoading = true)

        viewModelScope.launch {
            getReadPostUseCase(postId).onSuccess { post ->
                _uiState.update { current ->
                    if (current is CommunityUiState.Read && current.postId == postId) {
                        current.copy(post = post, isLoading = false)
                    } else current
                }
            }.onFailure {
                _uiState.update { current ->
                    if (current is CommunityUiState.Read && current.postId == postId) {
                        current.copy(isLoading = false)
                    } else current
                }
            }
        }
    }

    private fun handleWritePost(title: String, content: String) {

        val currentState = _uiState.value as? CommunityUiState.Write ?: return
        _uiState.value = currentState.copy(isLoading = true)

        viewModelScope.launch {
            postCommunityPostUseCase(null, "잡담", title, content).onSuccess {
                // 2. 성공 시 'WriteSuccess' 이벤트를 보냄
                _event.emit(CommunityEvent.WriteSuccess)
            }.onFailure {
                _uiState.update { state ->
                    if (state is CommunityUiState.Write) state.copy(isLoading = false) else state
                }
            }
        }
    }
}