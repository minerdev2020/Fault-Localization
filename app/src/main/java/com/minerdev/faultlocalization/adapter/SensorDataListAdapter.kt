package com.minerdev.faultlocalization.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.databinding.SensorDataItemBinding
import com.minerdev.faultlocalization.model.Sensor

class SensorDataListAdapter(diffCallback: DiffCallback) :
    ListAdapter<Sensor, SensorDataListAdapter.ViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SensorDataItemBinding.inflate(
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

    operator fun get(position: Int): Sensor {
        return getItem(position)
    }

    class ViewHolder(val binding: SensorDataItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sensor: Sensor) {
            binding.tvName.text = sensor.name
            binding.tvState.text = sensor.state.name
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Sensor>() {
        override fun areItemsTheSame(oldItem: Sensor, newItem: Sensor): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sensor, newItem: Sensor): Boolean {
            return oldItem == newItem
        }
    }
}