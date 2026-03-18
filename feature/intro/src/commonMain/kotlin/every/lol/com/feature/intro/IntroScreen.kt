package every.lol.com.feature.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import every.lol.com.core.common.EveryLolBackHandler
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.component.TosScreen
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.intro.model.IntroIntent
import every.lol.com.feature.intro.model.IntroUiState
import everylol.feature.intro.generated.resources.Res
import everylol.feature.intro.generated.resources.ic_logo_text
import everylol.feature.intro.generated.resources.img_login
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun IntroRoute(
    viewModel: IntroViewModel = koinViewModel<IntroViewModel>(IntroViewModel::class),
    onNavigateHome: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.onIntent(IntroIntent.LoadInitial)

        viewModel.event.collect { event ->
            when (event) {
                is IntroEvent.ShowErrorSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.throwable.message ?: "에러가 발생했습니다."
                    )
                }
                IntroEvent.NavigateHome -> onNavigateHome()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val state = uiState) {
                is IntroUiState.Loading -> {
                    LoadingScreen()
                }

                is IntroUiState.Login -> {
                    LoginScreen(
                        onClick = {
                            viewModel.onIntent(IntroIntent.ClickLogin("kakaoToken"))
                                  },
                        onLongClick = { /* viewModel.putUserInitial() */ }
                    )
                }

                is IntroUiState.Signup -> {
                    SignupScreen(
                            state = state,
                            onValueChange = { nickName ->
                                viewModel.onIntent(IntroIntent.InputNickName(nickName))
                            },
                            onProfileImageChange = { image ->
                                viewModel.onIntent(IntroIntent.ChangeProfileImage(image))
                            },
                            onTeamsChange = { teams ->
                                viewModel.onIntent(IntroIntent.ChangeSelectedTeams(teams))
                            },
                            checkNickName = { nickName ->
                                viewModel.onIntent(IntroIntent.ClickCheckDuplicateNickname(nickName))
                            },
                            onBackClick = {
                                viewModel.onIntent(IntroIntent.ClickBackToSignup)
                            },
                            onSignupClick = {
                                viewModel.onIntent(IntroIntent.ClickSignupSubmit)
                            },
                            onNavigateToTermDetail = { id ->
                                viewModel.onIntent(IntroIntent.ClickTosDetail(id))
                            }
                        )
                }

                is IntroUiState.SignupComplete -> {
                    CompleteScreen(
                        nickName = state.nickName,
                        onGoHomeClick = {
                            viewModel.onIntent(IntroIntent.ClickStartApp)
                        }
                    )
                }

                is IntroUiState.TosDetail -> {
                    EveryLolBackHandler {
                        viewModel.onIntent(IntroIntent.ClickBackToSignup)
                    }
                    TosScreen(
                        tosId = state.id,
                        onBackClick = {
                            viewModel.onIntent(IntroIntent.ClickBackToSignup)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginScreen(
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.everylolDefault(EveryLoLTheme.color.newBg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .width(225.dp)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = onLongClick,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ),
                painter = painterResource(Res.drawable.ic_logo_text),
                contentDescription = "everylol logo",
                tint = Color.Unspecified
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .width(270.dp)
                    .combinedClickable(
                        onClick = onClick,
                        onLongClick = onLongClick,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ),
                painter = painterResource(Res.drawable.img_login),
                contentDescription = "login button",
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}