package com.minerdev.faultlocalization.custom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class PersonListAdapter : RecyclerView.Adapter<com.zhengzr.fl.PersonListAdapter.ViewHolder>() {
    private var items: ArrayList<Person> = ArrayList<Person>()
    private var listener: OnPersonItemClickListener? = null
    private var itemView: View? = null
    fun setOnItemClickListener(clickListener: OnPersonItemClickListener?) {
        listener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        itemView = inflater.inflate(R.layout.person_item, parent, false)
        return ViewHolder(itemView!!, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Person = items[position]
        holder.setItem(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: Person) {
        items.add(item)
    }

    fun setItems(items: ArrayList<Person>) {
        this.items = items
    }

    fun getItem(position: Int): Person {
        return items[position]
    }

    fun setItem(position: Int, item: Person) {
        items[position] = item
    }

    class ViewHolder(itemView: View, clickListener: OnPersonItemClickListener?) :
        RecyclerView.ViewHolder(itemView) {
        private val textView_name: TextView
        private val textView_state: TextView
        private val textView_type: TextView
        private val textView_phone: TextView
        private val imageView_profile: ImageView
        fun setItem(person: Person) {
            textView_name.setText(person.getName())
            textView_state.setText(person.getState().getName())
            textView_type.setText(person.getType().getName())
            textView_phone.setText(person.getPhone())
            imageView_profile.setBackgroundResource(R.drawable.ic_launcher_background)
            imageView_profile.setImageResource(R.drawable.ic_launcher_foreground)
        }

        init {
            textView_name = itemView.findViewById(R.id.personItem_name)
            textView_state = itemView.findViewById(R.id.personItem_state)
            textView_type = itemView.findViewById(R.id.personItem_type)
            textView_phone = itemView.findViewById(R.id.personItem_phone)
            imageView_profile = itemView.findViewById(R.id.personItem_profile_image)
            val linearLayout = itemView.findViewById<LinearLayout>(R.id.personItem_layout)
            linearLayout.setOnLongClickListener {
                if (clickListener != null) {
                    clickListener.onItemLongClick(this@ViewHolder, itemView, adapterPosition)
                }
                true
            }
            val button = itemView.findViewById<ImageButton>(R.id.personItem_imageButton)
            button.setOnClickListener {
                if (clickListener != null) {
                    clickListener.onItemClick(this@ViewHolder, itemView, adapterPosition)
                }
            }
        }
    }
}