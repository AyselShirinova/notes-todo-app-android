package com.cscorner.notestodoapp.ui.completed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscorner.notestodoapp.databinding.FragmentCompletedTodoBinding
import com.cscorner.notestodoapp.ui.todo.ToDoViewModel
import com.cscorner.notestodoapp.ui.todo.TodoAdapter

class CompletedFragment : Fragment(){

    private var _binding: FragmentCompletedTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ToDoViewModel
    private lateinit var adapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentCompletedTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel= ViewModelProvider(requireActivity())[ToDoViewModel::class.java]

        adapter = TodoAdapter(onCheckboxClicked = {todo, isChecked ->
            val updatedTodo=todo.copy(isCompleted = isChecked)
            viewModel.updateTodo(updatedTodo)
        })
        binding.completedRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        binding.completedRecyclerView.adapter=adapter

        observeCompletedTodos()
    }

    private fun observeCompletedTodos(){
        viewModel.completedTodos.observe(viewLifecycleOwner){ todos->
            adapter.setTodos(todos)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}