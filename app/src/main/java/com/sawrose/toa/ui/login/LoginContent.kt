package com.sawrose.toa.ui.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sawrose.toa.R
import com.sawrose.toa.ui.components.PrimaryButton
import com.sawrose.toa.ui.components.SecondaryButton
import com.sawrose.toa.ui.components.TOATextField
import com.sawrose.toa.ui.components.VerticalSpacer
import com.sawrose.toa.ui.theme.TOATheme

private const val APP_LOGO_WIDTH_PERCENTAGE = 0.75F

/**
 * this composable maintain the entire login screen for handling user login
 * 
 * @param[viewState] the current state of the screen to render.
 */
@Composable
fun LoginContent(
    viewState: LoginViewState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        LogoInputColumn(
            viewState,
            onEmailChanged,
            onPasswordChanged,
            onLoginClicked,
            onSignUpClicked,
        )

    }
}

@Composable
private fun LogoInputColumn(
    viewState: LoginViewState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(
        dimensionResource(id = R.dimen.screen_padding)
    ),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
                end = contentPadding.calculateEndPadding(LocalLayoutDirection.current),
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VerticalSpacer(height = contentPadding.calculateTopPadding())

        AppLogo(
            modifier = Modifier
                .padding(vertical = 88.dp),
        )

        EmailInput(
            text = "viewState.credentials.email.value",
            onTextChanged = onEmailChanged,
            errorMessage = "(viewState as? LoginViewState.Active)",
//                ?.emailInputErrorMessage
//                ?.getString()",
            enabled = true
//            "viewState.inputsEnabled",
        )

        Spacer(modifier = Modifier.height(12.dp))

        TOATextField(
            text = viewState.password,
            onTextChanged = {},
            labelText = "Password"
        )

        Spacer(modifier = Modifier.height(12.dp))

        PrimaryButton(
            text = "Log in",
            onClick = { /*TODO*/ },
            backgroundColor = MaterialTheme.colors.secondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        SecondaryButton(
            text = "Sign Up",
            onClick = { /*TODO*/ },
        )
    }
}

@Composable
private fun EmailInput(
    text: String,
    onTextChanged: (String) -> Unit,
    errorMessage: String?,
    enabled: Boolean,
) {
    TOATextField(
        text = text,
        onTextChanged = onTextChanged,
        labelText = stringResource(R.string.email),
        errorMessage = errorMessage,
        enabled = enabled
    )
}

@Composable
private fun AppLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painterResource(id = R.drawable.ic_toa_checkmark),
        contentDescription = "App Logo",
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
        username = "",
        password = "",
    )
    TOATheme {
        LoginContent(
            viewState = viewState,
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClicked = {},
            onSignUpClicked = {},
        )
    }
}
