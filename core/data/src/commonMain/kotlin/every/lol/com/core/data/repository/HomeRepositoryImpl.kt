package every.lol.com.core.data.repository

import every.lol.com.core.data.mapper.toResult
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.domain.repository.HomeRepository
import every.lol.com.core.model.HomeAlerts
import every.lol.com.core.model.HomeAlertsDetail
import every.lol.com.core.model.HomeNews
import every.lol.com.core.model.HomeNewsDetail
import every.lol.com.core.model.HomeTeam
import every.lol.com.core.model.HomeTodayMatch
import every.lol.com.core.model.HomeTodayMatchDetail
import every.lol.com.core.network.datasource.HomeDataSource

class HomeRepositoryImpl(
    private val remote: HomeDataSource,
    private val local: AuthLocalDataSource
): HomeRepository {

    override suspend fun todayMatchHome(): Result<HomeTodayMatch> =
        remote.todayMatchHome().toResult().map{ response ->
            HomeTodayMatch(
                matches = response.matches.map {
                    HomeTodayMatchDetail(
                        matchId = it.matchId,
                        matchDate = it.matchDate,
                        matchStatus = it.matchStatus,
                        seasonName = it.seasonName,
                        groupName = it.groupName,
                        roundName = it.roundName,
                        team1 = HomeTeam(
                            teamId = it.team1.teamId,
                            teamName = it.team1.teamName,
                            winner = it.team1.winner
                        ),
                        team2 = HomeTeam(
                            teamId = it.team2.teamId,
                            teamName = it.team2.teamName,
                            winner = it.team2.winner
                        )
                    )
                }
            )
        }

    override suspend fun newsHome(): Result<HomeNews> =
        remote.newsHome().toResult().map { response ->
            HomeNews(
                newsList = response.newsList.map {
                    HomeNewsDetail(
                        title = it.title,
                        link = it.link,
                        press = it.press,
                        publishedAt = it.publishedAt,
                        thumbnailUrl = it.thumbnailUrl
                    )
                }
            )
        }

    override suspend fun alertsHome(): Result<HomeAlerts> =
        remote.alertsHome().toResult().map {response ->
            HomeAlerts(
                alerts = response.alerts.map {
                    HomeAlertsDetail(
                        message = it.message,
                        matchId = it.matchId,
                        team1Id = it.team1Id,
                        team1Name = it.team1Name,
                        team2Id = it.team2Id,
                        team2Name = it.team2Name
                    )
                },
                alertCount = response.alertCount
            )
        }
}