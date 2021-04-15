package com.minerdev.faultlocalization.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.databinding.ItemEquipmentBinding
import com.minerdev.faultlocalization.model.LoginLog


class LoginLogListAdapter(diffCallback: DiffCallback) :
    ListAdapter<LoginLog, LoginLogListAdapter.ViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEquipmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    operator fun get(position: Int): LoginLog {
        return getItem(position)
    }

    class ViewHolder(val binding: ItemEquipmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loginLog: LoginLog) {

        }
    }

    class DiffCallback : DiffUtil.ItemCallback<LoginLog>() {
        override fun areItemsTheSame(oldItem: LoginLog, newItem: LoginLog): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LoginLog, newItem: LoginLog): Boolean {
            return oldItem == newItem
        }
    }
}