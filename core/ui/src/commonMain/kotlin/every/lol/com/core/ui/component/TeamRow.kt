package every.lol.com.core.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.Team
import kotlin.collections.plus


@Composable
fun TeamGroup(
    onSelectedTeamsChange: (Set<Team>) -> Unit
) {
    val allTeams = remember { Team.entries.filter { it != Team.NONE } }
    var selectedTeams by remember { mutableStateOf(setOf<Team>()) }

    val row1 = allTeams.take(3)
    val row2 = allTeams.drop(3).take(4)
    val row3 = allTeams.drop(7).take(3)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TeamRow(teams = row1, selectedTeams = selectedTeams) { team ->
            selectedTeams = if (selectedTeams.contains(team)) selectedTeams - team else selectedTeams + team
            onSelectedTeamsChange(selectedTeams)
        }
        TeamRow(teams = row2, selectedTeams = selectedTeams) { team ->
            selectedTeams = if (selectedTeams.contains(team)) selectedTeams - team else selectedTeams + team
            onSelectedTeamsChange(selectedTeams)
        }
        TeamRow(teams = row3, selectedTeams = selectedTeams) { team ->
            selectedTeams = if (selectedTeams.contains(team)) selectedTeams - team else selectedTeams + team
            onSelectedTeamsChange(selectedTeams)
        }
    }
}

@Composable
private fun TeamRow(
    teams: List<Team>,
    selectedTeams: Set<Team>,
    horizontalSpacing: Dp = 12.dp,
    onTeamClick: (Team) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing, Alignment.CenterHorizontally)
    ) {
        teams.forEach { team ->
            TeamChip(
                team = team,
                isSelected = selectedTeams.contains(team),
                onClick = { onTeamClick(team) }
            )
        }
    }
}