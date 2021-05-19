package com.minerdev.faultlocalization.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.databinding.ItemRequestBinding
import com.minerdev.faultlocalization.model.Request
import com.minerdev.faultlocalization.utils.Time
import java.util.*

class RequestListAdapter(diffCallback: DiffCallback) :
    ListAdapter<Request, RequestListAdapter.ViewHolder>(diffCallback) {
    lateinit var clickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRequestBinding.inflate(
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

    operator fun get(position: Int): Request {
        return getItem(position)
    }

    interface OnItemClickListener {
        fun onItemClick(viewHolder: ViewHolder, view: View, position: Int)
        fun onButtonClick(viewHolder: ViewHolder, view: View, position: Int)
    }

    class ViewHolder(
        val binding: ItemRequestBinding,
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

        fun bind(request: Request) {
            binding.tvState.text = request.state.name
            binding.tvType.text = request.type.name
            binding.tvFrom.text = request.from.name
            binding.tvUpdatedAt.text = Time.getShortDate(request.updatedAt)

            val contents = request.equipment_info.name + " : " + request.contents
            binding.tvContents.text = contents

            val reply = request.replyer?.let { "${it.name}回答" } ?: "未被回答"
            binding.tvReplyer.text = reply
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Request>() {
        override fun areItemsTheSame(oldItem: Request, newItem: Request): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Request, newItem: Request): Boolean {
            return oldItem == newItem
        }
    }
}