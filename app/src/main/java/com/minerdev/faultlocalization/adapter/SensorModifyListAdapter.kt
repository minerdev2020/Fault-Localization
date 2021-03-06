package com.minerdev.faultlocalization.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.databinding.FooterItemBinding
import com.minerdev.faultlocalization.databinding.SensorModifyItemBinding
import com.minerdev.faultlocalization.model.Sensor
import com.minerdev.faultlocalization.model.SensorState
import com.minerdev.faultlocalization.model.SensorType
import com.minerdev.faultlocalization.utils.Constants.DELETE
import com.minerdev.faultlocalization.utils.Constants.TYPE_FOOTER
import com.minerdev.faultlocalization.utils.Constants.TYPE_ITEM
import com.minerdev.faultlocalization.utils.Constants.UPDATE
import java.util.*


class SensorModifyListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val addEmptyItem = {
        items.add(Sensor(SensorState = SensorState(), SensorType = SensorType()))
        notifyItemInserted(items.size)
    }

    var items = ArrayList<Sensor>()
    var deletedItems = ArrayList<Sensor>()
    lateinit var listener: (
        viewHolder: ItemViewHolder?,
        view: View?,
        position: Int
    ) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_FOOTER) {
            val binding = FooterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return FooterViewHolder(binding, addEmptyItem)

        } else {
            val binding = SensorModifyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return ItemViewHolder(binding, listener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val item = items[position]
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    fun removeItem(position: Int) {
        if (items[position].state == UPDATE) {
            items[position].state = DELETE
            deletedItems.add(items[position])
        }

        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size)
            TYPE_FOOTER;
        else
            TYPE_ITEM;
    }

    class FooterViewHolder(binding: FooterItemBinding, addListener: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.materialBtnAdd.setOnClickListener {
                addListener()
            }
        }
    }

    class ItemViewHolder(
        val binding: SensorModifyItemBinding,
        listener: (viewHolder: ItemViewHolder?, view: View?, position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageBtnDelete.setOnClickListener {
                listener(this@ItemViewHolder, itemView, bindingAdapterPosition)
            }
        }

        fun bind(sensor: Sensor) {
            binding.etName.setText(sensor.name)
            binding.etModelNumber.setText(sensor.SensorState.name)
            binding.etType.setText(sensor.SensorType.name)
        }
    }
}