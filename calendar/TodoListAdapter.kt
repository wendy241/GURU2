package com.example.calendar_todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoListAdapter(
    private val todoItems: List<TodoItemWithCheck>,
    private val onAllItemsCheckedListener: (Boolean) -> Unit
) : RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item_layout, parent, false)
        return TodoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoItem = todoItems[position]
        holder.titleTextView.text = todoItem.title
        holder.descriptionTextView.text = todoItem.description
        holder.checkBox.isChecked = todoItem.isChecked

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            todoItem.isChecked = isChecked
        }
    }

    override fun getItemCount(): Int {
        return todoItems.size
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }
}
