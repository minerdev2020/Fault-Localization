package com.minerdev.faultlocalization.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.EquipmentItemBinding
import com.minerdev.faultlocalization.model.Equipment
import java.util.*

class EquipmentListAdapter(diffCallback: DiffCallback) :
    ListAdapter<Equipment, EquipmentListAdapter.ViewHolder>(diffCallback) {
    var listener: OnItemClickListener? = null

    private val selectedItems = SparseBooleanArray()
    private var prePosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EquipmentItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.setVisibility(selectedItems[position])
    }

    operator fun get(position: Int): Equipment {
        return getItem(position)
    }

    interface OnItemClickListener {
        fun onItemClick(viewHolder: ViewHolder?, view: View?, position: Int)
        fun onItemLongClick(
            viewHolder: ViewHolder?, view: View?, position: Int
        )
    }

    inner class ViewHolder(val binding: EquipmentItemBinding, clickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageBtnExpand.setOnClickListener {
                val position = bindingAdapterPosition
                if (selectedItems[position]) {
                    selectedItems.delete(position)

                } else {
                    selectedItems.delete(prePosition)
                    selectedItems.put(position, true)
                }

                if (prePosition != -1 && prePosition != position) {
                    this@EquipmentListAdapter.notifyItemChanged(prePosition)
                }

                this@EquipmentListAdapter.notifyItemChanged(position)
                prePosition = position
            }

            binding.btnDetail.setOnClickListener {
                clickListener?.onItemClick(this@ViewHolder, itemView, bindingAdapterPosition)
            }
        }

        fun bind(equipment: Equipment) {
            binding.tvName.text = equipment.name
            binding.tvState.text = equipment.EquipmentState.name
            binding.ivProfile.setBackgroundResource(R.drawable.ic_launcher_background)
            binding.ivProfile.setImageResource(R.drawable.ic_launcher_foreground)
        }

        fun setVisibility(isExpanded: Boolean) {
            binding.imageBtnExpand.setImageResource(
                if (isExpanded)
                    R.drawable.ic_round_expand_less_24
                else
                    R.drawable.ic_round_expand_more_24
            )
            binding.hiddenLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
            binding.hiddenLayout.requestLayout()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Equipment>() {
        override fun areItemsTheSame(oldItem: Equipment, newItem: Equipment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Equipment, newItem: Equipment): Boolean {
            return oldItem == newItem
        }
    }
}