package com.cscorner.notestodoapp.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscorner.notestodoapp.R
import com.cscorner.notestodoapp.data.AppDatabase
import com.cscorner.notestodoapp.data.ToDo
import com.cscorner.notestodoapp.databinding.FragmentToDoBinding

class ToDoFragment : Fragment() {

    private var allTodos = listOf<ToDo>()
    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ToDoViewModel
    private lateinit var adapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = AppDatabase.getDatabase(requireContext()).todoDao()
        val repository = ToDoRepository(dao)
        val factory = ToDoViewModelFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory)[ToDoViewModel::class.java]

        adapter = TodoAdapter(onCheckboxClicked = { todo, isChecked ->
            val updatedTodo = todo.copy(isCompleted = isChecked)
            viewModel.updateTodo(updatedTodo)
        }, onDeleteClicked = { todo ->
            viewModel.deleteTodo(todo)
        }, onEditClicked = { todo ->
            showAddOrEditDialog(todo)
        })

        binding.TodoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.TodoRecyclerView.adapter = adapter

        viewModel.activeTodos.observe(viewLifecycleOwner) {
            adapter.setTodos(it)
        }
        //searchView
        binding.TodoSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterTodos(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterTodos(newText ?: "")
                return true
            }
        })

        binding.addTodoButton.setOnClickListener {
            showAddOrEditDialog()
        }
    }

    private fun filterTodos(query: String) {
        val filtered = if (query.isBlank()) {
            allTodos
        } else {
            allTodos.filter { it.task.contains(query, ignoreCase = true) }
        }
        adapter.setTodos(filtered)
    }


    fun showAddOrEditDialog(existingTodo: ToDo? = null) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.fragment_add_to_do, null)
        val editText = dialogView.findViewById<EditText>(R.id.AddToDo)

        val isEdit = existingTodo != null

        if (isEdit) {
            editText.setText(existingTodo?.task)
        }

        val dialog =
            AlertDialog.Builder(requireContext()).setTitle(if (isEdit) "Edit Todo" else "Add Todo")
                .setView(dialogView).setPositiveButton(if (isEdit) "Update" else "Add") { _, _ ->
                    val todoText = editText.text.toString().trim()
                    if (todoText.isNotEmpty()) {
                        val updatedTodo =
                            existingTodo?.copy(task = todoText) ?: ToDo(task = todoText)
                        if (isEdit) {
                            viewModel.updateTodo(updatedTodo)
                            Toast.makeText(requireContext(), "Todo updated", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            viewModel.addTodo(updatedTodo)
                            Toast.makeText(requireContext(), "Todo added", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Please enter content", Toast.LENGTH_SHORT)
                            .show()
                    }
                }.setNegativeButton("Cancel", null).create()
        dialog.show()
    }
}