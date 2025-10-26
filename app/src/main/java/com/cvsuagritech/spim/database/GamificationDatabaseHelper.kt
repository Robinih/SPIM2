package com.cvsuagritech.spim.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.cvsuagritech.spim.models.*
import java.text.SimpleDateFormat
import java.util.*

class GamificationDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "gamification.db", null, 2) {

    companion object {
        private const val TABLE_USER_PROFILE = "user_profile"
        private const val TABLE_QUESTS = "quests"
        private const val TABLE_REWARDS = "rewards"
        private const val TABLE_QUEST_PROGRESS = "quest_progress"
        
        private const val COLUMN_ID = "id"
        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_TOTAL_POINTS = "total_points"
        private const val COLUMN_LEVEL = "level"
        private const val COLUMN_EXPERIENCE = "experience"
        private const val COLUMN_LAST_LOGIN = "last_login"
        private const val COLUMN_STREAK_DAYS = "streak_days"
        
        private const val COLUMN_QUEST_ID = "quest_id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_POINTS = "points"
        private const val COLUMN_IS_COMPLETED = "is_completed"
        private const val COLUMN_COMPLETED_DATE = "completed_date"
        private const val COLUMN_IS_DAILY = "is_daily"
        private const val COLUMN_TARGET_COUNT = "target_count"
        private const val COLUMN_CURRENT_COUNT = "current_count"
        
        private const val COLUMN_REWARD_ID = "reward_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_POINTS_COST = "points_cost"
        private const val COLUMN_REWARD_TYPE = "reward_type"
        private const val COLUMN_IS_UNLOCKED = "is_unlocked"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // User Profile table
        db.execSQL("""
            CREATE TABLE $TABLE_USER_PROFILE (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_USER_ID TEXT UNIQUE,
                $COLUMN_USERNAME TEXT,
                $COLUMN_TOTAL_POINTS INTEGER DEFAULT 0,
                $COLUMN_LEVEL INTEGER DEFAULT 1,
                $COLUMN_EXPERIENCE INTEGER DEFAULT 0,
                $COLUMN_LAST_LOGIN TEXT,
                $COLUMN_STREAK_DAYS INTEGER DEFAULT 0
            )
        """)

        // Quests table
        db.execSQL("""
            CREATE TABLE $TABLE_QUESTS (
                $COLUMN_QUEST_ID TEXT PRIMARY KEY,
                $COLUMN_TITLE TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_TYPE TEXT,
                $COLUMN_POINTS INTEGER,
                $COLUMN_IS_COMPLETED INTEGER DEFAULT 0,
                $COLUMN_COMPLETED_DATE TEXT,
                $COLUMN_IS_DAILY INTEGER DEFAULT 1,
                $COLUMN_TARGET_COUNT INTEGER DEFAULT 1,
                $COLUMN_CURRENT_COUNT INTEGER DEFAULT 0
            )
        """)

        // Rewards table
        db.execSQL("""
            CREATE TABLE $TABLE_REWARDS (
                $COLUMN_REWARD_ID TEXT PRIMARY KEY,
                $COLUMN_NAME TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_POINTS_COST INTEGER,
                $COLUMN_REWARD_TYPE TEXT,
                $COLUMN_IS_UNLOCKED INTEGER DEFAULT 0
            )
        """)

        // Insert default rewards
        insertDefaultRewards(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Clear old quest data to avoid enum conflicts
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUESTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REWARDS")
        
        // Recreate tables with new quest types
        db.execSQL("""
            CREATE TABLE $TABLE_QUESTS (
                $COLUMN_QUEST_ID TEXT PRIMARY KEY,
                $COLUMN_TITLE TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_TYPE TEXT,
                $COLUMN_POINTS INTEGER,
                $COLUMN_IS_COMPLETED INTEGER DEFAULT 0,
                $COLUMN_COMPLETED_DATE TEXT,
                $COLUMN_IS_DAILY INTEGER DEFAULT 1,
                $COLUMN_TARGET_COUNT INTEGER DEFAULT 1,
                $COLUMN_CURRENT_COUNT INTEGER DEFAULT 0
            )
        """)
        
        db.execSQL("""
            CREATE TABLE $TABLE_REWARDS (
                $COLUMN_REWARD_ID TEXT PRIMARY KEY,
                $COLUMN_NAME TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_POINTS_COST INTEGER,
                $COLUMN_REWARD_TYPE TEXT,
                $COLUMN_IS_UNLOCKED INTEGER DEFAULT 0
            )
        """)
        
        insertDefaultRewards(db)
    }

    private fun insertDefaultRewards(db: SQLiteDatabase) {
        val rewards = listOf(
            // Rice Farming Supplies
            Reward("r1", "Organic Insecticide", "Natural pest control for rice fields", 80, RewardType.PEST_IDENTIFICATION_PRO),
            Reward("r2", "NPK Fertilizer", "Balanced nutrients for rice growth", 120, RewardType.ADVANCED_ANALYTICS),
            Reward("r3", "Rice Seeds Premium", "High-yield rice seed variety", 150, RewardType.PREMIUM_SEEDS_CATALOG),
            Reward("r4", "Soil pH Tester", "Test soil acidity for optimal rice growth", 100, RewardType.SOIL_TESTING_TOOL),
            Reward("r5", "Water Level Gauge", "Monitor irrigation water levels", 90, RewardType.WEATHER_FORECAST),
            
            // Farming Tools
            Reward("r6", "Rice Planting Guide", "Complete rice cultivation manual", 60, RewardType.FARMING_GUIDE),
            Reward("r7", "Pest Control Spray", "Effective pest management solution", 110, RewardType.PEST_IDENTIFICATION_PRO),
            Reward("r8", "Fertilizer Spreader", "Even fertilizer distribution tool", 130, RewardType.ADVANCED_ANALYTICS),
            Reward("r9", "Harvest Timer", "Optimal harvest timing calculator", 140, RewardType.HARVEST_CALCULATOR),
            Reward("r10", "Rice Disease Guide", "Identify and treat rice diseases", 70, RewardType.FARMING_GUIDE),
            
            // Premium Features
            Reward("r11", "Expert Consultation", "One-on-one farming expert advice", 200, RewardType.EXPERT_CONSULTATION),
            Reward("r12", "Weather Forecast Pro", "Extended weather predictions", 160, RewardType.WEATHER_FORECAST),
            Reward("r13", "Farmer Network Access", "Connect with local rice farmers", 50, RewardType.FARMER_NETWORK_ACCESS),
            Reward("r14", "Golden Rice Badge", "Exclusive rice farming achievement", 100, RewardType.BADGE)
        )

        rewards.forEach { reward ->
            val values = ContentValues().apply {
                put(COLUMN_REWARD_ID, reward.id)
                put(COLUMN_NAME, reward.name)
                put(COLUMN_DESCRIPTION, reward.description)
                put(COLUMN_POINTS_COST, reward.pointsCost)
                put(COLUMN_REWARD_TYPE, reward.type.name)
                put(COLUMN_IS_UNLOCKED, if (reward.isUnlocked) 1 else 0)
            }
            db.insert(TABLE_REWARDS, null, values)
        }
    }

    fun getUserProfile(): UserProfile? {
        val db = readableDatabase
        val cursor = db.query(TABLE_USER_PROFILE, null, null, null, null, null, null)
        
        return if (cursor.moveToFirst()) {
            val profile = UserProfile(
                userId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                totalPoints = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_POINTS)),
                level = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)),
                experience = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXPERIENCE)),
                lastLoginDate = parseDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_LOGIN))),
                streakDays = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STREAK_DAYS))
            )
            cursor.close()
            profile
        } else {
            cursor.close()
            null
        }
    }

    fun createUserProfile(userId: String, username: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, UUID.randomUUID().toString())
            put(COLUMN_USER_ID, userId)
            put(COLUMN_USERNAME, username)
            put(COLUMN_TOTAL_POINTS, 0)
            put(COLUMN_LEVEL, 1)
            put(COLUMN_EXPERIENCE, 0)
            put(COLUMN_STREAK_DAYS, 0)
        }
        db.insert(TABLE_USER_PROFILE, null, values)
    }

    fun updateUserLogin() {
        val db = writableDatabase
        val profile = getUserProfile() ?: return
        
        val today = Date()
        val lastLogin = profile.lastLoginDate
        val streakDays = if (lastLogin != null && isSameDay(lastLogin, today)) {
            profile.streakDays
        } else {
            profile.streakDays + 1
        }

        val values = ContentValues().apply {
            put(COLUMN_LAST_LOGIN, formatDate(today))
            put(COLUMN_STREAK_DAYS, streakDays)
        }
        
        db.update(TABLE_USER_PROFILE, values, "$COLUMN_USER_ID = ?", arrayOf(profile.userId))
    }

    fun addPoints(points: Int) {
        val db = writableDatabase
        val profile = getUserProfile() ?: return
        
        val newTotalPoints = profile.totalPoints + points
        val newExperience = profile.experience + points
        val newLevel = calculateLevel(newExperience)

        val values = ContentValues().apply {
            put(COLUMN_TOTAL_POINTS, newTotalPoints)
            put(COLUMN_EXPERIENCE, newExperience)
            put(COLUMN_LEVEL, newLevel)
        }
        
        db.update(TABLE_USER_PROFILE, values, "$COLUMN_USER_ID = ?", arrayOf(profile.userId))
    }

    fun getDailyQuests(): List<Quest> {
        val db = readableDatabase
        val cursor = db.query(TABLE_QUESTS, null, "$COLUMN_IS_DAILY = 1", null, null, null, null)
        
        val quests = mutableListOf<Quest>()
        while (cursor.moveToNext()) {
            quests.add(createQuestFromCursor(cursor))
        }
        cursor.close()
        return quests
    }

    fun completeQuest(questId: String) {
        val db = writableDatabase
        val quest = getQuestById(questId) ?: return
        
        val values = ContentValues().apply {
            put(COLUMN_IS_COMPLETED, 1)
            put(COLUMN_COMPLETED_DATE, formatDate(Date()))
        }
        
        db.update(TABLE_QUESTS, values, "$COLUMN_QUEST_ID = ?", arrayOf(questId))
        
        // Add points
        addPoints(quest.points)
    }

    fun getRewards(): List<Reward> {
        val db = readableDatabase
        val cursor = db.query(TABLE_REWARDS, null, null, null, null, null, null)
        
        val rewards = mutableListOf<Reward>()
        while (cursor.moveToNext()) {
            rewards.add(createRewardFromCursor(cursor))
        }
        cursor.close()
        return rewards
    }

    fun unlockReward(rewardId: String): Boolean {
        val db = writableDatabase
        val reward = getRewardById(rewardId) ?: return false
        val profile = getUserProfile() ?: return false
        
        if (profile.totalPoints >= reward.pointsCost) {
            val values = ContentValues().apply {
                put(COLUMN_IS_UNLOCKED, 1)
            }
            db.update(TABLE_REWARDS, values, "$COLUMN_REWARD_ID = ?", arrayOf(rewardId))
            
            // Deduct points
            val newPoints = profile.totalPoints - reward.pointsCost
            val pointsValues = ContentValues().apply {
                put(COLUMN_TOTAL_POINTS, newPoints)
            }
            db.update(TABLE_USER_PROFILE, pointsValues, "$COLUMN_USER_ID = ?", arrayOf(profile.userId))
            return true
        }
        return false
    }

    private fun getQuestById(questId: String): Quest? {
        val db = readableDatabase
        val cursor = db.query(TABLE_QUESTS, null, "$COLUMN_QUEST_ID = ?", arrayOf(questId), null, null, null)
        
        return if (cursor.moveToFirst()) {
            val quest = createQuestFromCursor(cursor)
            cursor.close()
            quest
        } else {
            cursor.close()
            null
        }
    }

    private fun getRewardById(rewardId: String): Reward? {
        val db = readableDatabase
        val cursor = db.query(TABLE_REWARDS, null, "$COLUMN_REWARD_ID = ?", arrayOf(rewardId), null, null, null)
        
        return if (cursor.moveToFirst()) {
            val reward = createRewardFromCursor(cursor)
            cursor.close()
            reward
        } else {
            cursor.close()
            null
        }
    }

    private fun createQuestFromCursor(cursor: Cursor): Quest {
        val questTypeString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))
        
        // Handle old quest types by mapping them to new ones
        val questType = when (questTypeString) {
            "ANALYZE_CROP" -> QuestType.ANALYZE_RICE_CROP
            "VIEW_MAP" -> QuestType.VIEW_FARM_MAP
            "COMPLETE_RECOMMENDATION" -> QuestType.COMPLETE_TREATMENT_RECOMMENDATION
            "GENERATE_REPORT" -> QuestType.GENERATE_LGU_REPORT
            else -> try {
                QuestType.valueOf(questTypeString)
            } catch (e: IllegalArgumentException) {
                QuestType.LOGIN_DAILY // Default fallback
            }
        }
        
        return Quest(
            id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUEST_ID)),
            title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
            description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
            type = questType,
            points = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POINTS)),
            isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1,
            completedDate = parseDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED_DATE))),
            isDaily = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_DAILY)) == 1,
            targetCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TARGET_COUNT)),
            currentCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_COUNT))
        )
    }

    private fun createRewardFromCursor(cursor: Cursor): Reward {
        return Reward(
            id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REWARD_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
            description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
            pointsCost = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POINTS_COST)),
            type = RewardType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REWARD_TYPE))),
            isUnlocked = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_UNLOCKED)) == 1
        )
    }

    private fun calculateLevel(experience: Int): Int {
        return (experience / 100) + 1
    }

    private fun formatDate(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date)
    }

    private fun parseDate(dateString: String?): Date? {
        return if (dateString != null) {
            try {
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateString)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}
