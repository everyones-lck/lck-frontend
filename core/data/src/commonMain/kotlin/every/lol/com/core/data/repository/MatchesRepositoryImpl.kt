package every.lol.com.core.data.repository

import every.lol.com.core.data.mapper.toResult
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.domain.repository.MatchesRepository
import every.lol.com.core.model.MatchInfo
import every.lol.com.core.model.MatchPogResult
import every.lol.com.core.model.MatchPogVoteResult
import every.lol.com.core.model.MatchVoteRate
import every.lol.com.core.model.MatchVoteTeam
import every.lol.com.core.model.Matches
import every.lol.com.core.model.MatchesTeam
import every.lol.com.core.model.SetPogResult
import every.lol.com.core.model.SetPogSetResult
import every.lol.com.core.model.SetPogVoteItem
import every.lol.com.core.model.SetPogVoteResult
import every.lol.com.core.network.datasource.MatchesDataSource
import every.lol.com.core.network.model.request.MatchPogVoteRequest
import every.lol.com.core.network.model.request.MatchVoteMakingRequest
import every.lol.com.core.network.model.request.SetPogVoteItemRequest
import every.lol.com.core.network.model.request.SetPogVoteRequest

class MatchesRepositoryImpl(
    private val remote: MatchesDataSource,
    private val local: AuthLocalDataSource
): MatchesRepository {
    override suspend fun getMatches(): Result<MatchInfo> =
        remote.getMatches().toResult().map { response ->
            MatchInfo(
                matchInfo = response.matchInfo.map {
                    Matches(
                        matchId = it.matchId,
                        matchDate = it.matchDate,
                        matchStatus = it.matchStatus.toDomain(),
                        seasonName = it.seasonName,
                        groupName = it.groupName,
                        roundName = it.roundName,
                        team1 = MatchesTeam(
                            teamId = it.team1.teamId,
                            teamName = it.team1.teamName,
                            winner = it.team1.winner
                        ),
                        team2 = MatchesTeam(
                            teamId = it.team2.teamId,
                            teamName = it.team2.teamName,
                            winner = it.team2.winner
                        )
                    )
                }
            )
        }

    override suspend fun getMatchVoteRate(matchId: Long): Result<MatchVoteRate> =
        remote.getMatchVoteRate(matchId).toResult().map { response ->
            MatchVoteRate(
                matchId = response.matchId,
                team1 = MatchVoteTeam(
                    teamId = response.team1.teamId,
                    voteCount = response.team1.voteCount,
                    voteRate = response.team1.voteRate
                ),
                team2 = MatchVoteTeam(
                    teamId = response.team2.teamId,
                    voteCount = response.team2.voteCount,
                    voteRate = response.team2.voteRate
                ),
                totalVoteCount = response.totalVoteCount
            )
        }

    override suspend fun postMatchVote(matchId: Long, teamId: Int): Result<Unit?> =
        remote.postMatchVote(
            MatchVoteMakingRequest(
                matchId = matchId,
                teamId = teamId
            )
        ).toResult().map {
            Unit
        }

    override suspend fun postSetPogVote(matchId: Long, setPogVotes: List<SetPogVoteItem>): Result<Unit?> =
        remote.postSetPogVote(
            SetPogVoteRequest(matchId = matchId, setPogVotes = setPogVotes.map {
                    SetPogVoteItemRequest(setIndex = it.setIndex, playerId = it.playerId)
                }
            )
        ).toResult().map {
            Unit
        }

    override suspend fun postMatchPogVote(matchId: Long, playerId: Long?): Result<Unit?> =
        remote.postMatchPogVote(
            MatchPogVoteRequest(matchId = matchId, playerId = playerId)
        ).toResult().map {
            Unit
        }

    override suspend fun getSetPogResult(matchId: Long): Result<SetPogResult> =
        remote.getSetPogResult(matchId).toResult().map { response ->
            SetPogResult(
                sets = response.sets.map { set ->
                    SetPogSetResult(
                        setIndex = set.setIndex,
                        results = set.results.map { result ->
                            SetPogVoteResult(
                                playerId = result.playerId,
                                playerName = result.playerName,
                                voteCount = result.voteCount,
                                voteRate = result.voteRate
                            )
                        }
                    )
                }
            )
        }

    override suspend fun getMatchPogResult(matchId: Long): Result<MatchPogResult> =
        remote.getMatchPogResult(matchId).toResult().map { response ->
            MatchPogResult(
                matchId = response.matchId,
                results = response.results.map { result ->
                    MatchPogVoteResult(
                        playerId = result.playerId,
                        playerName = result.playerName,
                        voteCount = result.voteCount,
                        voteRate = result.voteRate
                    )
                }
            )
        }
}

private fun every.lol.com.core.network.model.response.MatchStatus.toDomain(): every.lol.com.core.model.MatchStatus {
    return when (this) {
        every.lol.com.core.network.model.response.MatchStatus.SCHEDULED ->
            every.lol.com.core.model.MatchStatus.SCHEDULED
        every.lol.com.core.network.model.response.MatchStatus.LIVE ->
            every.lol.com.core.model.MatchStatus.LIVE
        every.lol.com.core.network.model.response.MatchStatus.FINISHED ->
            every.lol.com.core.model.MatchStatus.FINISHED
    }
}