package every.lol.com.core.data.repository

import every.lol.com.core.data.mapper.toResult
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.domain.repository.CommunityRepository
import every.lol.com.core.model.CommentList
import every.lol.com.core.model.FileList
import every.lol.com.core.model.PostDetail
import every.lol.com.core.model.PostLike
import every.lol.com.core.model.PostList
import every.lol.com.core.model.PostListDetail
import every.lol.com.core.network.datasource.CommunityDataSource
import every.lol.com.core.network.model.request.PostCommentRequest
import every.lol.com.core.network.model.request.PostPostDetailRequest
import every.lol.com.core.network.model.request.PostPostRequest
import every.lol.com.core.network.model.request.ReportCommentRequest
import every.lol.com.core.network.model.request.ReportPostRequest

class CommunityRepositoryImpl(
    private val remote: CommunityDataSource,
    private val local: AuthLocalDataSource
): CommunityRepository {

    override suspend fun postPost(files: List<ByteArray>?, type: String, title: String, content: String): Result<Unit> =
        remote.postPost( PostPostRequest(files, PostPostDetailRequest(type, title, content))).toResult().map {it.postId}

    override suspend fun editPost(postId: Int, type: String, title: String, content: String): Result<Unit> =
        remote.editPost(postId, PostPostDetailRequest(type, title, content)).toResult().map{it.postId}

    override suspend fun detailPost(postId: Int): Result<PostDetail> =
        remote.detailPost(postId).toResult().map {response ->
            PostDetail(
                postType = response.postType,
                writerProfileUrl = response.writerProfileUrl,
                writerNickName = response.writerNickname,
                postTitle = response.postTitle,
                postCreatedAt = response.postCreatedAt,
                content = response.content,
                isWriter = response.isWriter,
                fileList = response.fileList.map {
                    FileList(fileUrl = it.fileUrl, isImage = it.isImage)
                },
                commentList = response.commentList.map {
                    CommentList(
                        profileImageUrl = it.profileImageUrl,
                        nickname = it.nickname,
                        content = it.content,
                        createdAt = it.createdAt,
                        commentId = it.commentId,
                        isWriter = it.isWriter
                    )
                }
            )
        }

    override suspend fun postList(postType: String, page: Int, size: Int): Result<PostList> =
        remote.postList(postType, page, size).toResult().map { response ->
            PostList(
                postDetailList = response.postDetailList.map {
                    PostListDetail(
                        postId = it.postId,
                        postTitle = it.postTitle,
                        postContent = it.postContent,
                        postCreatedAt = it.postCreatedAt,
                        userNickname = it.userNickname,
                        userProfilePicture = it.userProfilePicture,
                        thumbnailFileUrl = it.thumbnailFileUrl,
                        commentCounts = it.commentCounts
                    )
                },
                isLast = response.isLast
            )
        }

    override suspend fun deletePost(postId: Int): Result<Unit?> =
        remote.deletePost(postId).toResult().map{
            Unit
        }

    override suspend fun postComment(postId: Int, content: String, parentCommentId: Long?): Result<Unit?> =
        remote.postComment(postId, PostCommentRequest(content, parentCommentId)).toResult().map {
            Unit
        }

    override suspend fun deleteComment(commentId: Int): Result<Unit?> =
        remote.deleteComment(commentId).toResult().map {
            Unit
        }

    override suspend fun reportPost(postId: Int, reportDetail: String): Result<Unit?> =
        remote.reportPost(ReportPostRequest(postId, reportDetail)).toResult().map {
            Unit
        }

    override suspend fun reportComment(commentId: Int, reportDetail: String): Result<Unit?> =
        remote.reportComment(ReportCommentRequest(commentId, reportDetail)).toResult().map {
            Unit
        }

    override suspend fun postLike(postId: Int): Result<PostLike> =
        remote.postLike(postId).toResult().map { response ->
            PostLike(
                isLiked = response.isLiked,
                likeCount = response.likeCount
            )
        }
}