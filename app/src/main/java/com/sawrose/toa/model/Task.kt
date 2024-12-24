package com.sawrose.toa.model

data class Task(
    val id: String,
    val description: String,
    val scheduledDateMillis: Long,
    val completed: Boolean,
)
