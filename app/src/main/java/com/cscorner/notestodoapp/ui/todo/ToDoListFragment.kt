package com.cscorner.notestodoapp.ui.todo

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscorner.notestodoapp.R
import com.cscorner.notestodoapp.data.AppDatabase
import com.cscorner.notestodoapp.databinding.FragmentToDoBinding
import kotlinx.coroutines.launch

class ToDoListFragment : Fragment() {

    private lateinit var binding: FragmentToDoBinding
    private lateinit var viewModel: ToDoViewModel
    private lateinit var adapter: TodoAdapter
    private lateinit var factory: ToDoViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = AppDatabase.getDatabase(requireContext())
        val repository = ToDoRepository(dao.todoDao())
        factory = ToDoViewModelFactory(Application(), repository)
        viewModel = ViewModelProvider(this, factory)[ToDoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TodoAdapter(onCheckboxClicked = { todo, isChecked ->
            lifecycleScope.launch {
                viewModel.updateTodo(todo.copy(isCompleted = isChecked))
            }
        }, onDeleteClicked = { todo ->
            lifecycleScope.launch {
                viewModel.deleteTodo(todo)
            }
        })

        binding.TodoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.TodoRecyclerView.adapter = adapter

        viewModel.activeTodos.observe(viewLifecycleOwner) {
            adapter.setTodos(it)
        }
        binding.addTodoButton.setOnClickListener {
            findNavController().navigate(R.id.action_todo_fragment_to_addToDo)
        }
    }
}