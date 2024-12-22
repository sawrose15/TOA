package com.sawrose.toa.model

data class Task(
    val id: String,
    val description: String,
    val scheduleTimeInMillis: Long,
    val completed: Boolean,
)
