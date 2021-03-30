package com.minerdev.faultlocalization.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.databinding.ItemSensorBinding
import com.minerdev.faultlocalization.model.Sensor

class SensorListAdapter(diffCallback: DiffCallback) :
    ListAdapter<Sensor, SensorListAdapter.ViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSensorBinding.inflate(
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

    class ViewHolder(val binding: ItemSensorBinding) :
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