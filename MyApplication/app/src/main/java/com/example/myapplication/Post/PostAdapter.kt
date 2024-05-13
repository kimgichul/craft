package com.example.myapplication.Post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import java.util.Calendar

class PostAdapter(private val listener: OnPostItemClickListener) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var postList = listOf<Post_Title>() // 포스트 목록을 저장하는 리스트
    private val currentDate = Calendar.getInstance() // 현재 날짜 및 시간 정보

    // 새로운 포스트 목록으로 데이터를 업데이트하는 메서드
    fun updateData(newPostList: List<Post_Title>) {
        postList = newPostList
        notifyDataSetChanged()
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.post_items_tv_title) // 타이틀 텍스트뷰
        val textViewTime: TextView = itemView.findViewById(R.id.post_items_tv_time) // 시간 텍스트뷰
        val textViewTag: TextView = itemView.findViewById(R.id.post_items_tv_tag) // 태그 텍스트뷰
    }

    // 뷰홀더를 생성하는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_items, parent, false)
        return PostViewHolder(itemView)
    }

    // 뷰홀더에 데이터를 바인딩하는 메서드
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = postList[position]
        holder.textViewTitle.text = currentItem.title // 타이틀 설정
        holder.textViewTime.text = formatDate(currentItem.time) // 시간 설정
        holder.textViewTag.text = currentItem.category // 태그 설정

        // 아이템 클릭 리스너 설정
        holder.itemView.setOnClickListener {
            listener.onPostItemClick(currentItem)
        }
    }

    // 아이템 개수를 반환하는 메서드
    override fun getItemCount() = postList.size

    // 타임스탬프를 현재 시간과 비교하여 형식화하는 메서드
    private fun formatDate(timestamp: com.google.firebase.Timestamp): String {
        val postDate = Calendar.getInstance().apply {
            time = timestamp.toDate()
        }

        val diffMinutes = (currentDate.timeInMillis - postDate.timeInMillis) / (60 * 1000)

        return when {
            diffMinutes < 60 -> "$diffMinutes 분 전"
            diffMinutes < 24 * 60 -> "${diffMinutes / 60} 시간 전"
            diffMinutes < 30 * 24 * 60 -> "${diffMinutes / (24 * 60)} 일 전"
            diffMinutes < 365 * 24 * 60 -> "${diffMinutes / (30 * 24 * 60)} 개월 전"
            else -> "${diffMinutes / (365 * 24 * 60)} 년 전"
        }
    }

    // 아이템 클릭 리스너를 정의하는 인터페이스
    interface OnPostItemClickListener {
        fun onPostItemClick(post: Post_Title)
    }
}
