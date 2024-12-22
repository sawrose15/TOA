package com.sawrose.toa.core.ui.components

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.window.Dialog
import com.sawrose.toa.core.ui.theme.TOATheme
import com.sawrose.toa.core.utils.toEpochMillisUTC
import java.time.LocalDate

/**
 * This is a [Dialog] that will render a [DatePicker] and allow the user to select any date today
 * or in the future. This is most likely going to be triggered from within a [TOADatePickerInput].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TOADatePickerDialog(
    datePickerState: DatePickerState,
    onDismiss: () -> Unit,
    onComplete: (Long?) -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onComplete(datePickerState.selectedDateMillis) }) {
                Text(text = "DONE")
            }
        },
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    ) {
        DatePicker(state = datePickerState)
    }
}

object TOADatePickerDialog {
    /**
     * Custom implementation of [SelectedDates] that restricts to only select dates
     * in the future.
     * */

    @OptIn(ExperimentalMaterial3Api::class)
    val FutureDates = object : SelectableDates {
        override fun isSelectableDate(
            utcTimeMillis: Long,
        ): Boolean {
            val todayStartMillis = LocalDate.now().toEpochMillisUTC()
            return utcTimeMillis >= todayStartMillis
        }

        override fun isSelectableYear(
            year: Int,
        ): Boolean {
            val todayYear = LocalDate.now().year
            return year >= todayYear
        }
    }
}

@ExperimentalMaterial3Api
@PreviewLightDark
@Composable
private fun TOADatePickerDialogPreview() {
    TOATheme {
        TOADatePickerDialog(
            datePickerState = rememberDatePickerState(),
            onDismiss = {},
            onComplete = {},
        )
    }
}
