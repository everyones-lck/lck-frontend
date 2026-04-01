package every.lol.com.core.data.repository

import every.lol.com.core.data.mapper.toResult
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.domain.repository.MatchesRepository
import every.lol.com.core.model.MatchCandidate
import every.lol.com.core.model.MatchCandidateTeam
import every.lol.com.core.model.MatchInfo
import every.lol.com.core.model.MatchPogCandidate
import every.lol.com.core.model.MatchVoteRate
import every.lol.com.core.model.MatchVoteTeam
import every.lol.com.core.model.Matches
import every.lol.com.core.model.MatchesTeam
import every.lol.com.core.model.PogCandidateCandidate
import every.lol.com.core.model.SetPogCandidate
import every.lol.com.core.model.SetPogCandidateDetail
import every.lol.com.core.network.datasource.MatchesDataSource

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

    override suspend fun getSetPogCandidate(matchId: Int): Result<SetPogCandidate> =
        remote.getSetPogCandidate(matchId).toResult().map { response ->
            SetPogCandidate(
                sets = response.sets.map { set ->
                    SetPogCandidateDetail(
                        setIndex = set.setIndex,
                        winnerTeamName = set.winnerTeamName,
                        candidates = set.candidates.map { candidate ->
                            PogCandidateCandidate(
                                playerId = candidate.playerId,
                                playerName = candidate.playerName
                            )
                        },
                        myVotedPlayerId = set.myVotedPlayerId
                    )
                }
            )
        }

    override suspend fun getMatchPogCandidate(matchId: Int): Result<MatchPogCandidate> =
        remote.getMatchPogCandidate(matchId).toResult().map { response ->
            MatchPogCandidate(
                matchId = response.matchId,
                winnerTeamName = response.winnerTeamName,
                candidates = response.candidates.map { candidate ->
                    PogCandidateCandidate(
                        playerId = candidate.playerId,
                        playerName = candidate.playerName
                    )
                },
                myVotedPlayerId = response.myVotedPlayerId
            )
        }

    override suspend fun getMatchCandidate(matchId: Int): Result<MatchCandidate> =
        remote.getMatchCandidate(matchId).toResult().map { response ->
            MatchCandidate(
                matchId = response.matchId,
                votable = response.votable,
                team1 = MatchCandidateTeam(
                    teamId = response.team1.teamId,
                    teamName = response.team1.teamName
                ),
                team2 = MatchCandidateTeam(
                    teamId = response.team2.teamId,
                    teamName = response.team2.teamName
                ),
                myVotedTeamId = response.myVotedTeamId
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