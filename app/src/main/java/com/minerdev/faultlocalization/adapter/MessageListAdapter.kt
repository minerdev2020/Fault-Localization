package com.minerdev.faultlocalization.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.databinding.MessageItemBinding
import com.minerdev.faultlocalization.model.Message
import com.minerdev.faultlocalization.utils.Time
import java.util.*

class MessageListAdapter(diffCallback: DiffCallback) :
    ListAdapter<Message, MessageListAdapter.ViewHolder>(diffCallback) {
    lateinit var listener: (viewHolder: ViewHolder?, view: View?, position: Int) -> Unit

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

    class ViewHolder(
        val binding: MessageItemBinding,
        listener: (viewHolder: ViewHolder?, view: View?, position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageBtn.setOnClickListener {
                listener(this@ViewHolder, itemView, bindingAdapterPosition)
            }
        }

        fun bind(message: Message) {
            binding.tvState.text = message.state.name
            binding.tvType.text = message.type.name
            binding.tvFrom.text = message.from.name
            binding.tvUpdatedAt.text = Time.getHMS(message.updatedAt)

            val contents = message.equipment_info.name + " : " +  message.contents
            binding.tvContents.text = contents

            binding.tvReplyer.text = message.replyer?.name ?: ""
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