package com.sawrose.toa.ui.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.sawrose.toa.ui.components.PrimaryButton
import com.sawrose.toa.ui.components.SecondaryButton
import com.sawrose.toa.ui.theme.TOATheme

/**
 * this composable maintain the entire login screen for handling user login
 * 
 * @param[viewState] the current state of the screen to render.
 */
@Composable
fun LoginScreen(
    viewState: LoginViewState,
) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
//            TOATextField(
//                text = ,
//                onTextChanged = ,
//                labelText =
//            )

            Spacer(modifier = Modifier.weight(1F))

            PrimaryButton(
                text = "Log in",
                onClick = { /*TODO*/ },
                backgroundColor = MaterialTheme.colors.secondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            SecondaryButton(
                text = "Sign Up",
                onClick = { /*TODO*/ },
                contentColor = MaterialTheme.colors.onPrimary
            )
        }

    }
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
        LoginScreen(viewState = viewState)
    }
}
