package every.lol.com.core.data.repository

import every.lol.com.core.common.getFileSize
import every.lol.com.core.data.mapper.toResult
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.domain.repository.CommunityRepository
import every.lol.com.core.model.CommentList
import every.lol.com.core.model.CommentRepliesList
import every.lol.com.core.model.MediaFile
import every.lol.com.core.model.PopularPostList
import every.lol.com.core.model.PopularPostListDetail
import every.lol.com.core.model.PostBlock
import every.lol.com.core.model.PostDetail
import every.lol.com.core.model.PostDetailBlocks
import every.lol.com.core.model.PostLike
import every.lol.com.core.model.PostList
import every.lol.com.core.model.PostListDetail
import every.lol.com.core.network.datasource.CommunityDataSource
import every.lol.com.core.network.model.request.BlocksRequest
import every.lol.com.core.network.model.request.MediaFileRequst
import every.lol.com.core.network.model.request.PostCommentRequest
import every.lol.com.core.network.model.request.PostPostDetailRequest
import every.lol.com.core.network.model.request.PostPostRequest
import every.lol.com.core.network.model.request.ReportCommentRequest
import every.lol.com.core.network.model.request.ReportPostRequest

class CommunityRepositoryImpl(
    private val remote: CommunityDataSource,
    private val local: AuthLocalDataSource
): CommunityRepository {

    override suspend fun postPost(files: List<MediaFile>?, type: String, title: String, blocks: List<PostBlock>, platformContext: Any): Result<Unit> {
        val networkBlocks = blocks.mapIndexed { index, block ->
            when (block) {
                is PostBlock.Text -> BlocksRequest(
                    sequence = index + 1,
                    type = "TEXT",
                    content = block.text
                )
                is PostBlock.Image -> BlocksRequest(
                    sequence = index + 1,
                    type = "IMAGE",
                    fileIndex = files?.indexOfFirst { it.uriString == block.imageUrl } ?: -1
                )
                is PostBlock.Video -> BlocksRequest(
                    sequence = index + 1,
                    type = "VIDEO",
                    fileIndex = files?.indexOfFirst { it.uriString == block.videoUrl } ?: -1,
                    thumbnailFileIndex = files?.indexOfFirst { it.uriString == block.thumbnailUrl } ?: -1
                )
            }
        }

        val networkMediaFiles = files?.map {
            MediaFileRequst(
                uriString = it.uriString,
                isVideo = it.isVideo,
                fileSize = getFileSize(platformContext, it.uriString)
            )
        }

        val requestBody = PostPostRequest(
            files = networkMediaFiles,
            request = PostPostDetailRequest(
                postType = type,
                postTitle = title,
                blocks = networkBlocks
            )
        )

        return remote.postPost(requestBody)
            .toResult()
            .map { }
    }

/*    override suspend fun editPost(postId: Int, type: String, title: String, content: String): Result<Unit> =
        remote.editPost(postId, PostPostDetailRequest(type, title, blocks)).toResult().map{it.postId}*/

    override suspend fun detailPost(postId: Int): Result<PostDetail> =
        remote.detailPost(postId).toResult().map {response ->
            PostDetail(response.postId, response.postType, response.writerProfileUrl, response.writerNickname, response.writerTeams, response.postTitle, response.postCreatedAt, response.isModified, response.isWriter, response.isLiked, response.likeCount, response.viewCount, response.commentCount, response.imageCounts, response.videoCounts,
                blocks = response.blocks.map {
                    PostDetailBlocks(it.sequence, it.type, it.content, it.fileUrl, it.fileName)
                },
                commentList = response.commentList!!.map {
                    CommentList(it.commentId, it.parentCommentId, it.profileImageUrl, it.nickname, it.supportTeams, it.content, it.createdAt, it.isDeleted, it.isWriter,
                        it.replies!!.map{ repliesList ->
                            CommentRepliesList(repliesList.commentId, repliesList.parentCommentId, repliesList.profileImageUrl, repliesList.nickname, repliesList.supportTeams, repliesList.content, repliesList.createdAt, repliesList.isDeleted, repliesList.isWriter)
                        }
                    )
                }
            )
        }

    override suspend fun postList(postType: String, page: Int, size: Int): Result<PostList> =
        remote.postList(postType, page, size).toResult().map { response ->
            PostList(
                postDetailList = response.postDetailList.map { PostListDetail(it.postId, it.postType, it.postTitle, it.postContent, it.postCreatedAt, it.userNickname, it.userProfileUrl, it.supportTeamNames, it.imageThumbnailUrl, it.videoThumbnailUrl, it.imageCounts, it.videoCounts, it.commentCounts, it.viewCount, it.likeCount, it.isLiked, it.isWriter) },
                isLast = response.isLast
            )
        }

    override suspend fun popularPostList(period: String): Result<PopularPostList> =
        remote.popularPostList(period).toResult().map { response ->
            PopularPostList(
                period = response.period,
                postList = response.postList.map { PopularPostListDetail(it.postId, it.postTypeName, it.postTitle, it.postContent, it.postCreatedAt, it.userNickname, it.userProfilePicture, it.imageThumbnailUrl, it.videoThumbnailUrl, it.imageCounts, it.videoCounts, it.commentCount, it.likeCount, it.viewCount)}
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