package every.lol.com.feature.community

import every.lol.com.core.domain.usecase.DeleteCommentUseCase
import every.lol.com.core.domain.usecase.DeletePostUseCase
import every.lol.com.core.domain.usecase.GetCommunityPopularPostsUseCase
import every.lol.com.core.domain.usecase.GetCommunityPostsUseCase
import every.lol.com.core.domain.usecase.GetReadPostUseCase
import every.lol.com.core.domain.usecase.PatchCommunityPostUseCase
import every.lol.com.core.domain.usecase.PostCommunityCommentUseCase
import every.lol.com.core.domain.usecase.PostCommunityPostLikeUseCase
import every.lol.com.core.domain.usecase.PostCommunityPostUseCase
import every.lol.com.core.domain.usecase.ReportCommentUseCase
import every.lol.com.core.domain.usecase.ReportPostUseCase
import every.lol.com.core.model.MediaFile
import every.lol.com.core.model.PostBlock
import every.lol.com.feature.community.model.CommunityIntent
import every.lol.com.feature.community.model.CommunityUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
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
    data object WriteSuccess: CommunityEvent
    data object DeletePostSuccess: CommunityEvent
    data class ShowToast(val message: String): CommunityEvent
}

class CommunityViewModel(
    private val getCommunityPostsUseCase: GetCommunityPostsUseCase,
    private val getCommunityPopularPostsUseCase: GetCommunityPopularPostsUseCase,
    private val getReadPostUseCase: GetReadPostUseCase,
    private val postCommunityPostUseCase: PostCommunityPostUseCase,
    private val patchCommunityPostUseCase: PatchCommunityPostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val reportPostUseCase: ReportPostUseCase,
    private val postCommunityCommentUseCase: PostCommunityCommentUseCase,
    private val postCommunityPostLikeUseCase: PostCommunityPostLikeUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val reportCommentUseCase: ReportCommentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CommunityUiState>(CommunityUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CommunityEvent>(replay = 0)
    val event = _event.asSharedFlow()

    private val uploadScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var communityLoadJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        uploadScope.cancel()
    }

    init {
        onIntent(CommunityIntent.Loading)
    }

    fun onIntent(intent: CommunityIntent){
        when(intent){
            is CommunityIntent.Loading -> {
                _uiState.value = CommunityUiState.Community(isLoading = true)
                loadCommunityData(tab = CommunityUiState.CommunityTab.ALL)
            }

            is CommunityIntent.ClickTab -> handleTabClick(intent.tab)
            is CommunityIntent.ClickWriteTab -> {
                communityLoadJob?.cancel()
                handleWriteTabClick(intent.tab)
            }
            is CommunityIntent.DetailPost -> loadReadPost(intent.postId, intent.isRefresh)
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
                handleSavePost(intent.title, intent.content)
            }
            is CommunityIntent.EditPost -> {
                communityLoadJob?.cancel()
                _uiState.value = CommunityUiState.Write(
                    postId = intent.postId,
                    title = intent.title,
                    content = intent.content,
                    selectedMedias = intent.medias,
                    isLoading = false
                )
            }
            is CommunityIntent.LoadPostForEdit -> {
                val currentState = _uiState.value
                if (currentState is CommunityUiState.Write && currentState.title.isNotEmpty()) {
                    _uiState.update { currentState.copy(isLoading = false) }
                } else {
                    handleLoadPostForEdit(intent.postId)
                }
            }
            is CommunityIntent.AddMedias -> handleAddMedias(intent.medias)
            is CommunityIntent.RemoveMedia -> handleRemoveMedia(intent.index)
            is CommunityIntent.MoveMedia -> handleMoveMedia(intent.from, intent.to)
            is CommunityIntent.LoadNextPage -> {
                val currentState = uiState.value as? CommunityUiState.Community
                if (currentState != null) {
                    loadCommunityData(tab = currentState.selectedTab, isNextPage = true)
                }
            }
            is CommunityIntent.DeletePost -> handleDeletePost(intent.postId)
            is CommunityIntent.ReportPost -> handleReportPost(intent.postId, intent.reportDetail)
            is CommunityIntent.DeleteComment -> handleDeleteComment(intent.commentId)
            is CommunityIntent.ReportComment -> handleReportComment(intent.commentId, intent.reportDetail)
            is CommunityIntent.WriteComment -> handleWriteComment(intent.postId, intent.content)
            is CommunityIntent.WriteReply -> handleWriteComment(intent.postId, intent.content, intent.parentCommentId)
            is CommunityIntent.LikePost -> handleLikePost(intent.postId)
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
        if(tab == CommunityUiState.CommunityTab.ALL) getPopularPosts("weekly")

        val currentTab = tab
            ?: (uiState.value as? CommunityUiState.Community)?.selectedTab
            ?: CommunityUiState.CommunityTab.ALL

        if (isFetching || (isNextPage && isLastPage)) return

        isFetching = true
        if (!isNextPage) {
            currentPage = 0
            isLastPage = false
        }
        communityLoadJob?.cancel()
        communityLoadJob = viewModelScope.launch {
            getCommunityPostsUseCase(currentTab.displayName, currentPage, 10).onSuccess { response ->
                _uiState.update { state ->
                    if (state !is CommunityUiState.Community && !isNextPage && state !is CommunityUiState.Loading) {
                        return@update state
                    }

                    val currentState = when (state) {
                        is CommunityUiState.Community -> state
                        is CommunityUiState.Loading -> CommunityUiState.Community()
                        else -> return@update state
                    }

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

    private fun handleLoadPostForEdit(postId: Int) {
        val currentState = _uiState.value
        if (currentState is CommunityUiState.Write && currentState.title.isNotEmpty()) {
            return
        }

        _uiState.update { CommunityUiState.Write(postId = postId, isLoading = true) }

        viewModelScope.launch {
            getReadPostUseCase(postId).onSuccess { post ->
                val combinedContent = post.blocks.filter { it.type == "TEXT" }.joinToString("\n") { it.content ?: "" }
                val mediaItems = post.blocks.filter { it.type != "TEXT" }.mapIndexed { index, block ->
                    CommunityUiState.MediaItem(
                        id = block.fileName ?: "media_$index",
                        uriString = block.fileUrl ?: "",
                        isVideo = block.type == "VIDEO",
                        order = block.sequence
                    )
                }

                _uiState.update {
                    CommunityUiState.Write(
                        postId = postId,
                        title = post.postTitle,
                        content = combinedContent,
                        selectedMedias = mediaItems,
                        isLoading = false
                    )
                }
            }.onFailure {
                _uiState.update { (it as? CommunityUiState.Write)?.copy(isLoading = false) ?: it }
                _event.emit(CommunityEvent.ShowToast("데이터를 불러오지 못했습니다."))
            }
        }
    }

    private fun loadReadPost(postId: Int, isRefresh: Boolean = false) {

        val currentState = _uiState.value

        if (!isRefresh && currentState is CommunityUiState.Read && currentState.postId == postId && currentState.post != null) {
            return
        }

        _uiState.update { state ->
            if (state is CommunityUiState.Read && state.postId == postId) {
                state.copy(isLoading = true)
            } else {
                CommunityUiState.Read(postId = postId, isLoading = true)
            }
        }

        viewModelScope.launch {
            getReadPostUseCase(postId).onSuccess { post ->
                _uiState.update { current ->
                    val baseState = if (current is CommunityUiState.Read && current.postId == postId) current else CommunityUiState.Read(postId = postId)

                    baseState.copy(
                        post = post,
                        isLoading = false,
                        isMine = post.isWriter
                    )
                }
            }.onFailure {
                _uiState.update { current ->
                    if (current is CommunityUiState.Read) {
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
        updateWriteState { state ->
            val currentMedias = state.selectedMedias
            val currentImages = currentMedias.filter { !it.isVideo }
            val currentVideos = currentMedias.filter { it.isVideo }

            val newImages = newMedias.filter { !it.isVideo }
            val newVideos = newMedias.filter { it.isVideo }

            val availableImageSpace = (10 - currentImages.size).coerceAtLeast(0)
            val availableVideoSpace = (2 - currentVideos.size).coerceAtLeast(0)

            if (newImages.size > availableImageSpace || newVideos.size > availableVideoSpace) {
                viewModelScope.launch {
                    _event.emit(CommunityEvent.ShowToast("사진은 최대 10장, 영상은 최대 2개까지 가능합니다."))
                }
            }

            val validNewImages = newImages.take(availableImageSpace)
            val validNewVideos = newVideos.take(availableVideoSpace)
            val processedNewMedias = (validNewImages + validNewVideos)

            if (processedNewMedias.isEmpty()) return@updateWriteState state
            val lastLineIndex = state.content.split("\n").size - 1
            val finalNewMedias = processedNewMedias.map {
                it.copy(order = lastLineIndex.coerceAtLeast(0))
            }

            val updatedContent = if (state.content.isEmpty()) "\n" else state.content + "\n"

            state.copy(
                content = updatedContent,
                selectedMedias = currentMedias + finalNewMedias
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

    private fun handleSavePost(title: String, content: String){
        val currentState = _uiState.value as? CommunityUiState.Write ?: return
        val isEditMode = currentState.postId != null

        uploadScope.launch {
            try {
                _uiState.update { if (it is CommunityUiState.Write) it.copy(isLoading = true) else it }

                val fileInputs = currentState.selectedMedias
                    .filter { if (isEditMode) !it.uriString.startsWith("http") else true }
                    .map { MediaFile(uriString = it.uriString, isVideo = it.isVideo) }

                val postBlocks = mutableListOf<PostBlock>()
                val lines = content.split("\n")
                lines.forEachIndexed { index, lineText ->
                    if (lineText.isNotBlank()) postBlocks.add(PostBlock.Text(text = lineText))
                    currentState.selectedMedias
                        .filter { it.order == index }
                        .forEach { media ->
                            val block = if (media.isVideo) PostBlock.Video(media.uriString)
                            else PostBlock.Image(media.uriString)
                            postBlocks.add(block)
                        }
                }

                val result = if (isEditMode) {
                    patchCommunityPostUseCase(
                        postId = currentState.postId!!,
                        newFiles = fileInputs,
                        type = currentState.selectedTab.displayName,
                        title = title,
                        blocks = postBlocks
                    )
                } else {
                    postCommunityPostUseCase(
                        files = fileInputs,
                        type = currentState.selectedTab.displayName,
                        title = title,
                        blocks = postBlocks
                    )
                }

                result.onSuccess {
                    _event.emit(CommunityEvent.WriteSuccess)
                }.onFailure { e ->
                    _uiState.update { if (it is CommunityUiState.Write) it.copy(isLoading = false) else it }
                    _event.emit(CommunityEvent.ShowToast("저장에 실패했습니다: ${e.message}"))
                }
            } catch (e: Exception) {
                _uiState.update { if (it is CommunityUiState.Write) it.copy(isLoading = false) else it }
                _event.emit(CommunityEvent.ShowToast("저장에 실패했습니다: ${e.message}"))
            }
        }
    }
    private fun handleDeletePost(postId: Int) {
        viewModelScope.launch {
            deletePostUseCase(postId).onSuccess {
                _event.emit(CommunityEvent.DeletePostSuccess)
            }.onFailure {
                _event.emit(CommunityEvent.ShowToast("삭제에 실패하였습니다."))
            }
        }
    }

    private fun handleReportPost(postId: Int, reportDetail: String) {
        viewModelScope.launch {
            reportPostUseCase(postId, reportDetail).onSuccess {

            }.onFailure {
                _event.emit(CommunityEvent.ShowToast("신고에 실패하였습니다."))
            }
        }
    }

    //Todo: 댓글 삭제 서버 수정 후 재확인
    private fun handleDeleteComment(commentId: Int) {
        _uiState.update { state ->
            if (state is CommunityUiState.Read) state.copy(isLoading = true) else state
        }
        viewModelScope.launch {
            deleteCommentUseCase(commentId).onSuccess {
                val currentPostId = (uiState.value as? CommunityUiState.Read)?.postId
                if (currentPostId != null) {
                    loadReadPost(currentPostId, isRefresh = true)
                }
            }.onFailure {
                _event.emit(CommunityEvent.ShowToast("삭제에 실패하였습니다."))
            }
        }
    }

    private fun handleReportComment(commentId: Int, reportDetail: String) {
        viewModelScope.launch {
            reportCommentUseCase(commentId,reportDetail).onSuccess {

            }.onFailure {
                _event.emit(CommunityEvent.ShowToast("신고에 실패하였습니다."))
            }
        }
    }

    private fun handleWriteComment(postId: Int, content: String,parentCommentId: Long? = null) {
        _uiState.update { state ->
            if (state is CommunityUiState.Read) state.copy(isLoading = true) else state
        }
        viewModelScope.launch {
            postCommunityCommentUseCase(postId, content, parentCommentId).onSuccess {
                loadReadPost(postId, isRefresh = true)
            }.onFailure {
                _uiState.update { state ->
                    if (state is CommunityUiState.Read) state.copy(isLoading = false) else state
                }
                _event.emit(CommunityEvent.ShowToast("댓글 작성에 실패하였습니다."))
            }
        }
    }

    private fun handleLikePost(postId: Int){
        viewModelScope.launch {
            postCommunityPostLikeUseCase(postId).onSuccess { response ->
                _uiState.update { state ->
                    if (state is CommunityUiState.Read && state.postId == postId) {
                        state.copy(
                            isLiked = response.isLiked,
                            likeCount = response.likeCount
                        )
                    } else state
                }
            }.onFailure {
                _event.emit(CommunityEvent.ShowToast("좋아요에 실패하였습니다."))
            }
        }
    }

    private fun getPopularPosts(period: String){
        viewModelScope.launch {
            getCommunityPopularPostsUseCase(period).onSuccess { response ->
                _uiState.update {
                    if (it is CommunityUiState.Community) it.copy(popularPosts = response.postList) else it
                }
            }.onFailure {
                _uiState.update {
                    if (it is CommunityUiState.Community) it.copy(popularPosts = emptyList()) else it
                }
            }
        }
    }
}
