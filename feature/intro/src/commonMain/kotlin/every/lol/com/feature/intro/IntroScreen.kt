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
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.intro.model.IntroStep
import everylol.feature.intro.generated.resources.Res
import everylol.feature.intro.generated.resources.ic_logo_text
import everylol.feature.intro.generated.resources.img_login
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun IntroRoute(
    viewModel: IntroViewModel,
    onNavigateHome: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                IntroEvent.NavigateHome -> onNavigateHome()
                else -> Unit
            }
        }
    }
    if (uiState.isLoading) {
        LoadingScreen()
    } else {
        when (val currentStep = uiState.step) {
            is IntroStep.Loading -> {
                LoadingScreen()
            }

            is IntroStep.Login -> {
                LoginScreen(
                    onClick = {
                        onLoginClick()
                        viewModel.onLoginSuccess("test_token")
                    },
                    onLongClick = { viewModel.putUserInitial() }
                )
            }

            is IntroStep.Signup -> {
                SignupScreen(
                    nickName = uiState.nickName,
                    onValueChange = viewModel::changeNickName,
                    enabled = uiState.isEnabled,
                    checkNickName = { /* viewModel.checkNickname() */ },
                    onBackClick = viewModel::deleteLoginInfo,
                    onSignupClick = viewModel::onSignupSuccess,
                    onNavigateToTermDetail = viewModel::onNavigateToTosDetail
                )
            }

            is IntroStep.SignupComplete -> {
                CompleteScreen(
                    nickName = uiState.nickName,
                    onGoHomeClick = viewModel::putUserInitial
                )
            }

            is IntroStep.TosDetail -> {
                EveryLolBackHandler {
                    viewModel.backToSignupFromTos()
                }
                TosScreen(
                    tosId = currentStep.id,
                    onBackClick = viewModel::backToSignupFromTos
                )
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