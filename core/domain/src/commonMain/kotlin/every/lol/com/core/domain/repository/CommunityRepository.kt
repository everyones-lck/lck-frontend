package every.lol.com.core.domain.repository

import every.lol.com.core.model.MediaFile
import every.lol.com.core.model.PopularPostList
import every.lol.com.core.model.PostBlock
import every.lol.com.core.model.PostDetail
import every.lol.com.core.model.PostLike
import every.lol.com.core.model.PostList

interface CommunityRepository {
    suspend fun postPost(files: List<MediaFile>?=null, type: String, title: String, blocks: List<PostBlock>, platformContext: Any): Result<Unit>
    //suspend fun editPost(postId: Int, type: String, title: String, content: String): Result<Unit>
    suspend fun detailPost(postId: Int): Result<PostDetail>
    suspend fun postList(postType: String, page: Int, size: Int): Result<PostList>
    suspend fun popularPostList(period: String): Result<PopularPostList>
    suspend fun deletePost(postId: Int): Result<Unit?>
    suspend fun postComment(postId: Int, content: String, parentCommentId: Long?=null): Result<Unit?>
    suspend fun deleteComment(commentId: Int): Result<Unit?>
    suspend fun reportPost(postId: Int, reportDetail: String): Result<Unit?>
    suspend fun reportComment(commentId: Int, reportDetail: String): Result<Unit?>

    suspend fun postLike(postId:Int): Result<PostLike>
}