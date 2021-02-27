package com.minerdev.faultlocalization.custom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MessageListAdapter : RecyclerView.Adapter<com.zhengzr.fl.MessageListAdapter.ViewHolder>() {
    private var items: ArrayList<Message> = ArrayList<Message>()
    private var listener: OnMessageItemClickListener? = null
    private var itemView: View? = null
    fun setOnItemClickListener(clickListener: OnMessageItemClickListener?) {
        listener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        itemView = inflater.inflate(R.layout.message_item, parent, false)
        return ViewHolder(itemView!!, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Message = items[position]
        holder.setItem(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: Message) {
        items.add(item)
    }

    fun setItems(items: ArrayList<Message>) {
        this.items = items
    }

    fun getItem(position: Int): Message {
        return items[position]
    }

    fun setItem(position: Int, item: Message) {
        items[position] = item
    }

    class ViewHolder(itemView: View, clickListener: OnMessageItemClickListener?) :
        RecyclerView.ViewHolder(itemView) {
        private val textView_from: TextView
        private val textView_state: TextView
        private val textView_type: TextView
        private val textView_contents: TextView
        fun setItem(message: Message) {
            textView_from.setText(message.getFromWho())
            textView_state.setText(message.getState().getName())
            textView_type.setText(message.getType().getName())
            textView_contents.setText(message.getContents())
        }

        init {
            textView_from = itemView.findViewById(R.id.messageItem_from)
            textView_state = itemView.findViewById(R.id.messageItem_state)
            textView_type = itemView.findViewById(R.id.messageItem_type)
            textView_contents = itemView.findViewById(R.id.messageItem_contents)
            val button = itemView.findViewById<ImageButton>(R.id.messageItem_imageButton)
            button.setOnClickListener {
                if (clickListener != null) {
                    clickListener.onItemClick(this@ViewHolder, itemView, adapterPosition)
                }
            }
        }
    }
}