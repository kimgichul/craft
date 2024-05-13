package com.example.myapplication.Post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import java.text.SimpleDateFormat
import java.util.*

class CommentAdapter(
    private val commentList: List<Comment>
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentItem = commentList[position]

        holder.textViewCommentAuthor.text = currentItem.nickname
        holder.textViewCommentContent.text = currentItem.content
        holder.textViewCommentTime.text = formatTimestamp(currentItem.timestamp.toDate())
    }

    override fun getItemCount() = commentList.size

    private fun formatTimestamp(timestamp: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(timestamp)
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewCommentAuthor: TextView = itemView.findViewById(R.id.comment_item_tv_CommentAuthor)
        val textViewCommentContent: TextView = itemView.findViewById(R.id.comment_item_tv_CommentContent)
        val textViewCommentTime: TextView = itemView.findViewById(R.id.comment_item_tv_CommentTime)
    }
}
