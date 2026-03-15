package every.lol.com.core.data.repository

import every.lol.com.core.data.mapper.toResult
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.domain.repository.AboutLCKRepository
import every.lol.com.core.model.AboutLCKTeamHistory
import every.lol.com.core.model.Match
import every.lol.com.core.model.MatchByDateList
import every.lol.com.core.model.MatchDetail
import every.lol.com.core.model.MatchTeam
import every.lol.com.core.model.MatchTime
import every.lol.com.core.network.datasource.AboutLCKDataSource

class AboutLCKRepositoryImpl(
    private val remote: AboutLCKDataSource,
    private val local: AuthLocalDataSource
): AboutLCKRepository {

    override suspend fun match(searchData: String): Result<Match> =
        remote.aboutLCKMatch(searchData).toResult().map{ response ->
            Match(
                matchByDateList = response.matchByDateList.map { dateList ->
                    MatchByDateList(
                        matchDate = dateList.matchDate,
                        matchDetailSize = dateList.matchDetailSize,
                        matchDetailList = dateList.matchDetailList.map { detail ->
                            MatchDetail(
                                team1 = MatchTeam(
                                    teamName = detail.team1.teamName,
                                    teamLogoUrl = detail.team1.teamLogoUrl,
                                    isWinner = detail.team1.isWinner
                                ),
                                team2 = MatchTeam(
                                    teamName = detail.team2.teamName,
                                    teamLogoUrl = detail.team2.teamLogoUrl,
                                    isWinner = detail.team2.isWinner
                                ),
                                matchFinished = detail.matchFinished,
                                season = detail.season,
                                matchNumber = detail.matchNumber,
                                matchDate = detail.matchDate,
                                matchTime = MatchTime(
                                    hour = detail.matchTime.hour,
                                    minute = detail.matchTime.minute,
                                    second = detail.matchTime.second,
                                    nano = detail.matchTime.nano
                                )
                            )
                        }
                    )
                }
            )
        }

    override suspend fun teamWinningHistory(teamId: Int, page: Int, size: Int): Result<AboutLCKTeamHistory> =
        remote.aboutLCKTeamWinningHistory(teamId, page, size).toResult().map{ response ->
            AboutLCKTeamHistory(
                seasonNameList = response.seasonNameList,
                totalPage = response.totalPage,
                totalElements = response.totalElements,
                isFirst = response.isFirst,
                isLast = response.isLast
            )
        }

    
}