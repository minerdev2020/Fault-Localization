package com.minerdev.faultlocalization.custom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.databinding.MessageItemBinding
import com.minerdev.faultlocalization.model.Message
import java.util.*

class MessageListAdapter : RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {
    var items = ArrayList<Message>()
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
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnItemClickListener {
        fun onItemClick(viewHolder: ViewHolder?, view: View?, position: Int)
    }

    class ViewHolder(val binding: MessageItemBinding, clickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.tvFrom.text = message.fromWho
            binding.tvState.text = message.state.toString()
            binding.tvType.text = message.type.toString()
            binding.tvContents.text = message.contents
        }

        init {
            binding.imageBtn.setOnClickListener {
                clickListener?.onItemClick(this@ViewHolder, itemView, bindingAdapterPosition)
            }
        }
    }
}