package com.minerdev.faultlocalization.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.databinding.ItemSensorModifyBinding
import com.minerdev.faultlocalization.model.Sensor
import com.minerdev.faultlocalization.utils.Constants.CREATE
import com.minerdev.faultlocalization.utils.Constants.DELETE
import com.minerdev.faultlocalization.utils.Constants.TAG
import com.minerdev.faultlocalization.utils.Constants.UPDATE

class SensorModifyListAdapter(diffCallback: DiffCallback) :
    ListAdapter<Sensor, SensorModifyListAdapter.ViewHolder>(diffCallback) {
    lateinit var clickListener: OnItemClickListener

    val types = ArrayList<String>()
    val deleteList = ArrayList<Sensor>()
    var parentId = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSensorModifyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        val arrayAdapter =
            ArrayAdapter<String>(parent.context, android.R.layout.simple_spinner_dropdown_item)
        return ViewHolder(binding, arrayAdapter, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, types)
    }

    operator fun get(position: Int): Sensor {
        return getItem(position)
    }

    fun removeItem(position: Int) {
        if (getItem(position).editState == UPDATE) {
            getItem(position).editState = DELETE
            deleteList.add(getItem(position))
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
            add(Sensor(type_id = 1, parent_id = parentId, editState = CREATE))
        }

        submitList(newList)
    }

    interface OnItemClickListener {
        fun onItemClick(viewHolder: ViewHolder, view: View, position: Int)
    }

    class ViewHolder(
        val binding: ItemSensorModifyBinding,
        private val arrayAdapter: ArrayAdapter<String>,
        listener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageBtnDelete.setOnClickListener {
                listener.onItemClick(this@ViewHolder, itemView, bindingAdapterPosition)
            }

            binding.spinnerType.adapter = arrayAdapter
        }

        fun bind(sensor: Sensor, types: List<String>) {
            binding.model = sensor

            arrayAdapter.clear()
            arrayAdapter.addAll(types)
            binding.spinnerType.setSelection(sensor.type_id - 1)
            binding.spinnerType.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        sensor.type_id = p2 + 1
                        Log.d(TAG, "sensor type : ${p2 + 1}")
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        sensor.type_id = 1
                    }
                }

            binding.radioButtonRunning.setOnCheckedChangeListener { _, p1 ->
                if (p1) {
                    sensor.state_id = 1
                }
            }
            binding.radioButtonUnderRepair.setOnCheckedChangeListener { _, p1 ->
                if (p1) {
                    sensor.state_id = 2
                }
            }
            binding.radioButtonStoped.setOnCheckedChangeListener { _, p1 ->
                if (p1) {
                    sensor.state_id = 3
                }
            }
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