package com.minerdev.faultlocalization.adapter

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.EquipmentItemBinding
import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.utils.Constants.TYPE_ID


class EquipmentListAdapter(private val context: Context, diffCallback: DiffCallback) :
    ListAdapter<Equipment, EquipmentListAdapter.ViewHolder>(diffCallback) {
    private val selectedItems = SparseBooleanArray()
    private val expandListener = { position: Int ->
        if (selectedItems[position]) {
            selectedItems.delete(position)

        } else {
            selectedItems.delete(prePosition)
            selectedItems.put(position, true)
        }

        if (prePosition != -1 && prePosition != position) {
            this.notifyItemChanged(prePosition)
        }

        this.notifyItemChanged(position)
        prePosition = position
    }

    lateinit var listener: OnItemClickListener
    private var prePosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EquipmentItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding, listener, context, expandListener)
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
        fun onDetailButtonClick(viewHolder: ViewHolder?, view: View?, position: Int)
        fun onModifyButtonClick(viewHolder: ViewHolder?, view: View?, position: Int)
        fun onItemLongClick(viewHolder: ViewHolder?, view: View?, position: Int)
    }

    class ViewHolder(
        val binding: EquipmentItemBinding,
        clickListener: OnItemClickListener?,
        context: Context,
        expandListener: (position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private val adapter by lazy { SensorListAdapter(SensorListAdapter.DiffCallback()) }

        init {
            binding.imageBtnExpand.setOnClickListener {
                expandListener(bindingAdapterPosition)
            }

            binding.btnDetail.setOnClickListener {
                clickListener?.onDetailButtonClick(
                    this@ViewHolder,
                    itemView,
                    bindingAdapterPosition
                )
            }

            if (TYPE_ID == "2") {
                binding.btnModify.text = "发送维修申请"
            }

            binding.btnModify.setOnClickListener {
                clickListener?.onModifyButtonClick(
                    this@ViewHolder,
                    itemView,
                    bindingAdapterPosition
                )
            }

            binding.shownLayout.setOnLongClickListener {
                clickListener?.onItemLongClick(this@ViewHolder, itemView, bindingAdapterPosition)
                true
            }

            val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.recyclerView.layoutManager = manager
            binding.recyclerView.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )

            binding.recyclerView.adapter = adapter
        }

        fun bind(equipment: Equipment) {
            binding.tvName.text = equipment.name
            binding.tvState.text = equipment.EquipmentState.name
            binding.ivProfile.setBackgroundResource(R.drawable.ic_launcher_background)
            binding.ivProfile.setImageResource(R.drawable.ic_launcher_foreground)
            adapter.submitList(equipment.Sensors)
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