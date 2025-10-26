package com.cvsuagritech.spim.models

import java.util.Date

data class Quest(
    val id: String,
    val title: String,
    val description: String,
    val type: QuestType,
    val points: Int,
    val isCompleted: Boolean = false,
    val completedDate: Date? = null,
    val isDaily: Boolean = true,
    val targetCount: Int = 1,
    val currentCount: Int = 0
)

enum class QuestType {
    // Daily Activities
    LOGIN_DAILY,
    
    // Rice Planting Activities
    ANALYZE_RICE_CROP,
    CHECK_SOIL_CONDITIONS,
    MONITOR_WATER_LEVEL,
    APPLY_FERTILIZER,
    CONTROL_PESTS,
    HARVEST_RICE,
    
    // Farm Management
    VIEW_FARM_MAP,
    COMPLETE_TREATMENT_RECOMMENDATION,
    GENERATE_LGU_REPORT,
    UPDATE_FARM_LOG,
    
    // Learning & Knowledge
    READ_FARMING_TIPS,
    WATCH_TUTORIAL,
    SHARE_EXPERIENCE
}

data class UserProfile(
    val userId: String,
    val username: String,
    val totalPoints: Int = 0,
    val level: Int = 1,
    val experience: Int = 0,
    val lastLoginDate: Date? = null,
    val streakDays: Int = 0,
    val completedQuests: List<String> = emptyList()
)

data class Reward(
    val id: String,
    val name: String,
    val description: String,
    val pointsCost: Int,
    val type: RewardType,
    val isUnlocked: Boolean = false
)

enum class RewardType {
    // Visual Customization
    AVATAR_FRAME,
    THEME_COLOR,
    BADGE,
    
    // Rice Farming Tools & Features
    ADVANCED_ANALYTICS,
    PREMIUM_SEEDS_CATALOG,
    WEATHER_FORECAST,
    SOIL_TESTING_TOOL,
    PEST_IDENTIFICATION_PRO,
    HARVEST_CALCULATOR,
    
    // Learning Resources
    FARMING_GUIDE,
    VIDEO_TUTORIALS,
    EXPERT_CONSULTATION,
    
    // Community Features
    FARMER_NETWORK_ACCESS,
    KNOWLEDGE_SHARING_PLATFORM
}

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val category: AchievementCategory,
    val pointsReward: Int,
    val isUnlocked: Boolean = false,
    val unlockedDate: Date? = null,
    val iconResId: String? = null
)

enum class AchievementCategory {
    RICE_PLANTING,
    PEST_CONTROL,
    HARVEST_SUCCESS,
    KNOWLEDGE_GAIN,
    COMMUNITY_CONTRIBUTION,
    CONSISTENCY
}
