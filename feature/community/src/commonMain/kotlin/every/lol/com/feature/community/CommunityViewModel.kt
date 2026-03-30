package every.lol.com.feature.community

import every.lol.com.core.common.compressImage
import every.lol.com.core.domain.usecase.DeletePostUseCase
import every.lol.com.core.domain.usecase.GetCommunityPostsUseCase
import every.lol.com.core.domain.usecase.GetReadPostUseCase
import every.lol.com.core.domain.usecase.PostCommunityCommentUseCase
import every.lol.com.core.domain.usecase.PostCommunityPostUseCase
import every.lol.com.core.domain.usecase.ReportPostUseCase
import every.lol.com.core.model.CommentList
import every.lol.com.feature.community.model.CommunityIntent
import every.lol.com.feature.community.model.CommunityUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val deletePostUseCase: DeletePostUseCase,
    private val reportPostUseCase: ReportPostUseCase,
    private val postCommunityCommentUseCase: PostCommunityCommentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CommunityUiState>(CommunityUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CommunityEvent>(replay = 0)
    val event = _event.asSharedFlow()

    private val uploadScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        uploadScope.cancel()
    }

    init {
        onIntent(CommunityIntent.Loading)
    }

    fun onIntent(intent: CommunityIntent){
        when(intent){
            CommunityIntent.Loading -> loadCommunityData(tab = CommunityUiState.CommunityTab.ALL)
            is CommunityIntent.ClickTab -> handleTabClick(intent.tab)
            is CommunityIntent.ClickWriteTab -> handleWriteTabClick(intent.tab)
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
                    _uiState.value = currentState.copy(content = intent.content)
                }
            }
            is CommunityIntent.WritePost -> {
                handleWritePost(intent.title, intent.content)
            }
            is CommunityIntent.AddMedias -> handleAddMedias(intent.medias)

            is CommunityIntent.RemoveMedia -> handleRemoveMedia(intent.index)

            is CommunityIntent.MoveMedia -> handleMoveMedia(intent.from, intent.to)
            is CommunityIntent.LoadNextPage -> {
                println("Ktor Debug: LoadNextPage 이벤트 수신함 : ${uiState.value}")
                val currentState = uiState.value as? CommunityUiState.Community
                if (currentState != null) {
                    loadCommunityData(tab = currentState.selectedTab, isNextPage = true)
                }
            }
            is CommunityIntent.DeletePost -> handleDeletePost(intent.postId)
            is CommunityIntent.ReportPost -> handleReportPost(intent.postId)
            is CommunityIntent.WriteComment -> handleWriteComment(intent.postId, intent.content)
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


    private var currentPage = 0
    private var isLastPage = false
    private var isFetching = false


    private fun loadCommunityData(
        tab: CommunityUiState.CommunityTab?=null,
        isNextPage: Boolean = false
    ) {
        val currentTab = tab
            ?: (uiState.value as? CommunityUiState.Community)?.selectedTab
            ?: CommunityUiState.CommunityTab.ALL

        if (isFetching || (isNextPage && isLastPage)) return

        isFetching = true
        if (!isNextPage) {
            currentPage = 0
            isLastPage = false
        }

        viewModelScope.launch {
            getCommunityPostsUseCase(currentTab.displayName, currentPage, 10).onSuccess { response ->
                _uiState.update { state ->
                    val currentState = state as? CommunityUiState.Community ?: CommunityUiState.Community()

                    val updatedPosts = if (isNextPage) {
                        currentState.posts + response.postDetailList
                    } else {
                        response.postDetailList
                    }

                    isLastPage = response.postDetailList.isEmpty()
                    if (!isLastPage) currentPage++

                    currentState.copy(
                        isLoading = false,
                        posts = updatedPosts,
                        selectedTab = currentTab
                    )
                }
                isFetching = false
            }.onFailure {
                isFetching = false
                _uiState.update { state ->
                    val currentState = state as? CommunityUiState.Community ?: CommunityUiState.Community()
                    currentState.copy(
                        isLoading = false,
                        posts = if (currentPage == 0) emptyList() else currentState.posts
                    )
                }
            }
        }
    }


    private fun loadReadPost(postId: Int, isRefresh: Boolean = false) {
        val state = _uiState.value

        if (!isRefresh && state is CommunityUiState.Read && state.postId == postId) return

        if (!isRefresh) {
            _uiState.value = CommunityUiState.Read(postId = postId, isLoading = true)
        }

        viewModelScope.launch {
            getReadPostUseCase(postId).onSuccess { post ->
                _uiState.update { current ->
                    if (current is CommunityUiState.Read && current.postId == postId) {
                        current.copy(post = post, isLoading = false, isMine = post.isWriter)
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

        uploadScope.launch {
            try {
                _uiState.update { if (it is CommunityUiState.Write) it.copy(isLoading = true) else it }

                val compressedFiles = withContext(Dispatchers.Default) {
                    currentState.selectedMedias.map { it.url.compressImage(30) }
                }

                withContext(Dispatchers.IO) {
                    postCommunityPostUseCase(compressedFiles, currentState.selectedTab.displayName, title, content)
                        .onSuccess {
                            withContext(Dispatchers.Main) {
                                _event.emit(CommunityEvent.WriteSuccess)
                            }
                        }
                        .onFailure {
                            _uiState.update { if (it is CommunityUiState.Write) it.copy(isLoading = false) else it }
                        }
                }
            } catch (e: Exception) {
                _uiState.update { if (it is CommunityUiState.Write) it.copy(isLoading = false) else it }
            }
        }
    }

    private fun handleDeletePost(postId: Int) {
        viewModelScope.launch {
            deletePostUseCase(postId).onSuccess {
                _event.emit(CommunityEvent.NavigateCommunityHome)
            }.onFailure {
                _event.emit(CommunityEvent.ShowToast("삭제에 실패하였습니다."))
            }
        }
    }

    private fun handleReportPost(postId: Int) {
        viewModelScope.launch {
            reportPostUseCase(postId).onSuccess {
                _event.emit(CommunityEvent.NavigateCommunityHome)
            }.onFailure {
                _event.emit(CommunityEvent.ShowToast("신고에 실패하였습니다."))
            }
        }
    }

    private fun handleWriteComment(postId: Int, content: String) {
        val currentState = uiState.value as? CommunityUiState.Read ?: return
        val currentPost = currentState.post ?: return

        viewModelScope.launch {
            val tempComment = CommentList(
                profileImageUrl = "String",
                nickname = "String",
                content= content,
                createdAt= "방금 전",
                commentId= 1000,
                isWriter = if(currentPost.isWriter) true else false
            )

            val updatedPost = currentPost.copy(
                commentList = currentPost.commentList + tempComment
            )

            _uiState.update { state ->
                if (state is CommunityUiState.Read) {
                    state.copy(post = updatedPost)
                } else state
            }

            postCommunityCommentUseCase(postId, content).onSuccess {
                loadReadPost(postId, isRefresh = true)
            }.onFailure {
                _uiState.update { state ->
                    if (state is CommunityUiState.Read) {
                        state.copy(post = currentPost)
                    } else state
                }
                _event.emit(CommunityEvent.ShowToast("댓글 작성에 실패하였습니다."))
            }
        }
    }
}
