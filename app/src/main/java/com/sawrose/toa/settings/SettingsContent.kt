package com.sawrose.toa.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sawrose.toa.R
import com.sawrose.toa.core.ui.theme.TOATheme

@Composable
fun SettingsContent(
    viewState: SettingsViewState,
    onNumTasksChanged: (String) -> Unit,
    onNumTasksEnabledChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.screen_padding))
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.preferences),
            style = MaterialTheme.typography.titleLarge,
        )

        NumTasksPerDayPreference(
            viewState = viewState,
            onNumTasksEnabledChanged = onNumTasksEnabledChanged,
            onNumTasksChanged = onNumTasksChanged,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun NumTasksPerDayPreference(
    viewState: SettingsViewState,
    onNumTasksEnabledChanged: (Boolean) -> Unit,
    onNumTasksChanged: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.num_tasks_per_day_label),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1F),
        )

        Switch(
            checked = viewState.numTasksPreferenceEnabled,
            onCheckedChange = onNumTasksEnabledChanged,
        )
    }

    OutlinedTextField(
        value = viewState.numTasksPerDay?.toString().orEmpty(),
        onValueChange = onNumTasksChanged,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
        ),
        modifier = Modifier
            .fillMaxWidth(),
        shape = CircleShape,
        enabled = viewState.numTasksPreferenceEnabled,
    )
}

@PreviewLightDark
@Composable
private fun SettingsContentPreview() {
    val viewState = SettingsViewState(
        numTasksPerDay = null,
        numTasksPreferenceEnabled = false,
    )

    TOATheme {
        Surface {
            SettingsContent(
                viewState = viewState,
                onNumTasksChanged = {},
                onNumTasksEnabledChanged = {},
            )
        }
    }
}
