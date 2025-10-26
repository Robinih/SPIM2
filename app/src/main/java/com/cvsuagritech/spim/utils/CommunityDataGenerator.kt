package com.cvsuagritech.spim.utils

import com.cvsuagritech.spim.models.CommunityPost
import com.cvsuagritech.spim.models.PostCategory
import java.util.*

object CommunityDataGenerator {

    fun generateDummyPosts(): List<CommunityPost> {
        return listOf(
            CommunityPost(
                id = "post_1",
                userId = "user_1",
                username = "Maria Santos",
                title = "Best practices for rice planting season",
                content = "I've been farming rice for over 10 years and wanted to share some tips that have helped me increase my yield significantly. The key is proper soil preparation and timing. Make sure to prepare your fields at least 2 weeks before planting...",
                likes = 24,
                comments = 8,
                shares = 3,
                createdAt = Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000), // 2 hours ago
                category = PostCategory.RICE_FARMING
            ),
            CommunityPost(
                id = "post_2",
                userId = "user_2",
                username = "Juan Dela Cruz",
                title = "Help! Brown spots on rice leaves",
                content = "I noticed brown spots appearing on my rice leaves. Has anyone experienced this before? What could be causing it and how can I treat it? My harvest is in 3 weeks and I'm worried about the quality.",
                likes = 12,
                comments = 15,
                shares = 2,
                createdAt = Date(System.currentTimeMillis() - 4 * 60 * 60 * 1000), // 4 hours ago
                category = PostCategory.QUESTION
            ),
            CommunityPost(
                id = "post_3",
                userId = "user_3",
                username = "Ana Rodriguez",
                title = "Water management tip that saved my crop",
                content = "During the dry season, I discovered that maintaining water depth at 5-10cm during the vegetative stage significantly improved my rice quality. This simple technique increased my yield by 15%!",
                likes = 31,
                comments = 6,
                shares = 8,
                createdAt = Date(System.currentTimeMillis() - 6 * 60 * 60 * 1000), // 6 hours ago
                category = PostCategory.TIP
            ),
            CommunityPost(
                id = "post_4",
                userId = "user_4",
                username = "Carlos Mendoza",
                title = "Record harvest this season! 🎉",
                content = "After implementing the new irrigation system and using organic fertilizers, I achieved my best harvest ever! 4.2 tons per hectare compared to my previous 3.1 tons. Hard work pays off!",
                likes = 45,
                comments = 12,
                shares = 7,
                createdAt = Date(System.currentTimeMillis() - 8 * 60 * 60 * 1000), // 8 hours ago
                category = PostCategory.SUCCESS_STORY
            ),
            CommunityPost(
                id = "post_5",
                userId = "user_5",
                username = "Luis Garcia",
                title = "Pest control without chemicals",
                content = "I've been using neem oil and garlic spray for pest control and it's working wonders! No harmful chemicals, safe for the environment, and effective against common rice pests. Anyone else tried natural pest control methods?",
                likes = 18,
                comments = 9,
                shares = 4,
                createdAt = Date(System.currentTimeMillis() - 12 * 60 * 60 * 1000), // 12 hours ago
                category = PostCategory.PEST_CONTROL
            ),
            CommunityPost(
                id = "post_6",
                userId = "user_6",
                username = "Elena Torres",
                title = "Weather forecast accuracy for farming",
                content = "How reliable are weather forecasts for planning rice farming activities? I've been using the AgriSight weather feature and it's been quite accurate for the past month. What's your experience with weather predictions?",
                likes = 7,
                comments = 11,
                shares = 1,
                createdAt = Date(System.currentTimeMillis() - 18 * 60 * 60 * 1000), // 18 hours ago
                category = PostCategory.WEATHER
            ),
            CommunityPost(
                id = "post_7",
                userId = "user_7",
                username = "Roberto Silva",
                title = "Market prices for rice this season",
                content = "Current market price for palay is ₱18-20 per kilo in our area. How are prices in your region? Planning to sell my harvest next week and want to get the best deal.",
                likes = 14,
                comments = 7,
                shares = 3,
                createdAt = Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000), // 1 day ago
                category = PostCategory.MARKET
            ),
            CommunityPost(
                id = "post_8",
                userId = "user_8",
                username = "Isabel Morales",
                title = "Harvesting equipment recommendations",
                content = "Looking to upgrade my harvesting equipment. Any recommendations for reliable rice harvesters? Budget is around ₱500,000. Need something efficient for 5-hectare farm.",
                likes = 9,
                comments = 13,
                shares = 2,
                createdAt = Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000), // 2 days ago
                category = PostCategory.EQUIPMENT
            ),
            CommunityPost(
                id = "post_9",
                userId = "user_9",
                username = "Fernando Ramos",
                title = "Soil testing results interpretation",
                content = "Got my soil test results back. pH is 6.2, nitrogen is low, phosphorus is adequate, potassium is high. What fertilizer mix should I use for the next planting season?",
                likes = 16,
                comments = 8,
                shares = 1,
                createdAt = Date(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000), // 3 days ago
                category = PostCategory.QUESTION
            ),
            CommunityPost(
                id = "post_10",
                userId = "user_10",
                username = "Carmen Villanueva",
                title = "Organic rice farming journey",
                content = "Started transitioning to organic rice farming 2 years ago. It's challenging but rewarding! Yield dropped initially but quality improved significantly. Now getting premium prices for organic rice. Anyone else on this journey?",
                likes = 28,
                comments = 14,
                shares = 6,
                createdAt = Date(System.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000), // 4 days ago
                category = PostCategory.SUCCESS_STORY
            ),
            CommunityPost(
                id = "post_11",
                userId = "user_11",
                username = "Miguel Herrera",
                title = "Rice variety selection guide",
                content = "Different rice varieties perform differently in various conditions. NSIC Rc 222 works great in our area with good yield and disease resistance. What varieties are you planting this season?",
                likes = 22,
                comments = 10,
                shares = 3,
                createdAt = Date(System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000), // 5 days ago
                category = PostCategory.TIP
            ),
            CommunityPost(
                id = "post_12",
                userId = "user_12",
                username = "Patricia Cruz",
                title = "Post-harvest storage tips",
                content = "Proper storage is crucial to maintain rice quality. Keep moisture content below 14%, use clean containers, and store in cool, dry place. Lost 20% of my harvest last year due to poor storage. Don't make the same mistake!",
                likes = 19,
                comments = 5,
                shares = 4,
                createdAt = Date(System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000), // 6 days ago
                category = PostCategory.TIP
            )
        )
    }
}
