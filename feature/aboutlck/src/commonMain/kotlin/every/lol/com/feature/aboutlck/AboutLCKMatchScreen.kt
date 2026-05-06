package every.lol.com.feature.aboutlck

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.aboutlck.component.AboutLCKMatchCard
import every.lol.com.feature.aboutlck.model.AboutLCKIntent
import every.lol.com.feature.aboutlck.model.AboutLCKUiState

@Composable
fun AboutLCKMatchScreen(
    state: AboutLCKUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onIntent: (AboutLCKIntent) -> Unit
) {

    val aboutLCKState = state as? AboutLCKUiState.Match

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .everylolDefault(EveryLoLTheme.color.newBg, false),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                EverylolTopAppBar(onBackClick = onBackClick, title = "About LCK")
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
                    AboutLCKMatchCard(
                        item = aboutLCKState!!.matchData
                    )
                }
            }
        }
    }