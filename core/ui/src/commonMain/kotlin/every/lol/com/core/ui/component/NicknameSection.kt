package every.lol.com.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.component.EverylolTextField
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.core.ui.generated.resources.Res
import everylol.core.ui.generated.resources.ic_check
import org.jetbrains.compose.resources.painterResource

@Composable
fun NicknameSection(
    isProfileEdit: Boolean = false,
    originalNickname: String = "",
    modifier: Modifier = Modifier,
    nickName: String,
    onValueChange: (String) -> Unit,
    isDuplicateChecked: Boolean,
    onCheckNickName: () -> Unit,
    onValidationChanged: (Boolean) -> Unit
) {
    val isChanged = if (isProfileEdit) nickName != originalNickname else nickName.isNotEmpty()

    val isNotEmpty = nickName.isNotEmpty()
    val isProperLength = nickName.length in 1..10
    val isNoSpace = !nickName.contains(" ")

    val isAllValid = if (isProfileEdit && !isChanged) {
        true
    } else {
        isProperLength && isNoSpace && isDuplicateChecked
    }

    val textFieldStatus = when {
        !isNotEmpty -> 0
        isAllValid -> 1
        isProfileEdit  && !isChanged -> 2
        else -> 3
    }

    val dynamicHint = if (isProfileEdit) originalNickname else "닉네임을 입력해주세요"
    LaunchedEffect(textFieldStatus) {
        onValidationChanged(textFieldStatus == 1 || (isProfileEdit && textFieldStatus == 2))
    }


    Column(modifier = modifier.fillMaxWidth()) {
        EverylolTextField(
            value = nickName,
            onValueChange = onValueChange,
            hint = dynamicHint,
            status = textFieldStatus,
            onDone = onCheckNickName
        )

        Spacer(modifier = Modifier.height(16.dp))

        NicknameValidationGroup(
            isChanged = isChanged,
            nickName = nickName,
            isProperLength = isProperLength,
            isNoSpace = isNoSpace,
            isDuplicateChecked = isDuplicateChecked
        )
    }
}

@Composable
private fun NicknameValidationGroup(
    isChanged: Boolean,
    nickName: String,
    isProperLength: Boolean,
    isNoSpace: Boolean,
    isDuplicateChecked: Boolean
) {

    val isEmpty = nickName.isEmpty()

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (!isEmpty) {
            NicknameValidation(
                status = if (!isChanged) 0 else if (isDuplicateChecked) 1 else 2,
                message = "중복된 닉네임은 사용할 수 없습니다"
            )
        }

        NicknameValidation(
            status = when {
                !isChanged || isEmpty -> 0
                isProperLength -> 1
                else -> 2
            },
            message = "닉네임은 최대 10글자까지 입력 가능합니다"
        )

        NicknameValidation(
            status = when {
                !isChanged || isEmpty -> 0
                isNoSpace -> 1
                else -> 2
            },
            message = "닉네임 사이에는 공백을 입력할 수 없습니다"
        )
    }
}

@Composable
private fun NicknameValidation(
    modifier: Modifier = Modifier,
    status: Int?=0, //default, success, fail
    message: String
){
    val contentColor = when (status) {
        1 -> EveryLoLTheme.color.semanticCheck
        2 -> EveryLoLTheme.color.semanticWarning
        else -> EveryLoLTheme.color.grayScale800
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Icon(
            painter = painterResource(Res.drawable.ic_check),
            contentDescription = "Nickname Validation",
            tint = contentColor
        )
        Text(
            text = message,
            style = EveryLoLTheme.typography.subtitle04,
            color = contentColor
        )
    }
}