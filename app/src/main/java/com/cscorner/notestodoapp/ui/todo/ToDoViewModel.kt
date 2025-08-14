package com.cscorner.notestodoapp.ui.todo

import android.app.Application
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cscorner.notestodoapp.data.AppDatabase
import com.cscorner.notestodoapp.data.ToDo
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ToDoRepository
    val activeTodos: LiveData<List<ToDo>>
    val completedTodos: LiveData<List<ToDo>>

    init {
        val dao= AppDatabase.getDatabase(application).todoDao()
        repository = ToDoRepository(dao)
        activeTodos = repository.activeTodos
        completedTodos = repository.completedTodos
    }

    fun addTodo(todo: ToDo) {
        viewModelScope.launch {
            repository.insert(todo)
        }
    }

    fun updateTodo(todo: ToDo) {
        viewModelScope.launch {
            repository.update(todo)
        }
    }

    fun deleteTodo(todo: ToDo) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }

}

