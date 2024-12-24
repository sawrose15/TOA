package com.sawrose.toa.addTask.domain.usecases

import com.google.common.truth.Truth.assertThat
import com.sawrose.toa.addTask.domain.model.AddTaskResult
import com.sawrose.toa.fake.FakePreferences
import com.sawrose.toa.fake.FakeTaskRepository
import com.sawrose.toa.model.Task
import com.sawrose.toa.preferences.UserPreferences
import com.sawrose.toa.withAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class AddTaskTest {
    private val fakeTaskRepository = FakeTaskRepository()

    private val userPreferences = UserPreferences(
        preferences = FakePreferences(),
    )

    @Test
    fun submitWithEmptyDescription() =
        runTest {
            val taskToSubmit = Task(
                "testing",
                description = "",
                scheduledDateMillis = ZonedDateTime.now()
                    .toInstant()
                    .toEpochMilli(),
                completed = false,
            )

            val expectedResult = AddTaskResult.Failure.InvalidInput(
                emptyDescription = true,
                scheduledDateInPast = false,
            )

            val actualResult = withAll(fakeTaskRepository, userPreferences) {
                addTask(
                    task = taskToSubmit,
                    ignoreTaskLimits = true,
                )
            }

            assertThat(actualResult).isEqualTo(expectedResult)
        }

    @Test
    fun submitWithBlankDescription() =
        runTest {
            val taskToSubmit = Task(
                "testing",
                description = "          ",
                scheduledDateMillis = ZonedDateTime.now()
                    .toInstant()
                    .toEpochMilli(),
                completed = false,
            )

            val expectedResult = AddTaskResult.Failure.InvalidInput(
                emptyDescription = true,
                scheduledDateInPast = false,
            )

            val actualResult = withAll(fakeTaskRepository, userPreferences) {
                addTask(
                    task = taskToSubmit,
                    ignoreTaskLimits = true,
                )
            }

            assertThat(actualResult).isEqualTo(expectedResult)
        }

    @Test
    fun submitWithPastDateDescription() =
        runTest {
            val taskToSubmit = Task(
                "testing",
                description = "Some Description",
                scheduledDateMillis = LocalDate.now().minusDays(1)
                    .atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli(),
                completed = false,
            )

            val expectedResult = AddTaskResult.Failure.InvalidInput(
                emptyDescription = false,
                scheduledDateInPast = true,
            )

            val actualResult = withAll(fakeTaskRepository, userPreferences) {
                addTask(
                    task = taskToSubmit,
                    ignoreTaskLimits = false,
                )
            }

            assertThat(actualResult).isEqualTo(expectedResult)
        }

    @Test
    fun submitValidTaskWithExtraWhitespace() =
        runTest {
            val inputTask = Task(
                id = "Some ID",
                description = "   Testing      ",
                scheduledDateMillis = ZonedDateTime.now()
                    .toInstant()
                    .toEpochMilli(),
                completed = false,
            )

            val expectedSavedTask = inputTask.copy(
                description = "Testing",
            )

            fakeTaskRepository.addTaskResults[expectedSavedTask] = Result.success(Unit)

            val expectedResult = AddTaskResult.Success
            val actualResult = withAll(fakeTaskRepository, userPreferences) {
                addTask(
                    task = inputTask,
                    ignoreTaskLimits = false,
                )
            }
            assertThat(actualResult).isEqualTo(expectedResult)
        }

    @Test
    fun submitWithoutPreferenceLimit() =
        runTest {
            val inputTask = Task(
                id = "Some ID",
                description = "   Testing",
                scheduledDateMillis = ZonedDateTime.now()
                    .toInstant()
                    .toEpochMilli(),
                completed = false,
            )

            val expectedSavedTask = inputTask.copy(
                description = "Testing",
            )

            fakeTaskRepository.addTaskResults[expectedSavedTask] = Result.success(Unit)

            val expectedResult = AddTaskResult.Success
            val actualResult = withAll(fakeTaskRepository, userPreferences) {
                addTask(
                    task = inputTask,
                    ignoreTaskLimits = false,
                )
            }
            assertThat(actualResult).isEqualTo(expectedResult)
        }

    @Test
    fun submitWithPreferenceLimit() =
        runTest {
            val today = ZonedDateTime.now()
                .toInstant()
                .toEpochMilli()

            // For sake of testing, lol
            userPreferences.setPreferredNumTasksPerDay(0)
            userPreferences.setPreferredNumTasksPerDayEnabled(true)

            // Mock an empty task list for this date.
            fakeTaskRepository.tasksForDateResults[Pair(today, false)] =
                flowOf(Result.success(emptyList()))

            val inputTask = Task(
                id = "Some ID",
                description = "   Testing",
                scheduledDateMillis = today,
                completed = false,
            )

            val expectedResult = AddTaskResult.Failure.MaxTasksPerDayExceeded

            val actualResult = withAll(fakeTaskRepository, userPreferences) {
                addTask(
                    task = inputTask,
                    ignoreTaskLimits = false,
                )
            }
            assertThat(actualResult).isEqualTo(expectedResult)
        }

    @Test
    fun submitWithIgnoringPreferenceLimit() =
        runTest {
            val today = ZonedDateTime.now()
                .toInstant()
                .toEpochMilli()

            // For sake of testing, lol
            userPreferences.setPreferredNumTasksPerDay(0)

            // Mock an empty task list for this date.
            fakeTaskRepository.tasksForDateResults[Pair(today, false)] =
                flowOf(Result.success(emptyList()))

            val inputTask = Task(
                id = "Some ID",
                description = "   Testing",
                scheduledDateMillis = today,
                completed = false,
            )

            val expectedSavedTask = inputTask.copy(
                description = "Testing",
            )

            fakeTaskRepository.addTaskResults[expectedSavedTask] = Result.success(Unit)

            val expectedResult = AddTaskResult.Success
            val actualResult = withAll(fakeTaskRepository, userPreferences) {
                addTask(
                    task = inputTask,
                    ignoreTaskLimits = true,
                )
            }
            assertThat(actualResult).isEqualTo(expectedResult)
        }

    /**
     * If our preferences say we have a limit on tasks,
     * but the preference is disabled, ensure that we ignore this limit.
     */
    @Test
    fun submitWithPreferenceLimitButDisabled() =
        runTest {
            userPreferences.setPreferredNumTasksPerDay(0)
            userPreferences.setPreferredNumTasksPerDayEnabled(false)

            val taskToSubmit = Task(
                id = "Testing",
                description = "Test",
                scheduledDateMillis = ZonedDateTime.now()
                    .toInstant()
                    .toEpochMilli(),
                completed = false,
            )

            // Mock a result for this task specifically
            // we do this to ensure in our test that the insert function is ultimately
            // called with the task that we expect it to be.
            fakeTaskRepository.addTaskResults[taskToSubmit] = Result.success(Unit)

            val expectedResult = AddTaskResult.Success

            val actualResult = withAll(fakeTaskRepository, userPreferences) {
                addTask(
                    task = taskToSubmit,
                    ignoreTaskLimits = false,
                )
            }

            assertThat(actualResult).isEqualTo(expectedResult)
        }
}
