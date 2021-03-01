package com.minerdev.faultlocalization.custom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.PersonItemBinding
import com.minerdev.faultlocalization.model.Person
import java.util.*

class PersonListAdapter : RecyclerView.Adapter<PersonListAdapter.ViewHolder>() {
    var items = ArrayList<Person>()
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PersonItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnItemClickListener {
        fun onItemClick(viewHolder: ViewHolder?, view: View?, position: Int)
        fun onItemLongClick(
            viewHolder: ViewHolder?, view: View?, position: Int
        )
    }

    class ViewHolder(val binding: PersonItemBinding, clickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(person: Person) {
            binding.tvName.text = person.name
            binding.tvState.text = person.state
            binding.tvType.text = person.type
            binding.tvPhone.text = person.phone
            binding.ivProfile.setBackgroundResource(R.drawable.ic_launcher_background)
            binding.ivProfile.setImageResource(R.drawable.ic_launcher_foreground)
        }

        init {
            binding.personItemLayout.setOnLongClickListener {
                clickListener?.onItemLongClick(this@ViewHolder, itemView, bindingAdapterPosition)
                true
            }

            binding.imageBtn.setOnClickListener {
                clickListener?.onItemClick(this@ViewHolder, itemView, bindingAdapterPosition)
            }
        }
    }
}