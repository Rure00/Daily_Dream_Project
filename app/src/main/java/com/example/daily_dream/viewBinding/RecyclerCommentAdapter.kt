package com.example.daily_dream.viewBinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.daily_dream.R
import com.example.daily_dream.serverCommunication.serverData.CommentData
import com.example.daily_dream.serverCommunication.serverData.PostData

class RecyclerCommentAdapter(private val items: ArrayList<CommentData>) : RecyclerView.Adapter<RecyclerCommentAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]
        val listener = View.OnClickListener {
            //Toast
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }

        // clickEvent 구현 : https://mechacat.tistory.com/7
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(inflatedView)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener (onItemClickListener: RecyclerCommentAdapter.OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: CommentData) {
            val content: TextView = itemView.findViewById(R.id.commentText)
            val date: TextView = itemView.findViewById(R.id.dateText)
            val name: TextView = itemView.findViewById(R.id.nameText)

            content.text = item.getContent()
            name.text = item.getUserName()
            //date.text = item.getDate()
        }
    }
}