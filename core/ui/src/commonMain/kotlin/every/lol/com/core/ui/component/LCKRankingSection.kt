package every.lol.com.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.RankingTeam
import every.lol.com.core.model.Team.Companion.fromTeamName
import everylol.core.ui.generated.resources.Res
import everylol.core.ui.generated.resources.img_ranking_bfx
import everylol.core.ui.generated.resources.img_ranking_bro
import everylol.core.ui.generated.resources.img_ranking_dk
import everylol.core.ui.generated.resources.img_ranking_dns
import everylol.core.ui.generated.resources.img_ranking_gen
import everylol.core.ui.generated.resources.img_ranking_hle
import everylol.core.ui.generated.resources.img_ranking_krx
import everylol.core.ui.generated.resources.img_ranking_kt
import everylol.core.ui.generated.resources.img_ranking_ns
import everylol.core.ui.generated.resources.img_ranking_t1
import org.jetbrains.compose.resources.painterResource

@Composable
fun LckRankingSection(
    standings: List<RankingTeam>, //추후 Group으로 수정
    modifier: Modifier = Modifier,
    onTeamClick: (Long) -> Unit = {},
    // cardBackground: @Composable BoxScope.(LckStandingTeamModel) -> Unit = {}
) {
    if (standings.isEmpty()) return

    //val favoriteTeam = standings.firstOrNull { it.teamId == favoriteTeamId }

    val top3 = standings.take(3)
    val lowerRanks = standings.drop(3)

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "LCK 순위",
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.grayScale800
        )

        Spacer(modifier = Modifier.height(20.dp))
        if (standings.isEmpty()) {
            Text(
                text = "경기가 아직 진행되지 않았습니다",
                style = EveryLoLTheme.typography.subtitle03,
                color = EveryLoLTheme.color.white200
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                // Todo: 응원팀 로컬에 저장 및 호출 구현
                /*favoriteTeam?.let { team ->
                FavoriteRankCard(
                    team = team,
                    onClick = onTeamClick,
                    cardBackground = cardBackground
                )
            }*/
                top3.forEach { team ->
                    TopRankCard(
                        team = team,
                        onClick = onTeamClick,
                        //cardBackground = cardBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                lowerRanks.forEach { team ->
                    RankListRow(
                        team = team,
                        isFavorite = false,
                        onClick = onTeamClick
                    )
                }
            }
        }
    }
}

/*@Composable
private fun FavoriteRankCard(
    team: HomeRankingTeam,
    onClick: (Long) -> Unit,
    //cardBackground: @Composable BoxScope.(LckStandingTeamModel) -> Unit
) {
    Card(
        modifier = Modifier
            .width(76.dp)
            .height(112.dp)
            .clickable { onClick(team.teamId) },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = EveryLoLTheme.color.grayScale1000
        ),
        border = BorderStroke(1.dp, EveryLoLTheme.color.teamT1)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            cardBackground(team)

            Text(
                text = "${team.rank}",
                style = EveryLoLTheme.typography.subtitle03,
                color = EveryLoLTheme.color.white200,
                modifier = Modifier.padding(start = 8.dp, top = 7.dp)
            )

            Text(
                text = team.teamName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp, bottom = 10.dp)
            )
        }
    }
}*/

@Composable
private fun TopRankCard(
    team: RankingTeam,
    onClick: (Long) -> Unit
) {

    val rankingBgRes = when (team.teamName) {
        "T1" -> Res.drawable.img_ranking_t1
        "GEN" -> Res.drawable.img_ranking_gen
        "HLE" -> Res.drawable.img_ranking_hle
        "KT" -> Res.drawable.img_ranking_kt
        "DK" -> Res.drawable.img_ranking_dk
        "dn" -> Res.drawable.img_ranking_dns
        "bnk" -> Res.drawable.img_ranking_bfx
        "NS" -> Res.drawable.img_ranking_ns
        "BRO" -> Res.drawable.img_ranking_bro
        "DRX" -> Res.drawable.img_ranking_krx
        else -> Res.drawable.img_ranking_t1
    }

    Card(
        modifier = Modifier
            .width(76.dp)
            .height(112.dp)
            .clickable {
                val teamId = fromTeamName(team.teamName).id.toLong()
                onClick(teamId)
            },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = EveryLoLTheme.color.grayScale1000
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(rankingBgRes),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds // 전체를 꽉 채움
            )
            Text(
                text = "${team.rank}",
                style = EveryLoLTheme.typography.subtitle03,
                color = EveryLoLTheme.color.grayScale600,
                modifier = Modifier.padding(start = 8.dp, top = 7.dp)
            )
        }
    }
}

@Composable
private fun RankListRow(
    team: RankingTeam,
    isFavorite: Boolean,
    onClick: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
                if (isFavorite) EveryLoLTheme.color.grayScale1000 else Color.Transparent
            )
            .clickable {
                val teamId = fromTeamName(team.teamName).id.toLong()
                onClick(teamId)
            }
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${team.rank}",
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.white200,
            modifier = Modifier.width(10.dp)
        )

        Spacer(modifier = Modifier.width(30.dp))

        Text(
            text = team.teamName,
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.white200,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "-",
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.white200,
        )
    }
}