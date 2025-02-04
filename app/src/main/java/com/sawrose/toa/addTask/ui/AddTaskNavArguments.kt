package com.sawrose.toa.addTask.ui

import java.time.LocalDate

/**
 * Information required when launching the add task screen.
 */
data class AddTaskNavArguments(
    val initialDate: LocalDate,
)
