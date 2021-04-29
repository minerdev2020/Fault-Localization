package com.minerdev.faultlocalization.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.databinding.ItemAlertBinding
import com.minerdev.faultlocalization.model.Alert
import com.minerdev.faultlocalization.utils.Time
import java.util.*

class AlertListAdapter(diffCallback: DiffCallback) :
    ListAdapter<Alert, AlertListAdapter.ViewHolder>(diffCallback) {
    lateinit var clickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlertBinding.inflate(
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

    operator fun get(position: Int): Alert {
        return getItem(position)
    }

    interface OnItemClickListener {
        fun onItemClick(viewHolder: ViewHolder, view: View, position: Int)
        fun onButtonClick(viewHolder: ViewHolder, view: View, position: Int)
    }

    class ViewHolder(
        val binding: ItemAlertBinding,
        listener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.alertItemLayout.setOnClickListener {
                listener.onItemClick(this@ViewHolder, itemView, bindingAdapterPosition)
            }
        }

        fun bind(alert: Alert) {
            binding.tvId.text = alert.id.toString()
            binding.tvState.text = alert.state.name
            binding.tvType.text = alert.type.name
            binding.tvTarget.text = alert.breakdown_info.name
            binding.tvBreakdownCause.text = alert.breakdown_cause
            binding.tvUpdatedAt.text = Time.getHMS(alert.updatedAt)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Alert>() {
        override fun areItemsTheSame(oldItem: Alert, newItem: Alert): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Alert, newItem: Alert): Boolean {
            return oldItem == newItem
        }
    }
}