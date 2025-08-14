package com.cscorner.notestodoapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    @ColumnInfo(name = "Title") val title: String,
    @ColumnInfo(name = "Note") val note: String,
    @ColumnInfo(name = "Date") val date: Long
) : Serializable