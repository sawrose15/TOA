package com.sawrose.toa.login.ui

import com.sawrose.toa.CoroutinesTestRule
import com.sawrose.toa.R
import com.sawrose.toa.ThreadExceptionHandlerTestRule
import com.sawrose.toa.core.ui.UIText
import com.sawrose.toa.login.domain.model.Credentials
import com.sawrose.toa.login.domain.model.Email
import com.sawrose.toa.login.domain.model.InvalidCredentialsException
import com.sawrose.toa.login.domain.model.Password
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {
    private lateinit var testRobot: LoginViewModelRobot

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    @get:Rule
    val threadExceptionHandlerTestRule = ThreadExceptionHandlerTestRule()

    @Before
    fun setUp() {
        testRobot = LoginViewModelRobot()
    }

    @Test
    fun testUpdateCredentials() =
        runTest {
            val testEmail = "testy@mctestface.com"
            val testPassword = "Hunter2"

            val initialState = LoginViewState.Initial

            val emailEnteredState = LoginViewState.Active(
                credentials = Credentials(
                    email = Email(testEmail),
                ),
            )
            val emailPasswordEnteredState = LoginViewState.Active(
                credentials = Credentials(
                    email = Email(testEmail),
                    password = Password(testPassword),
                ),
            )

            val expectedViewState = listOf(
                initialState,
                emailEnteredState,
                emailPasswordEnteredState,
            )

            testRobot
                .buildViewModel()
                .expectViewState(
                    action = {
                        enterEmail(testEmail)
                        enterPassword(testPassword)
                    },
                    viewStates = expectedViewState,
                )
        }

    @Test
    fun testSubmitInvalidCredentials() =
        runTest {
            val testEmail = "testy@mctestface.com"
            val testPassword = "Hunter2"

            val completeCredential = Credentials(
                email = Email(testEmail),
                password = Password(testPassword),
            )

            val initialState = LoginViewState.Initial

            val emailEnteredState = LoginViewState.Active(
                credentials = Credentials(
                    email = Email(testEmail),
                ),
            )
            val emailPasswordEnteredState = LoginViewState.Active(
                credentials = Credentials(
                    email = Email(testEmail),
                    password = Password(testPassword),
                ),
            )

            val submittingState = LoginViewState.Submitting(
                credentials = completeCredential,
            )

            val submittingError = LoginViewState.SubmissionError(
                credentials = completeCredential,
                errorMessage = UIText.ResourceText(
                    value = R.string.err_invalid_credentials,
                ),
            )

            val expectViewState = listOf(
                initialState,
                emailEnteredState,
                emailPasswordEnteredState,
                submittingState,
                submittingError,
            )

            testRobot
                .buildViewModel()
                .mockLoginResultForCredentials(
                    credentials = completeCredential,
                    result = Result.failure(InvalidCredentialsException()),
                )
                .expectViewState(
                    action = {
                        enterEmail(testEmail)
                        enterPassword(testPassword)
                        clickLoginButton()
                    },
                    viewStates = expectViewState,
                )
        }

    @Test
    fun testUnknownLoginError() =
        runTest {
            val testEmail = "testy@mctestface.com"
            val testPassword = "Hunter2"

            val completeCredential = Credentials(
                email = Email(testEmail),
                password = Password(testPassword),
            )

            val initialState = LoginViewState.Initial
            val emailEnteredState = LoginViewState.Active(
                credentials = Credentials(
                    email = Email(testEmail),
                ),
            )

            val emailPasswordEnteredState = LoginViewState.Active(
                credentials = Credentials(
                    email = Email(testEmail),
                    password = Password(testPassword),
                ),
            )

            val submittingState = LoginViewState.Submitting(
                credentials = completeCredential,
            )

            val submissionErrorState = LoginViewState.SubmissionError(
                credentials = completeCredential,
                errorMessage = UIText.ResourceText(
                    value = R.string.err_login_failure,
                ),
            )

            val expectedViewState = listOf(
                initialState,
                emailEnteredState,
                emailPasswordEnteredState,
                submittingState,
                submissionErrorState,
            )

            testRobot
                .buildViewModel()
                .mockLoginResultForCredentials(
                    credentials = completeCredential,
                    result = Result.failure(Exception()),
                )
                .expectViewState(
                    action = {
                        enterEmail(testEmail)
                        enterPassword(testPassword)
                        clickLoginButton()
                    },
                    viewStates = expectedViewState,
                )
        }

    @Test
    fun submittingWithoutCredential() =
        runTest {
            val credentails = Credentials()
            val initialState = LoginViewState.Initial
            val submittingState = LoginViewState.Submitting(
                credentials = credentails,
            )

            val invalidInputState = LoginViewState.Active(
                credentials = credentails,
                emailInputErrorMessage = UIText.ResourceText(
                    value = R.string.err_empty_email,
                ),
                passwordInputErrorMessage = UIText.ResourceText(
                    value = R.string.err_empty_password,
                ),
            )

            val expectedViewState = listOf(
                initialState,
                submittingState,
                invalidInputState,
            )

            testRobot
                .buildViewModel()
                .expectViewState(
                    action = {
                        clickLoginButton()
                    },
                    viewStates = expectedViewState,
                )
        }

    @Test
    fun testClearErrorAfterInput() =
        runTest {
            val credential = Credentials()
            val testEmail = "testy@mctestface.com"
            val testPassword = "Hunter2"

            val initialState = LoginViewState.Initial
            val submittingState = LoginViewState.Submitting(
                credentials = credential,
            )

            val invalidInputCredential = LoginViewState.Active(
                credentials = credential,
                emailInputErrorMessage = UIText.ResourceText(
                    value = R.string.err_empty_email,
                ),
                passwordInputErrorMessage = UIText.ResourceText(
                    value = R.string.err_empty_password,
                ),
            )

            val emailEnteredState = LoginViewState.Active(
                credentials = Credentials(email = Email(testEmail)),
                emailInputErrorMessage = null,
                passwordInputErrorMessage = UIText.ResourceText(
                    value = R.string.err_empty_password,
                ),
            )

            val emailPasswordEnteredState = LoginViewState.Active(
                credentials = Credentials(
                    email = Email(testEmail),
                    password = Password(testPassword),
                ),
                emailInputErrorMessage = null,
                passwordInputErrorMessage = null,
            )

            val expectedViewState = listOf(
                initialState,
                submittingState,
                invalidInputCredential,
                emailEnteredState,
                emailPasswordEnteredState,
            )

            testRobot
                .buildViewModel()
                .expectViewState(
                    action = {
                        clickLoginButton()
                        enterEmail(testEmail)
                        enterPassword(testPassword)
                    },
                    viewStates = expectedViewState,
                )
        }
}
