package com.cvsuagritech.spim.utils

import android.content.Context
import com.cvsuagritech.spim.database.GamificationDatabaseHelper
import com.cvsuagritech.spim.models.*
import java.util.*

class GamificationManager(private val context: Context) {
    
    private val dbHelper = GamificationDatabaseHelper(context)
    
    fun initializeUser(userId: String, username: String) {
        val profile = dbHelper.getUserProfile()
        if (profile == null) {
            dbHelper.createUserProfile(userId, username)
            generateDailyQuests()
        } else {
            dbHelper.updateUserLogin()
        }
    }
    
    fun getUserProfile(): UserProfile? {
        return dbHelper.getUserProfile()
    }
    
    fun getDailyQuests(): List<Quest> {
        return dbHelper.getDailyQuests()
    }
    
    fun completeQuest(questId: String) {
        dbHelper.completeQuest(questId)
    }
    
    fun getRewards(): List<Reward> {
        return dbHelper.getRewards()
    }
    
    fun unlockReward(rewardId: String): Boolean {
        return dbHelper.unlockReward(rewardId)
    }
    
    fun addPoints(points: Int) {
        dbHelper.addPoints(points)
    }
    
    private fun generateDailyQuests() {
        val quests = listOf(
            // Daily Activities
            Quest(
                id = "daily_login",
                title = "🌅 Morning Check-in",
                description = "Start your rice farming day with AgriSight",
                type = QuestType.LOGIN_DAILY,
                points = 10,
                isDaily = true
            ),
            
            // Rice Planting Activities
            Quest(
                id = "analyze_rice_crop",
                title = "🌾 Rice Crop Analysis",
                description = "Analyze 3 rice crop images for health assessment",
                type = QuestType.ANALYZE_RICE_CROP,
                points = 30,
                isDaily = true,
                targetCount = 3
            ),
            Quest(
                id = "check_soil_conditions",
                title = "🌱 Soil Health Check",
                description = "Monitor soil conditions for optimal rice growth",
                type = QuestType.CHECK_SOIL_CONDITIONS,
                points = 25,
                isDaily = true
            ),
            Quest(
                id = "monitor_water_level",
                title = "💧 Water Management",
                description = "Check and adjust water levels in rice fields",
                type = QuestType.MONITOR_WATER_LEVEL,
                points = 20,
                isDaily = true
            ),
            Quest(
                id = "apply_fertilizer",
                title = "🌿 Fertilizer Application",
                description = "Apply appropriate fertilizer to rice plants",
                type = QuestType.APPLY_FERTILIZER,
                points = 35,
                isDaily = true
            ),
            Quest(
                id = "control_pests",
                title = "🐛 Pest Control",
                description = "Identify and control rice pests",
                type = QuestType.CONTROL_PESTS,
                points = 40,
                isDaily = true
            ),
            
            // Farm Management
            Quest(
                id = "view_farm_map",
                title = "🗺️ Farm Map Review",
                description = "Review your rice farm layout and conditions",
                type = QuestType.VIEW_FARM_MAP,
                points = 15,
                isDaily = true
            ),
            Quest(
                id = "complete_treatment",
                title = "💊 Treatment Application",
                description = "Complete 2 rice treatment recommendations",
                type = QuestType.COMPLETE_TREATMENT_RECOMMENDATION,
                points = 45,
                isDaily = true,
                targetCount = 2
            ),
            Quest(
                id = "update_farm_log",
                title = "📝 Farm Log Update",
                description = "Update your rice farming activities log",
                type = QuestType.UPDATE_FARM_LOG,
                points = 20,
                isDaily = true
            ),
            
            // Learning & Knowledge
            Quest(
                id = "read_farming_tips",
                title = "📚 Learn Rice Farming",
                description = "Read rice farming tips and best practices",
                type = QuestType.READ_FARMING_TIPS,
                points = 15,
                isDaily = true
            ),
            Quest(
                id = "share_experience",
                title = "🤝 Share Knowledge",
                description = "Share your rice farming experience with community",
                type = QuestType.SHARE_EXPERIENCE,
                points = 25,
                isDaily = true
            ),
            
            // Additional Dummy Quests
            Quest(
                id = "harvest_preparation",
                title = "🌾 Harvest Preparation",
                description = "Prepare tools and equipment for rice harvest",
                type = QuestType.HARVEST_RICE,
                points = 50,
                isDaily = true
            ),
            Quest(
                id = "weather_check",
                title = "🌤️ Weather Monitoring",
                description = "Check weather forecast for rice farming decisions",
                type = QuestType.READ_FARMING_TIPS,
                points = 15,
                isDaily = true
            ),
            Quest(
                id = "seed_selection",
                title = "🌱 Seed Selection",
                description = "Choose the best rice seed variety for planting",
                type = QuestType.CHECK_SOIL_CONDITIONS,
                points = 30,
                isDaily = true
            ),
            Quest(
                id = "irrigation_setup",
                title = "💧 Irrigation Setup",
                description = "Set up irrigation system for rice fields",
                type = QuestType.MONITOR_WATER_LEVEL,
                points = 40,
                isDaily = true
            ),
            Quest(
                id = "pest_monitoring",
                title = "🔍 Pest Monitoring",
                description = "Monitor rice fields for pest activity",
                type = QuestType.CONTROL_PESTS,
                points = 25,
                isDaily = true
            ),
            Quest(
                id = "soil_testing",
                title = "🧪 Soil Testing",
                description = "Test soil pH and nutrient levels",
                type = QuestType.CHECK_SOIL_CONDITIONS,
                points = 35,
                isDaily = true
            ),
            Quest(
                id = "crop_rotation",
                title = "🔄 Crop Rotation",
                description = "Plan crop rotation for sustainable farming",
                type = QuestType.READ_FARMING_TIPS,
                points = 20,
                isDaily = true
            ),
            Quest(
                id = "equipment_maintenance",
                title = "🔧 Equipment Maintenance",
                description = "Maintain farming equipment and tools",
                type = QuestType.APPLY_FERTILIZER,
                points = 30,
                isDaily = true
            ),
            Quest(
                id = "market_research",
                title = "📊 Market Research",
                description = "Research rice market prices and trends",
                type = QuestType.SHARE_EXPERIENCE,
                points = 25,
                isDaily = true
            )
        )
        
        // Insert quests into database
        val db = dbHelper.writableDatabase
        quests.forEach { quest ->
            val values = android.content.ContentValues().apply {
                put("quest_id", quest.id)
                put("title", quest.title)
                put("description", quest.description)
                put("type", quest.type.name)
                put("points", quest.points)
                put("is_daily", if (quest.isDaily) 1 else 0)
                put("target_count", quest.targetCount)
                put("current_count", quest.currentCount)
            }
            db.insert("quests", null, values)
        }
    }
    
    fun checkQuestCompletion(action: QuestType) {
        val quests = getDailyQuests()
        val relevantQuest = quests.find { it.type == action }
        
        if (relevantQuest != null && !relevantQuest.isCompleted) {
            // For now, just complete the quest
            // In a real implementation, you'd track progress
            completeQuest(relevantQuest.id)
        }
    }
}
