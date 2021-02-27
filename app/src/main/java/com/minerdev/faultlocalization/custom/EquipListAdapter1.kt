package com.minerdev.faultlocalization.custom

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.model.Equipment
import java.util.*

class EquipListAdapter : RecyclerView.Adapter<com.zhengzr.fl.EquipListAdapter.ViewHolder>() {
    private val selectedItems = SparseBooleanArray()
    private var prePosition = -1
    private var items: ArrayList<Equipment> = ArrayList<Equipment>()
    private var listener: OnEquipItemClickListener? = null
    private var itemView: View? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        itemView = inflater.inflate(R.layout.equip_item, parent, false)
        return ViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Equipment = items[position]
        holder.setItem(item)
        holder.setVisibility(selectedItems[position])
    }

    fun setOnItemClickListener(clickListener: OnEquipItemClickListener?) {
        listener = clickListener
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: Equipment) {
        items.add(item)
    }

    fun setItems(items: ArrayList<Equipment>) {
        this.items = items
    }

    fun getItem(position: Int): Equipment {
        return items[position]
    }

    fun setItem(position: Int, item: Equipment) {
        items[position] = item
    }

    inner class ViewHolder(itemView: View, clickListener: OnEquipItemClickListener?) :
        RecyclerView.ViewHolder(itemView) {
        private val textView_name: TextView
        private val textView_state: TextView
        private val imageView_profile: ImageView
        private val hidden: LinearLayout
        fun setItem(equipment: Equipment) {
            textView_name.setText(equipment.getName())
            textView_state.setText(equipment.getState().getName())
            imageView_profile.setBackgroundResource(R.drawable.ic_launcher_background)
            imageView_profile.setImageResource(R.drawable.ic_launcher_foreground)
        }

        private fun setVisibility(isExpanded: Boolean) {
            val button = itemView.findViewById<ImageButton>(R.id.equipItem_imageButton)
            button.setImageResource(if (isExpanded) R.drawable.round_expand_less_24 else R.drawable.round_expand_more_24)
            hidden.visibility = if (isExpanded) View.VISIBLE else View.GONE
            hidden.requestLayout()
        }

        init {
            textView_name = itemView.findViewById(R.id.equipItem_name)
            textView_state = itemView.findViewById(R.id.equipItem_state)
            imageView_profile = itemView.findViewById(R.id.equipItem_image)
            hidden = itemView.findViewById(R.id.equipItem_hidden_layout)
            val button = itemView.findViewById<ImageButton>(R.id.equipItem_imageButton)
            button.setOnClickListener {
                val position = adapterPosition
                if (selectedItems[position]) {
                    selectedItems.delete(position)
                } else {
                    selectedItems.delete(prePosition)
                    selectedItems.put(position, true)
                }
                if (prePosition != -1 && prePosition != position) {
                    this@EquipListAdapter.notifyItemChanged(prePosition)
                }
                this@EquipListAdapter.notifyItemChanged(position)
                prePosition = position
            }
            val button_detail = itemView.findViewById<Button>(R.id.equipItem_button_detail)
            button_detail.setOnClickListener {
                if (clickListener != null) {
                    clickListener.onItemClick(this@ViewHolder, itemView, adapterPosition)
                }
            }
        }
    }
}