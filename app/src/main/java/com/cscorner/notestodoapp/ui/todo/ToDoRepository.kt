package com.cscorner.notestodoapp.ui.todo

import androidx.lifecycle.LiveData
import com.cscorner.notestodoapp.data.ToDo
import com.cscorner.notestodoapp.data.ToDoDao

class ToDoRepository(private val todoDao: ToDoDao) {
    val activeTodos: LiveData<List<ToDo>> = todoDao.getAllActive()
    val completedTodos: LiveData<List<ToDo>> = todoDao.getAllCompleted()

    suspend fun insert(todo: ToDo) {
        todoDao.insert(todo)
    }

    suspend fun update(todo: ToDo) {
        todoDao.update(todo)
    }

    suspend fun delete(todo: ToDo) {
        todoDao.delete(todo)
    }

}