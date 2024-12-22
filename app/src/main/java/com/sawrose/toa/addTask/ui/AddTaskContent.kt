package com.sawrose.toa.addTask.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.sawrose.toa.R
import com.sawrose.toa.addTask.domain.model.TaskInput
import com.sawrose.toa.core.ui.UIText
import com.sawrose.toa.core.ui.components.Material3CircularProgressIndicator
import com.sawrose.toa.core.ui.components.PrimaryButton
import com.sawrose.toa.core.ui.components.TOADatePickerInput
import com.sawrose.toa.core.ui.components.TOATextField
import com.sawrose.toa.core.ui.components.VerticalSpacer
import com.sawrose.toa.core.ui.getString
import com.sawrose.toa.core.ui.theme.TOATheme
import java.time.LocalDate

const val ADD_TASK_DESCRIPTION_INPUT_TAG = "ADD_TASK_DESCRIPTION_INPUT"

@Composable
fun AddTaskContent(
    viewState: AddTaskViewState,
    onTaskDescriptionChanged: (String) -> Unit,
    onTaskScheduledDateChanged: (LocalDate) -> Unit,
    onSubmitClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val descriptionFocusRequester = remember {
        FocusRequester()
    }
    
    LaunchedEffect(Unit){
        descriptionFocusRequester.requestFocus()
    }

    Box(
        modifier = modifier
    ) {
        AddTaskInputColumn(
            viewState = viewState,
            onTaskDescriptionChanged = onTaskDescriptionChanged,
            onTaskScheduledDateChanged = onTaskScheduledDateChanged,
            onSubmitClicked = onSubmitClicked,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            descriptionFocusRequester = descriptionFocusRequester,
        )

        if(viewState is AddTaskViewState.Submitting){
            Material3CircularProgressIndicator(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)
            )
        }
    }

}

@Composable
fun AddTaskInputColumn(
    viewState: AddTaskViewState,
    onTaskDescriptionChanged: (String) -> Unit,
    onTaskScheduledDateChanged: (LocalDate) -> Unit,
    onSubmitClicked: () -> Unit,
    modifier: Modifier,
    descriptionFocusRequester: FocusRequester,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.form_spacing)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(
            text = "What would you like to accomplish?",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

        TOATextField(
            text = viewState.taskInput.description,
            onTextChanged = onTaskDescriptionChanged,
            labelText = null,
            enabled = viewState.inputsEnabled,
            errorMessage = (viewState as? AddTaskViewState.Active)
                ?.descriptionInputErrorMessage
                ?.getString(),
            focusRequester = descriptionFocusRequester,
            modifier = Modifier
                .testTag(ADD_TASK_DESCRIPTION_INPUT_TAG)
        )

        Text(
            text = "When would you like to do it?",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )

        TOADatePickerInput(
            value = viewState.taskInput.scheduledDate,
            onValueChanged = onTaskScheduledDateChanged,
            modifier = Modifier
                .fillMaxWidth(),
        )

        if (viewState is AddTaskViewState.SubmissionError) {
            Text(
                text = viewState.errorMessage.getString(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(top = 12.dp),
            )
        }

        VerticalSpacer(height = dimensionResource(id = R.dimen.form_spacing))

        PrimaryButton(
            text = viewState.submitButtonText.getString(),
            onClick = onSubmitClicked,
            enabled = viewState.inputsEnabled,
        )
    }
}

@Preview(
    name = "Night Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "id:pixel_6_pro",
)
@Preview(
    name = "Day Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "id:pixel_6_pro",
)
@Composable
@Suppress("UnusedPrivateMember")
private fun AddTaskContentPreview(
    @PreviewParameter(AddTaskViewStateProvider::class)
    addTaskViewState: AddTaskViewState,
) {
    TOATheme {
        Surface {
            AddTaskContent(
                addTaskViewState,
                {},
                {},
                {},
                Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.screen_padding)),
            )
        }
    }
}

class AddTaskViewStateProvider : PreviewParameterProvider<AddTaskViewState> {

    override val values: Sequence<AddTaskViewState>
        get() {
            val activeInput = TaskInput(
                description = "Buy an NFT",
                scheduledDate = LocalDate.now(),
            )

            return sequenceOf(
                AddTaskViewState.Initial(),
                AddTaskViewState.Active(
                    taskInput = activeInput,
                ),
                AddTaskViewState.Submitting(
                    taskInput = activeInput,
                ),
                AddTaskViewState.SubmissionError(
                    taskInput = activeInput,
                    errorMessage = UIText.StringText("Don't buy NFTs."),
                ),
                AddTaskViewState.Completed,
            )
        }
}
