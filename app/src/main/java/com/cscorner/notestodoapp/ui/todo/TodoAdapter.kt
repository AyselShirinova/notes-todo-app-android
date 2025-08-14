package com.cscorner.notestodoapp.ui.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cscorner.notestodoapp.R
import com.cscorner.notestodoapp.data.ToDo

class TodoAdapter(
    private val onCheckboxClicked: (ToDo, Boolean) -> Unit,
    private val onDeleteClicked: ((ToDo) -> Unit)? = null,
    private val onEditClicked: ((ToDo) -> Unit)? = null
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    var todoList = mutableListOf<ToDo>()

    fun setTodos(newList: List<ToDo>) {
        todoList = newList.toMutableList()
        notifyDataSetChanged()
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(todo: ToDo) {
            //check checkbox
            checkBox.isChecked = todo.isCompleted
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = todo.isCompleted
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                onCheckboxClicked(todo, isChecked)
            }

            //edit todo
            val todoText: TextView = itemView.findViewById(R.id.todoText)
            todoText.text = todo.task
            todoText.setOnClickListener {
                onEditClicked?.invoke(todo)
            }

            //delete todo
            deleteButton.setOnClickListener {
                onDeleteClicked?.invoke(todo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TodoViewHolder, position: Int
    ) {
        val todo = todoList[position]
        holder.bind(todo)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
}