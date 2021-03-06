package com.minerdev.faultlocalization.adapter

import android.service.quicksettings.Tile
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.databinding.SensorModifyItemBinding
import com.minerdev.faultlocalization.model.Sensor
import com.minerdev.faultlocalization.model.SensorState
import com.minerdev.faultlocalization.model.SensorType
import com.minerdev.faultlocalization.utils.Constants.CREATE
import com.minerdev.faultlocalization.utils.Constants.DELETE
import com.minerdev.faultlocalization.utils.Constants.UPDATE


class SensorModifyListAdapter(diffCallback: DiffCallback) :
    ListAdapter<Sensor, SensorModifyListAdapter.ViewHolder>(diffCallback) {
    var deletedItems = ArrayList<Sensor>()

    lateinit var listener: (
        viewHolder: ViewHolder?,
        view: View?,
        position: Int
    ) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SensorModifyItemBinding.inflate(
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

    operator fun get(position: Int): Sensor {
        return getItem(position)
    }

    fun removeItem(position: Int) {
        if (getItem(position).state == UPDATE) {
            getItem(position).state = DELETE
            deletedItems.add(getItem(position))
        }

        val newList = mutableListOf<Sensor>().apply {
            addAll(currentList)
            removeAt(position)
        }

        submitList(newList)
    }

    fun addEmptyItem() {
        val newList = mutableListOf<Sensor>().apply {
            addAll(currentList)
            add(Sensor(SensorState = SensorState(), SensorType = SensorType(), state = CREATE))
        }

        submitList(newList)
    }

    class ViewHolder(
        val binding: SensorModifyItemBinding,
        listener: (viewHolder: ViewHolder?, view: View?, position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageBtnDelete.setOnClickListener {
                listener(this@ViewHolder, itemView, bindingAdapterPosition)
            }
        }

        fun bind(sensor: Sensor) {
            binding.etName.setText(sensor.name)
            binding.etModelNumber.setText(sensor.SensorState.name)
            binding.etType.setText(sensor.SensorType.name)
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