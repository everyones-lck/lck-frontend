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
    data class ShowToast(val message: String): CommunityEvent
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
            is CommunityIntent.UpdateMediaOrder -> {
                handleUpdateMediaOrder(intent.mediaId, intent.newOrder)
            }

            is CommunityIntent.ChangeContent -> {
                val currentState = uiState.value
                if (currentState is CommunityUiState.Write) {
                    val oldLineCount = currentState.content.split("\n").size
                    val newLineCount = intent.content.split("\n").size

                    if (oldLineCount != newLineCount) {
                        handleContentLineChange(currentState, intent.content)
                    } else {
                        _uiState.value = currentState.copy(content = intent.content)
                    }
                }
            }
            is CommunityIntent.WritePost -> {
                handleWritePost(intent.title, intent.content)
            }
            is CommunityIntent.AddMedias -> handleAddMedias(intent.medias)

            is CommunityIntent.RemoveMedia -> handleRemoveMedia(intent.index)

            is CommunityIntent.MoveMedia -> handleMoveMedia(intent.from, intent.to)

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

    private fun handleUpdateMediaOrder(mediaId: String, newOrder: Int) {
        val currentState = uiState.value
        if (currentState is CommunityUiState.Write) {
            val updatedMedias = currentState.selectedMedias.map {
                if (it.id == mediaId) it.copy(order = newOrder) else it
            }
            _uiState.value = currentState.copy(selectedMedias = updatedMedias)
        }
    }

    private fun handleContentLineChange(currentState: CommunityUiState.Write, newContent: String) {
        val oldLines = currentState.content.split("\n")
        val newLines = newContent.split("\n")

        val diffIndex = oldLines.zip(newLines).indexOfFirst { it.first != it.second }
        val isAdded = newLines.size > oldLines.size

        val adjustedMedias = currentState.selectedMedias.map { media ->
            if (isAdded && media.order >= diffIndex && diffIndex != -1) {
                media.copy(order = media.order + 1)
            } else if (!isAdded && media.order > diffIndex && diffIndex != -1) {
                media.copy(order = (media.order - 1).coerceAtLeast(0))
            } else {
                media
            }
        }

        _uiState.value = currentState.copy(
            content = newContent,
            selectedMedias = adjustedMedias
        )
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

    private fun updateWriteState(transform: (CommunityUiState.Write) -> CommunityUiState.Write) {
        _uiState.update { state ->
            if (state is CommunityUiState.Write) transform(state) else state
        }
    }

    private fun handleAddMedias(newMedias: List<CommunityUiState.MediaItem>) {
        val timeLimit = 3 * 60 * 1000L

        val timeFilteredMedias = newMedias.filter {
            if (it.isVideo) it.durationMs <= timeLimit else true
        }

        if (timeFilteredMedias.size < newMedias.size) {
            viewModelScope.launch {
                _event.emit(CommunityEvent.ShowToast("3분을 초과하는 영상은 제외되었습니다."))
            }
        }

        updateWriteState { state ->
            val currentMedias = state.selectedMedias

            val currentImages = currentMedias.filter { !it.isVideo }
            val currentVideos = currentMedias.filter { it.isVideo }

            val newImages = timeFilteredMedias.filter { !it.isVideo }
            val newVideos = timeFilteredMedias.filter { it.isVideo }

            val finalImages = (currentImages + newImages).take(10)
            val finalVideos = (currentVideos + newVideos).take(2)

            if (newImages.size + currentImages.size > 10 || newVideos.size + currentVideos.size > 2) {
                viewModelScope.launch {
                    _event.emit(CommunityEvent.ShowToast("사진은 최대 10장, 영상은 최대 2개까지 가능합니다."))
                }
            }

            val currentLines = state.content.split("\n").toMutableList()
            val lastLineIndex = (currentLines.size - 1).coerceAtLeast(0)
            currentLines.add("")
            val newContent = currentLines.joinToString("\n")
            val processedNewMedias = (newImages + newVideos).map {
                it.copy(order = lastLineIndex)
            }
            state.copy(
                content = newContent,
                selectedMedias = currentMedias + processedNewMedias
            )
        }
    }

    private fun handleRemoveMedia(index: Int) {
        updateWriteState { state ->
            val newList = state.selectedMedias.toMutableList().apply {
                if (index in indices) removeAt(index)
            }
            state.copy(selectedMedias = newList)
        }
    }

    private fun handleMoveMedia(from: Int, to: Int) {
        updateWriteState { state ->
            val newList = state.selectedMedias.toMutableList().apply {
                if (from in indices && to in indices) {
                    add(to, removeAt(from))
                }
            }
            state.copy(selectedMedias = newList)
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