package com.minerdev.faultlocalization.custom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.databinding.MessageItemBinding
import com.minerdev.faultlocalization.model.Message
import com.minerdev.faultlocalization.model.Person
import java.util.*

class MessageListAdapter(diffCallback: DiffCallback) :
    ListAdapter<Message, MessageListAdapter.ViewHolder>(diffCallback) {
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MessageItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    operator fun get(position: Int): Message {
        return getItem(position)
    }

    interface OnItemClickListener {
        fun onItemClick(viewHolder: ViewHolder?, view: View?, position: Int)
    }

    class ViewHolder(val binding: MessageItemBinding, clickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.tvFrom.text = ""
            binding.tvState.text = message.state
            binding.tvType.text = message.type
            binding.tvContents.text = message.contents
        }

        init {
            binding.imageBtn.setOnClickListener {
                clickListener?.onItemClick(this@ViewHolder, itemView, bindingAdapterPosition)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }
}