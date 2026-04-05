package every.lol.com.core.data.repository

import every.lol.com.core.data.mapper.toResult
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.domain.repository.AboutLCKRepository
import every.lol.com.core.model.aboutlck.match.AboutLCKMatch
import every.lol.com.core.model.aboutlck.match.MatchDetail
import every.lol.com.core.model.aboutlck.match.MatchTeam
import every.lol.com.core.model.aboutlck.player.AboutLCKPlayerInformation
import every.lol.com.core.model.aboutlck.player.AboutLCKPlayerTeamHistory
import every.lol.com.core.model.aboutlck.player.AboutLCKPlayerWinningHistory
import every.lol.com.core.model.aboutlck.player.SeasonTeamDetails
import every.lol.com.core.model.aboutlck.team.AboutLCKTeamPlayerHistory
import every.lol.com.core.model.aboutlck.team.AboutLCKTeamPlayerInformation
import every.lol.com.core.model.aboutlck.team.AboutLCKTeamRating
import every.lol.com.core.model.aboutlck.team.AboutLCKTeamRatingHistory
import every.lol.com.core.model.aboutlck.team.AboutLCKTeamWinnigHistory
import every.lol.com.core.model.aboutlck.team.Player
import every.lol.com.core.model.aboutlck.team.PlayerDetail
import every.lol.com.core.model.aboutlck.team.SeasonDetailList
import every.lol.com.core.model.aboutlck.team.SeasonDetails
import every.lol.com.core.model.aboutlck.team.TeamDetailList
import every.lol.com.core.network.datasource.AboutLCKDataSource

class AboutLCKRepositoryImpl(
    private val remote: AboutLCKDataSource,
    private val local: AuthLocalDataSource
): AboutLCKRepository {

    override suspend fun aboutLCKMatch(searchDate: String): Result<AboutLCKMatch?> =
        remote.aboutLCKMatch(searchDate).toResult().map { response ->
            response?.let { payload ->
                AboutLCKMatch(
                    matches = payload.matches.map { dateList ->
                        MatchDetail(
                            matchDate = dateList.matchDate,
                            matchId = dateList.matchId,
                            matchStatus = dateList.matchStatus,
                            seasonName = dateList.seasonName,
                            groupName = dateList.groupName,
                            roundName = dateList.roundName,
                            team1 = MatchTeam(
                                teamId = dateList.team1.teamId,
                                teamName = dateList.team1.teamName,
                                winner = dateList.team1.winner
                            ),
                            team2 = MatchTeam(
                                teamId = dateList.team2.teamId,
                                teamName = dateList.team2.teamName,
                                winner = dateList.team2.winner
                            )
                        )
                    }
                )
            }
        }

    override suspend fun aboutLCKTeamWinningHistory(teamId: Int, page: Int, size: Int): Result<AboutLCKTeamWinnigHistory> =
        remote.aboutLCKTeamWinningHistory(teamId, page, size).toResult().map{ response ->
            AboutLCKTeamWinnigHistory(
                seasonNameList = response.seasonNameList,
                totalPage = response.totalPage,
                totalElements = response.totalElements,
                isFirst = response.isFirst,
                isLast = response.isLast
            )
        }

    override suspend fun aboutLCKTeamRatingHistory(teamId: Int, page: Int, size: Int): Result<AboutLCKTeamRatingHistory> =
        remote.aboutLCKTeamRatingHistory(teamId, page, size).toResult().map { response ->
            AboutLCKTeamRatingHistory(
                seasonDetailList = response.seasonDetailList.map {
                    SeasonDetailList(
                        seasonName = it.seasonName,
                        rating = it.rating
                    )
                },
                totalPage = response.totalPage,
                totalElements = response.totalElements,
                isFirst = response.isFirst,
                isLast = response.isLast
            )
        }

    override suspend fun aboutLCKTeamPlayerHistory(teamId: Int, page: Int, size: Int): Result<AboutLCKTeamPlayerHistory> =
        remote.aboutLCKTeamPlayerHistory(teamId, page, size).toResult().map { response ->
            AboutLCKTeamPlayerHistory(
                seasonDetails = response.seasonDetails.map {
                    SeasonDetails(
                        players = it.players.map { player ->
                            Player(
                                playerId = player.playerId,
                                playerName = player.playerName,
                                PlayerRole = player.PlayerRole,
                                playerPosition = player.playerPosition
                            )
                        },
                        numberOfPlayerDetail = it.numberOfPlayerDetail,
                        seasonName = it.seasonName
                    )
                },
                totalPage = response.totalPage,
                totalElements = response.totalElements,
                isFirst = response.isFirst,
                isLast = response.isLast
            )
        }

    override suspend fun aboutLCKTeamPlayerInformation(teamId: Int, seasonName: String, playerRole: String): Result<AboutLCKTeamPlayerInformation> =
        remote.aboutLCKTeamPlayerInformation(teamId, seasonName, playerRole).toResult().map { response ->
            AboutLCKTeamPlayerInformation(
                playerDetails = response.playerDetails.map {
                    PlayerDetail(
                        playerId = it.playerId,
                        playerName = it.playerName,
                        playerRole = it.playerRole
                    )
                },
                numberOfPlayerDetail = response.numberOfPlayerDetail
            )
        }

    override suspend fun aboutLCKTeamRating(page: Int, size: Int): Result<AboutLCKTeamRating> =
        remote.aboutLCKTeamRating(page, size).toResult().map { response ->
            AboutLCKTeamRating(
                teamDetailList = response.teamDetailList.map {
                    TeamDetailList(
                        teamId = it.teamId,
                        teamName = it.teamName,
                        teamLogoUrl = it.teamLogoUrl,
                        rating = it.rating
                    )
                },
                totalPage = response.totalPage,
                totalElements = response.totalElements,
                isFirst = response.isFirst,
                isLast = response.isLast
            )
        }

    override suspend fun aboutLCKPlayerWinningHistory(playerId: Int, page: Int, size: Int): Result<AboutLCKPlayerWinningHistory> =
        remote.aboutLCKPlayerWinningHistory(playerId, page, size).toResult().map {response ->
            AboutLCKPlayerWinningHistory(
                seasonNames = response.seasonNames,
                totalPage = response.totalPage,
                totalElements = response.totalElements,
                isFirst = response.isFirst,
                isLast = response.isLast
            )
        }

    override suspend fun aboutLCKPlayerTeamHistory(playerId: Int, page: Int, size: Int): Result<AboutLCKPlayerTeamHistory> =
        remote.aboutLCKPlayerTeamHistory(playerId, page, size).toResult().map { response ->
            AboutLCKPlayerTeamHistory(
                seasonTeamDetails = response.seasonTeamDetails.map {
                    SeasonTeamDetails(
                        teamName = it.teamName,
                        seasonName = it.seasonName
                    )
                },
                totalPage = response.totalPage,
                totalElements = response.totalElements,
                isFirst = response.isFirst,
                isLast = response.isLast
            )
        }

    override suspend fun aboutLCKPlayerInformation(playerId: Int): Result<AboutLCKPlayerInformation> =
        remote.aboutLCKPlayerInformation(playerId).toResult().map { response ->
            AboutLCKPlayerInformation(
                nickName = response.nickName,
                realName = response.realName,
                birthDate = response.birthDate,
                position = response.position
            )
        }
}