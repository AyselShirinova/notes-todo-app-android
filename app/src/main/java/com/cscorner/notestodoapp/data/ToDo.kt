package com.cscorner.notestodoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "todo_table")
data class ToDo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val task: String,
    val isCompleted: Boolean = false
) : Serializable