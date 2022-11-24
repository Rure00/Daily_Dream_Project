package com.example.daily_dream.viewBinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.daily_dream.R
import com.example.daily_dream.serverCommunication.serverData.PostData

class RecyclerUserAdapter(private val items: ArrayList<PostData>) : RecyclerView.Adapter<RecyclerUserAdapter.ViewHolder>() {

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
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(inflatedView)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener (onItemClickListener: RecyclerUserAdapter.OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: PostData) {
            val title: TextView = itemView.findViewById(R.id.titleText)
            val content: TextView = itemView.findViewById(R.id.contentText)
            val date: TextView = itemView.findViewById(R.id.dateText)
            val name: TextView = itemView.findViewById(R.id.nameText)

            title.text = item.getTitle()
            content.text = item.getContent()
            //date.text = item.getDate()
            name.text = item.getUserName()

            view.setOnClickListener {
                //Intent(this@ViewHolder, PostActivity::class.java)
            }
        }
    }
}