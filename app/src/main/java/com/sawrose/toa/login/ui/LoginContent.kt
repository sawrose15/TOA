package com.sawrose.toa.login.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sawrose.toa.R
import com.sawrose.toa.core.ui.components.PrimaryButton
import com.sawrose.toa.core.ui.components.SecondaryButton
import com.sawrose.toa.core.ui.components.TOATextField
import com.sawrose.toa.core.ui.components.VerticalSpacer
import com.sawrose.toa.core.ui.theme.TOATheme

private const val APP_LOGO_WIDTH_PERCENTAGE = 0.75F

/**
 * this composable maintain the entire login screen for handling user login
 * 
 * @param[viewState] the current state of the screen to render.
 * @param[onEmailChanged] a callback invoked when the user enters their email
 * @param[onPasswordChanged] a callback invoked when the user entered the password
 * @param[onLoginClicked] a callback invoked when the user clicks the login button.
 * @param[onSignUpClicked] a callback invoked when the user clicks the sign up button.
 */
@Composable
fun LoginContent(
    viewState: LoginViewState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
) {
    
    Surface(
        color = MaterialTheme.colors.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.screen_padding)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1F))

            AppLogo()

            Spacer(modifier = Modifier.weight(1F))

            EmailInput(
                text = viewState.userName,
                onTextChanged = onEmailChanged,
            )

            VerticalSpacer(height = 12.dp)

            PasswordInput(
                text = viewState.password,
                onTextChanged = onPasswordChanged,
            )

            VerticalSpacer(height = 48.dp)

            LoginButton(
                onClick = onLoginClicked
            )

            VerticalSpacer(height = 12.dp)

            SignUpButton(
                onClick = onSignUpClicked
            )
        }
    }
}

@Composable
private fun SignUpButton(
    onClick : () -> Unit
) {
    SecondaryButton(
        text = stringResource(R.string.sign_up),
        onClick = onClick,
    )
}

@Composable
private fun LoginButton(
    onClick: () -> Unit
) {
    PrimaryButton(
        text = stringResource(R.string.log_in),
        onClick = onClick,
    )
}

@Composable
private fun PasswordInput(
    text: String,
    onTextChanged: (String) -> Unit
) {
    TOATextField(
        text = text,
        onTextChanged = onTextChanged,
        labelText = stringResource(R.string.password),
    )
}

@Composable
private fun EmailInput(
    text: String,
    onTextChanged: (String) -> Unit
) {
    TOATextField(
        text = text,
        onTextChanged = onTextChanged,
        labelText = stringResource(R.string.email),
    )
}

@Composable
private fun AppLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painterResource(id = R.drawable.ic_toa_checkmark),
        contentDescription = stringResource(R.string.app_logo_container),
        modifier = modifier
            .fillMaxWidth(APP_LOGO_WIDTH_PERCENTAGE)
    )
}

@Preview(
    name = "Night Mode - Empty",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "Day Mode - Empty",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun LoginScreenPreview() {
    val viewState = LoginViewState(
        email = "",
        password = "",
    )
    TOATheme {
        LoginContent(
            viewState = viewState,
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClicked = {},
            onSignUpClicked = {}
        )
    }
}
