package com.sawrose.toa.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sawrose.toa.R
import com.sawrose.toa.core.ui.theme.ButtonShape
import com.sawrose.toa.core.utils.toEpochMillisUTC
import com.sawrose.toa.core.utils.toLocalDateUTC
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val ENABLED_CONTENT_ALPHA = 1F
private const val DISABLED_CONTENT_ALPHA = 0.38F

/**
 * A custom text field looking composable that displays a date. Clicking on it, will allow the user
 * to select a new date by launching a [TOADatePickerDialog].
 *
 * In the future, we will want to support a disabled state using the supplied [enabled] boolean.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TOADatePickerInput(
    value: LocalDate,
    onValueChanged: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val showDatePicker = remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = value.toEpochMillisUTC(),
    )

    val contentColor = MaterialTheme.colorScheme.onSurface.enabled(enabled)

    val borderWidth = if (enabled) {
        1.dp
    } else {
        Dp.Hairline
    }

    if (showDatePicker.value) {
        TOADatePickerDialog(
            datePickerState = datePickerState,
            onDismiss = {
                showDatePicker.value = false
            },
            onComplete = { selectedDateMillis ->
                showDatePicker.value = false

                if (selectedDateMillis != null) {
                    onValueChanged.invoke(selectedDateMillis.toLocalDateUTC())
                }
            },
        )
    }

    Column(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = borderWidth,
                    color = contentColor,
                    shape = ButtonShape,
                )
                .clip(ButtonShape)
                .clickable {
                    showDatePicker.value = true
                },
        ) {
            DateAndIcon(
                value = value,
                textColor = contentColor,
                iconColorToUse = contentColor,
            )
        }
    }
}

@Composable
private fun DateAndIcon(
    value: LocalDate,
    textColor: Color,
    iconColorToUse: Color,
) {
    Row(
        modifier = Modifier
            .padding(16.dp),
    ) {
        Text(
            text = value.toUIString(),
            color = textColor,
            modifier = Modifier
                .weight(1F),
        )

        Icon(
            Icons.Default.DateRange,
            contentDescription = stringResource(R.string.select_date_content_description),
            tint = iconColorToUse,
        )
    }
}

private fun LocalDate.toUIString(): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
    return formatter.format(this)
}

/**
 * Modifies the alpha value of this [Color] based on whether it should
 * represent an [enabled] state or not.
 */
private fun Color.enabled(
    enabled: Boolean,
): Color {
    val alphaToUse = if (enabled) {
        ENABLED_CONTENT_ALPHA
    } else {
        DISABLED_CONTENT_ALPHA
    }

    return this.copy(alpha = alphaToUse)
}
