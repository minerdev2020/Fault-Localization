package com.minerdev.faultlocalization.adapter

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.ItemTaskBinding
import com.minerdev.faultlocalization.model.Task
import java.util.*

class TaskListAdapter(diffCallback: DiffCallback) :
    ListAdapter<Task, TaskListAdapter.ViewHolder>(diffCallback) {
    lateinit var clickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    operator fun get(position: Int): Task {
        return getItem(position)
    }

    interface OnItemClickListener {
        fun onItemClick(viewHolder: ViewHolder, view: View, position: Int)
    }

    class ViewHolder(val binding: ItemTaskBinding, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageBtn.setOnClickListener {
                clickListener.onItemClick(
                    this@ViewHolder,
                    binding.imageBtn,
                    bindingAdapterPosition
                )
            }
        }

        fun bind(task: Task) {
            binding.tvName.text = task.target.name
            binding.tvState.text = task.state.name
            binding.ivProfile.setBackgroundResource(R.drawable.ic_launcher_background)
            binding.ivProfile.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}