package com.cvsuagritech.spim.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cvsuagritech.spim.R
import com.cvsuagritech.spim.models.CommunityPost
import com.cvsuagritech.spim.models.PostCategory
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(
    private val onPostClick: (CommunityPost) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var posts = listOf<CommunityPost>()

    fun updatePosts(newPosts: List<CommunityPost>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userAvatar: ImageView = itemView.findViewById(R.id.user_avatar)
        private val username: TextView = itemView.findViewById(R.id.tv_username)
        private val postTime: TextView = itemView.findViewById(R.id.tv_post_time)
        private val category: TextView = itemView.findViewById(R.id.tv_category)
        private val postTitle: TextView = itemView.findViewById(R.id.tv_post_title)
        private val postContent: TextView = itemView.findViewById(R.id.tv_post_content)
        private val postImage: ImageView = itemView.findViewById(R.id.post_image)
        private val likeIcon: ImageView = itemView.findViewById(R.id.ic_like)
        private val likesCount: TextView = itemView.findViewById(R.id.tv_likes)
        private val commentsCount: TextView = itemView.findViewById(R.id.tv_comments)
        private val sharesCount: TextView = itemView.findViewById(R.id.tv_shares)
        private val likeLayout: View = itemView.findViewById(R.id.layout_like)
        private val commentLayout: View = itemView.findViewById(R.id.layout_comment)
        private val shareLayout: View = itemView.findViewById(R.id.layout_share)

        fun bind(post: CommunityPost) {
            username.text = post.username
            postTime.text = formatTimeAgo(post.createdAt)
            postTitle.text = post.title
            postContent.text = post.content
            likesCount.text = post.likes.toString()
            commentsCount.text = post.comments.toString()
            sharesCount.text = post.shares.toString()

            // Set category
            category.text = getCategoryEmoji(post.category)
            
            // Set like state
            if (post.isLiked) {
                likeIcon.setImageResource(R.drawable.ic_favorite)
                likeIcon.setColorFilter(itemView.context.getColor(R.color.status_error))
            } else {
                likeIcon.setImageResource(R.drawable.ic_favorite_border)
                likeIcon.setColorFilter(itemView.context.getColor(R.color.text_modern_secondary))
            }

            // Set user avatar based on category
            userAvatar.setImageResource(getAvatarForCategory(post.category))

            // Show/hide post image
            if (post.imageUrl != null) {
                postImage.visibility = View.VISIBLE
                // In a real app, you would load the image here
                postImage.setImageResource(R.drawable.ic_crop_health)
            } else {
                postImage.visibility = View.GONE
            }

            // Set click listeners
            likeLayout.setOnClickListener {
                // Toggle like state
                Toast.makeText(itemView.context, "Liked post!", Toast.LENGTH_SHORT).show()
            }

            commentLayout.setOnClickListener {
                Toast.makeText(itemView.context, "View comments", Toast.LENGTH_SHORT).show()
            }

            shareLayout.setOnClickListener {
                Toast.makeText(itemView.context, "Share post", Toast.LENGTH_SHORT).show()
            }

            itemView.setOnClickListener {
                onPostClick(post)
            }
        }

        private fun getCategoryEmoji(category: PostCategory): String {
            return when (category) {
                PostCategory.RICE_FARMING -> "🌾 Rice Farming"
                PostCategory.QUESTION -> "❓ Question"
                PostCategory.TIP -> "💡 Tip"
                PostCategory.SUCCESS_STORY -> "🏆 Success Story"
                PostCategory.PEST_CONTROL -> "🐛 Pest Control"
                PostCategory.HARVEST -> "🌾 Harvest"
                PostCategory.EQUIPMENT -> "🔧 Equipment"
                PostCategory.MARKET -> "📊 Market"
                PostCategory.WEATHER -> "🌤️ Weather"
                PostCategory.GENERAL -> "📝 General"
            }
        }

        private fun getAvatarForCategory(category: PostCategory): Int {
            return when (category) {
                PostCategory.RICE_FARMING -> R.drawable.ic_crop_health
                PostCategory.QUESTION -> R.drawable.ic_fertilizer
                PostCategory.TIP -> R.drawable.ic_crop_health
                PostCategory.SUCCESS_STORY -> R.drawable.ic_fertilizer
                else -> R.drawable.ic_crop_health
            }
        }

        private fun formatTimeAgo(date: Date): String {
            val now = Date()
            val diff = now.time - date.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            return when {
                days > 0 -> "${days}d ago"
                hours > 0 -> "${hours}h ago"
                minutes > 0 -> "${minutes}m ago"
                else -> "Just now"
            }
        }
    }
}
