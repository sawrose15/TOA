package com.sawrose.toa.tasklist.ui

import com.sawrose.toa.R
import com.sawrose.toa.core.ui.AlertMessage
import com.sawrose.toa.core.ui.UIText
import com.sawrose.toa.core.utils.getSuffixForDayOfMonth
import com.sawrose.toa.model.Task
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class TaskListViewState(
    val showLoading: Boolean = true,
    val incompleteTasks: List<Task>? = null,
    val completedTask: List<Task>? = null,
    val errorMessage: UIText? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val taskToReschedule: Task? = null,
    val alertMessage: List<AlertMessage> = emptyList(),
) {
    /**
     * As long as we are not in a loading or error scenario, we can show the task list (or empty state).
     */
    val showTask: Boolean
        get() = !showLoading && errorMessage == null

    val selectedDateString: UIText
        get() {
            val today = LocalDate.now()
            val isYesterday = selectedDate == today.minusDays(1)
            val isToday = selectedDate == today
            val isTomorrow = selectedDate == today.plusDays(1)
            return when {
                isYesterday -> UIText.ResourceText(R.string.yesterday)
                isToday -> UIText.ResourceText(R.string.today)
                isTomorrow -> UIText.ResourceText(R.string.tommorrow)
                else -> {
                    val suffix = selectedDate.getSuffixForDayOfMonth()
                    val dateString =
                        DateTimeFormatter.ofPattern(DATE_HEADER_FORMAT).format(selectedDate)
                    UIText.StringText("$dateString$suffix")
                }
            }
        }

    companion object {
        const val DATE_HEADER_FORMAT = "MMM d"
    }
}
