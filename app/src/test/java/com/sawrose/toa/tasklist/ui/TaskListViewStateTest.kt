package com.sawrose.toa.tasklist.ui

import com.google.common.truth.Truth.assertThat
import com.sawrose.toa.R
import com.sawrose.toa.core.ui.UIText
import com.sawrose.toa.core.utils.getSuffixForDayOfMonth
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TaskListViewStateTest {
    @Test
    fun parseDateStringForYesterday() {
        val yesterday = LocalDate.now().minusDays(1)

        val viewState = TaskListViewState(
            selectedDate = yesterday,
        )

        val expectedString = UIText.ResourceText(R.string.yesterday)
        assertThat(viewState.selectedDateString).isEqualTo(expectedString)
    }

    @Test
    fun parseDateStringForToday() {
        val today = LocalDate.now()

        val viewState = TaskListViewState(
            selectedDate = today,
        )

        val expectedString = UIText.ResourceText(R.string.today)
        assertThat(viewState.selectedDateString).isEqualTo(expectedString)
    }

    @Test
    fun parseDateStringForTomorrow() {
        val tomorrow = LocalDate.now().plusDays(1)

        val viewState = TaskListViewState(
            selectedDate = tomorrow,
        )

        val expectedString = UIText.ResourceText(R.string.tommorrow)
        assertThat(viewState.selectedDateString).isEqualTo(expectedString)
    }

    @Test
    fun parseDateStringForFuture() {
        val twoDaysFromNow = LocalDate.now().plusDays(2)

        val viewState = TaskListViewState(
            selectedDate = twoDaysFromNow,
        )

        val expectedDateFormat = TaskListViewState.DATE_HEADER_FORMAT
        val expectedSuffix = twoDaysFromNow.getSuffixForDayOfMonth()
        val parsedDateString = DateTimeFormatter.ofPattern(expectedDateFormat).format(twoDaysFromNow)
        val expectedDateString = "$parsedDateString$expectedSuffix"
        val expectedString = UIText.StringText(expectedDateString)
        assertThat(viewState.selectedDateString).isEqualTo(expectedString)
    }
}
