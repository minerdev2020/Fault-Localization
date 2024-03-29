package com.minerdev.faultlocalization.adapter

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.ItemPersonBinding
import com.minerdev.faultlocalization.model.Person
import java.util.*

class PersonListAdapter(diffCallback: DiffCallback) :
    ListAdapter<Person, PersonListAdapter.ViewHolder>(diffCallback) {
    lateinit var clickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPersonBinding.inflate(
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

    operator fun get(position: Int): Person {
        return getItem(position)
    }

    interface OnItemClickListener {
        fun onItemClick(viewHolder: ViewHolder, view: View, position: Int)
        fun onItemLongClick(viewHolder: ViewHolder, view: View, position: Int)
    }

    class ViewHolder(val binding: ItemPersonBinding, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.personItemLayout.setOnLongClickListener {
                clickListener.onItemLongClick(this@ViewHolder, itemView, bindingAdapterPosition)
                true
            }

            binding.imageBtn.setOnClickListener {
                clickListener.onItemClick(
                    this@ViewHolder,
                    binding.imageBtn,
                    bindingAdapterPosition
                )
            }
        }

        fun bind(person: Person) {
            binding.tvName.text = person.name
            binding.tvState.text = person.state.name
            binding.tvType.text = person.type.name
            binding.tvPhone.text = person.phone
            binding.ivProfile.setBackgroundResource(R.drawable.ic_launcher_background)
            binding.ivProfile.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem == newItem
        }
    }
}