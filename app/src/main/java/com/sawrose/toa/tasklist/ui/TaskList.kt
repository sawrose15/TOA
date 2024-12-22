package com.sawrose.toa.tasklist.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sawrose.toa.R
import com.sawrose.toa.core.ui.theme.TOATheme
import com.sawrose.toa.model.Task

@Composable
fun TaskList(
    incompleteTasks: List<Task>,
    completedTasks: List<Task>,
    onRescheduleClicked: (Task) -> Unit,
    onDoneClicked: (Task) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        if (incompleteTasks.isEmpty()) {
            item {
                EmptySectionCard(
                    text = stringResource(id = R.string.no_incomplete_tasks),
                )
            }
        } else {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp,
                    ),
                ) {
                    incompleteTasks.forEachIndexed { index, task ->
                        TaskListItem(
                            task = task,
                            onRescheduleClicked = {
                                onRescheduleClicked(task)
                            },
                            onDoneClicked = {
                                onDoneClicked(task)
                            },
                            modifier = Modifier
                                .testTag("Incomplete Task ${task.id}")
                                .animateItem(),
                        )

                        if (index != incompleteTasks.lastIndex) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surface,
                            )
                        }
                    }
                }
            }
        }

        if (completedTasks.isNotEmpty()) {
            item {
                SectionHeader(
                    text = stringResource(id = R.string.completed_tasks),
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp,
                    ),
                ) {
                    completedTasks.forEachIndexed { index, task ->
                        TaskListItem(
                            task = task,
                            onRescheduleClicked = { onRescheduleClicked(task) },
                            onDoneClicked = { onDoneClicked(task) },
                            modifier = Modifier
                                .testTag("Completed Task ${task.id}")
                                .animateItem(),
                        )

                        if (index != completedTasks.lastIndex) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surface,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptySectionCard(
    text: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
private fun SectionHeader(
    text: String,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
    )
}

@Preview(
    name = "Night Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Day Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
@Suppress("UnusedPrivateMember")
private fun FullTaskListPreview() {
    val incompleteTasks = (1..3).map { index ->
        Task(
            id = "$index",
            description = "Test task: $index",
            scheduleTimeInMillis = 0L,
            completed = false,
        )
    }

    val completedTasks = (1..3).map { index ->
        Task(
            id = "$index",
            description = "Test task: $index",
            scheduleTimeInMillis = 0L,
            completed = true,
        )
    }

    TOATheme {
        TaskList(
            incompleteTasks = incompleteTasks,
            completedTasks = completedTasks,
            onRescheduleClicked = {},
            onDoneClicked = {},
        )
    }
}

@Preview(
    name = "Night Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Day Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
@Suppress("UnusedPrivateMember")
private fun NoTasksListPreview() {
    TOATheme {
        TaskList(
            incompleteTasks = emptyList(),
            completedTasks = emptyList(),
            onRescheduleClicked = {},
            onDoneClicked = {},
        )
    }
}
