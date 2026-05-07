package every.lol.com.feature.aboutlck

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import every.lol.com.core.common.formatMillisToDate
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.component.LckRankingSection
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.aboutlck.component.AboutLCKMatchCard
import every.lol.com.feature.aboutlck.component.DateSelectSection
import every.lol.com.feature.aboutlck.model.AboutLCKIntent
import every.lol.com.feature.aboutlck.model.AboutLCKUiState
import moe.tlaster.precompose.koin.koinViewModel

@OptIn(ExperimentalMultiplatform::class)
@Composable
fun AboutLCKRoute(
    viewModel: AboutLCKViewModel = koinViewModel(AboutLCKViewModel ::class)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val aboutLCK = uiState as? AboutLCKUiState.AboutLCK
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is AboutLCKEvent.ShowToast -> snackbarHostState.showSnackbar(event.message)
                else -> {}
            }
        }
    }

    when(uiState){
        is AboutLCKUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(EveryLoLTheme.color.newBg),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = EveryLoLTheme.color.grayScale200)
            }
        }
        is AboutLCKUiState.AboutLCK -> {
            AboutLCKScreen(
                state = uiState,
                snackbarHostState = snackbarHostState,
                onIntent = viewModel::onIntent
            )
        }
        is AboutLCKUiState.Match -> {
            AboutLCKMatchScreen(
                state = uiState as AboutLCKUiState.Match,
                snackbarHostState = snackbarHostState,
                onBackClick = { viewModel.onIntent(AboutLCKIntent.LoadInitial) },
                onIntent = viewModel::onIntent
            )
        }
        is AboutLCKUiState.Team -> {
            AboutLCKTeamScreen(
                state = uiState as AboutLCKUiState.Team,
                snackbarHostState = snackbarHostState,
                onBackClick = { viewModel.onIntent(AboutLCKIntent.LoadInitial) },
                onIntent = viewModel::onIntent
            )
        }
        is AboutLCKUiState.Player -> {
            AboutLCKPlayerScreen(
                state = uiState as AboutLCKUiState.Player,
                snackbarHostState = snackbarHostState,
                onBackClick = { viewModel.onIntent(AboutLCKIntent.LoadInitial) },
                onIntent = viewModel::onIntent
            )
        }
    }
}

@Composable
fun AboutLCKScreen(
    state: AboutLCKUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (AboutLCKIntent) -> Unit
) {

    val aboutLCKState = state as? AboutLCKUiState.AboutLCK
    val ranking = aboutLCKState?.ranking?.groups?.firstOrNull()?.teams ?: emptyList()
    val matches = aboutLCKState?.match?.matches ?: emptyList()

    var showCalender by remember { mutableStateOf(false) }
    val supportTeams = aboutLCKState?.supportTeam ?: emptyList()

    val date = aboutLCKState?.date

    val datePickerState = rememberDatePickerState()


    if (showCalender) {
        DatePickerDialog(
            onDismissRequest = { showCalender = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        val formattedDate = formatMillisToDate(selectedMillis)
                        onIntent(AboutLCKIntent.ChangeDate(formattedDate))
                    }
                    showCalender = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCalender = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .everylolDefault(EveryLoLTheme.color.newBg, false),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                EverylolTopAppBar(title = "About LCK")
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    DateSelectSection(
                        modifier = Modifier.padding(top = 24.dp),
                        date = date.toString(),
                        showDatePicker = {
                            showCalender = true
                        }
                    )
                }
                if (matches.isNotEmpty()) {
                    items(matches) { matchItem ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AboutLCKMatchCard(
                                item = matchItem,
                                onClick = {
                                    onIntent(AboutLCKIntent.Match(matchItem.matchId, matchItem))
                                    println("matchId: ${matchItem.matchId}")
                                }
                            )
                        }
                    }
                } else {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(108.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(EveryLoLTheme.color.newBg)
                                .border(
                                    width = 1.dp,
                                    color = EveryLoLTheme.color.grayScale900,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(20.dp, 28.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "No Match",
                                style = EveryLoLTheme.typography.heading01,
                                color = EveryLoLTheme.color.grayScale100
                            )
                        }
                    }
                }
                item {
                    LckRankingSection(
                        standings = ranking,
                        supportTeams = supportTeams,
                        modifier = Modifier.padding(top = 24.dp),
                        onTeamClick = { teamId ->
                            // TODO
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}