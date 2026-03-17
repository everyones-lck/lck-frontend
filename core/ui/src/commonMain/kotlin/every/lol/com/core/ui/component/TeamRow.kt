package every.lol.com.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import every.lol.com.core.model.Team


@Composable
fun TeamGroup(
    isSelectable: Boolean = true,
    selectedTeams: Set<Team> = emptySet(),
    onSelectedTeamsChange: (Set<Team>) -> Unit = {}
) {
    val teamsToShow = remember(isSelectable, selectedTeams) {
        val allTeams = Team.entries.filter { it != Team.NONE }
        if (isSelectable) {
            allTeams
        } else {
            allTeams.filter { it in selectedTeams }
        }
    }

    var internalSelectedTeams by remember { mutableStateOf(selectedTeams) }

    val row1 = if (isSelectable) teamsToShow.take(3) else teamsToShow
    val row2 = if (isSelectable) teamsToShow.drop(3).take(4) else emptyList()
    val row3 = if (isSelectable) teamsToShow.drop(7).take(3) else emptyList()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = if(isSelectable) Arrangement.spacedBy(16.dp) else Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val handleTeamClick: (Team) -> Unit = { team ->
            if (isSelectable) {
                internalSelectedTeams = if (internalSelectedTeams.contains(team)) {
                    internalSelectedTeams - team
                } else {
                    internalSelectedTeams + team
                }
                onSelectedTeamsChange(internalSelectedTeams)
            }
        }
        if (row1.isNotEmpty()) { TeamRow(teams = row1, selectedTeams = if (isSelectable) internalSelectedTeams else selectedTeams, isSelectable = isSelectable, onTeamClick = handleTeamClick) }
        if (isSelectable || row2.isNotEmpty()) { TeamRow(teams = row2, selectedTeams = if (isSelectable) internalSelectedTeams else selectedTeams, isSelectable = isSelectable, onTeamClick = handleTeamClick) }
        if (isSelectable || row3.isNotEmpty()) { TeamRow(teams = row3, selectedTeams = if (isSelectable) internalSelectedTeams else selectedTeams, isSelectable = isSelectable, onTeamClick = handleTeamClick) }
    }
}

@Composable
private fun TeamRow(
    teams: List<Team>,
    selectedTeams: Set<Team>,
    isSelectable: Boolean,
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
                isSelectable = isSelectable,
                onClick = { onTeamClick(team) }
            )
        }
    }
}