package every.lol.com.feature.aboutlck

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.feature.aboutlck.model.AboutLCKIntent
import every.lol.com.feature.aboutlck.model.AboutLCKUiState
import moe.tlaster.precompose.koin.koinViewModel


@Composable
fun AboutLCKRoute(
    viewModel: AboutLCKViewModel = koinViewModel(AboutLCKViewModel ::class)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val homeState = uiState as? AboutLCKUiState.Home


    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is AboutLCKEvent.ShowToast -> {  }
                else -> {}
            }
        }
    }

    println(homeState)
    if(homeState == null){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(EveryLoLTheme.color.newBg),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = EveryLoLTheme.color.grayScale200)
        }

    }else {
        AboutLCKScreen(
            state = uiState,
            onIntent = viewModel::onIntent
        )
    }
}

@Composable
fun AboutLCKScreen(
    state: AboutLCKUiState,
    onIntent: (AboutLCKIntent) -> Unit
) {

}