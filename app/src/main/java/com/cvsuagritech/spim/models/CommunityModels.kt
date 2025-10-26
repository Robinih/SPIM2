package com.cvsuagritech.spim.models

import java.util.Date

data class CommunityPost(
    val id: String,
    val userId: String,
    val username: String,
    val userAvatar: String? = null,
    val title: String,
    val content: String,
    val imageUrl: String? = null,
    val likes: Int = 0,
    val comments: Int = 0,
    val shares: Int = 0,
    val createdAt: Date,
    val category: PostCategory = PostCategory.GENERAL,
    var isLiked: Boolean = false
)

data class Comment(
    val id: String,
    val postId: String,
    val userId: String,
    val username: String,
    val userAvatar: String? = null,
    val content: String,
    val likes: Int = 0,
    val replies: Int = 0,
    val createdAt: Date,
    val parentCommentId: String? = null, // For nested replies
    var isLiked: Boolean = false
)

enum class PostCategory {
    GENERAL,
    RICE_FARMING,
    PEST_CONTROL,
    HARVEST,
    EQUIPMENT,
    MARKET,
    WEATHER,
    SUCCESS_STORY,
    QUESTION,
    TIP
}
