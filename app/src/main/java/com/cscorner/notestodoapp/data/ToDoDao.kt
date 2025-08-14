package com.cscorner.notestodoapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ToDoDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: ToDo)

    @Update
    suspend fun update(todo: ToDo)

    @Delete
    suspend fun delete(todo: ToDo)

    @Query("SELECT * FROM todo_table")
    fun getAllTodos(): LiveData<List<ToDo>>

    @Query("SELECT * FROM todo_table WHERE isCompleted=0")
    fun getAllActive(): LiveData<List<ToDo>>

    @Query("SELECT * FROM todo_table WHERE isCompleted=1")
    fun getAllCompleted(): LiveData<List<ToDo>>
}