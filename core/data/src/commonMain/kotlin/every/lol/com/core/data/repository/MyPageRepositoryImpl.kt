package every.lol.com.core.data.repository

import every.lol.com.core.data.mapper.toResult
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.domain.repository.MyPagesRepository
import every.lol.com.core.model.Comments
import every.lol.com.core.model.CommentsDetail
import every.lol.com.core.model.Posts
import every.lol.com.core.model.PostsDetail
import every.lol.com.core.model.UserInform
import every.lol.com.core.model.mypage.MypagePog
import every.lol.com.core.model.mypage.MypagePogDetail
import every.lol.com.core.model.mypage.MypagePom
import every.lol.com.core.model.mypage.MypagePomDetail
import every.lol.com.core.model.mypage.MypagePredictionDetail
import every.lol.com.core.model.mypage.MypagePredictions
import every.lol.com.core.network.datasource.MyPagesDataSource
import every.lol.com.core.network.model.request.PatchMyTeamRequest
import every.lol.com.core.network.model.request.PatchProfileData
import every.lol.com.core.network.model.request.PatchProfileRequest

class MyPageRepositoryImpl(
    private val remote: MyPagesDataSource,
    private val local: AuthLocalDataSource
): MyPagesRepository {

    companion object {
        private const val PAGING_SIZE = 10
    }

    override suspend fun getProfile(): Result<UserInform> {
        return remote.getProfile().toResult().mapCatching { response ->
            local.saveSupportTeam(response.teamIds)
            UserInform(response.nickname,response.profileImageUrl, response.teamIds,)
        }
    }


    override suspend fun patchProfile(nickname: String?, profileImage: ByteArray?): Result<Unit> {
        val isDefaultImage = if (profileImage == null) true else false

        return remote.patchProfile(
            request = PatchProfileRequest(
                profileImage = profileImage,
                request = PatchProfileData(nickname = nickname, isDefaultImage = isDefaultImage)
            )
        ).toResult().map {
            Unit
        }
    }

    override suspend fun patchMyTeam(teamIds: List<Int>): Result<Unit> {
        return remote.patchMyTeam(PatchMyTeamRequest(teamIds)).toResult().map {
            Unit
        }
    }

    override suspend fun getPosts(page: Int): Result<Posts> =
        remote.getPosts(page, PAGING_SIZE).toResult().map { response ->
            Posts(
                posts = response.posts.map { detail ->
                    PostsDetail(
                        id = detail.id,
                        title = detail.title,
                        postType = detail.postType
                    )
                },
                isLast = response.isLast
            )
        }


    override suspend fun getComments(page: Int): Result<Comments> =
        remote.getComments(page, PAGING_SIZE).toResult().map { response ->
            Comments(
                comments = response.comments.map { detail ->
                    CommentsDetail(
                        commentId = detail.commentId,
                        postId = detail.postId,
                        content = detail.content,
                        postType = detail.postType
                    )
                },
                isLast = response.isLast
            )
        }

    override suspend fun withdrawal(): Result<Unit?> {
        val result = remote.withdrawal().toResult()
        return result.map {
            local.clearAuthData()
            Unit
        }
    }

    override suspend fun logout(): Result<Unit?> {
        val authData = local.getAuthData() ?: return Result.success(Unit)
        val refreshToken = authData.refreshToken
        val result = remote.logout(refreshToken).toResult()
        return result.map {
            local.clearAuthData()
            Unit
        }
    }

    override suspend fun getPredictions(): Result<MypagePredictions> =
        remote.getPredictions().toResult().map{
            MypagePredictions(
                correctCount = it.correctCount,
                topPercent = it.topPercent,
                predictionDetails = it.predictionDetails.map{detail ->
                    MypagePredictionDetail(
                        matchId = detail.matchId,
                        matchDate = detail.matchDate,
                        team1Name = detail.team1Name,
                        team2Name = detail.team2Name,
                        predictedTeamName = detail.predictedTeamName,
                        isPredictionSuccessful = detail.isPredictionSuccessful
                        )
                }
            )
        }

    override suspend fun getPog(): Result<MypagePog> =
        remote.getPog().toResult().map {
            MypagePog(
                setPogVoteDetails = it.setPogVoteDetails.map { detail ->
                    MypagePogDetail(
                        matchId = detail.matchId,
                        setIndex = detail.setIndex,
                        playerId = detail.playerId,
                        playerName = detail.playerName,
                        position = detail.position,
                        voteDate = detail.voteDate
                    )
                }
            )
        }

    override suspend fun getPom(): Result<MypagePom> =
        remote.getPom().toResult().map {
            MypagePom(
                mvpVoteDetails = it.mvpVoteDetails.map {detail ->
                    MypagePomDetail(
                        matchId = detail.matchId,
                        playerId = detail.playerId,
                        playerName = detail.playerName,
                        position = detail.position,
                        voteDate = detail.voteDate
                    )
                }
            )
        }

}