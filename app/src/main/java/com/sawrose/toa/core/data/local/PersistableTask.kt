package com.sawrose.toa.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class PersistableTask(
    @PrimaryKey
    val id: String,
    val description: String,
    val scheduledDate: String,
    val completed: Boolean,
)
