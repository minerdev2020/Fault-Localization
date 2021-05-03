package com.minerdev.faultlocalization.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.databinding.ItemMessageBinding
import com.minerdev.faultlocalization.model.Message
import com.minerdev.faultlocalization.utils.Time
import java.util.*

class MessageListAdapter(diffCallback: DiffCallback) :
    ListAdapter<Message, MessageListAdapter.ViewHolder>(diffCallback) {
    lateinit var clickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(
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

    operator fun get(position: Int): Message {
        return getItem(position)
    }

    interface OnItemClickListener {
        fun onItemClick(viewHolder: ViewHolder, view: View, position: Int)
        fun onButtonClick(viewHolder: ViewHolder, view: View, position: Int)
    }

    class ViewHolder(
        val binding: ItemMessageBinding,
        listener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.messageItemLayout.setOnClickListener {
                listener.onItemClick(this@ViewHolder, itemView, bindingAdapterPosition)
            }

            binding.imageBtn.setOnClickListener {
                listener.onButtonClick(this@ViewHolder, itemView, bindingAdapterPosition)
            }
        }

        fun bind(message: Message) {
            binding.tvState.text = message.state.name
            binding.tvType.text = message.type.name
            binding.tvFrom.text = message.from.name
            binding.tvUpdatedAt.text = Time.getShortDate(message.updatedAt)

            val contents = message.equipment_info.name + " : " + message.contents
            binding.tvContents.text = contents

            val reply = message.replyer?.let { "${it.name}回答" } ?: "未被回答"
            binding.tvReplyer.text = reply
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