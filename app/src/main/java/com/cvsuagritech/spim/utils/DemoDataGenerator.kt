package com.cvsuagritech.spim.utils

import android.content.Context
import android.content.SharedPreferences
import com.cvsuagritech.spim.database.CropHealthDatabaseHelper
import com.cvsuagritech.spim.models.CropHealthRecord
import com.cvsuagritech.spim.models.CropHealthStatus
import com.cvsuagritech.spim.models.GrowthStage
import com.cvsuagritech.spim.models.LGUReport
import com.cvsuagritech.spim.models.ReportType
import com.cvsuagritech.spim.models.RiskLevel
import com.cvsuagritech.spim.models.TreatmentRecommendation
import com.cvsuagritech.spim.models.TreatmentType
import com.cvsuagritech.spim.models.SustainabilityLevel
import kotlin.random.Random

object DemoDataGenerator {
    
    private const val PREFS_NAME = "agrisight_demo_data"
    private const val DEMO_DATA_INITIALIZED_KEY = "demo_data_initialized"
    
    private val barangays = listOf(
        "Barangay 1", "Barangay 2", "Barangay 3", "Barangay 4", "Barangay 5",
        "Poblacion", "San Jose", "Santa Maria", "San Pedro", "San Miguel"
    )
    
    private val healthStatuses = listOf(
        CropHealthStatus.HEALTHY,
        CropHealthStatus.NUTRIENT_DEFICIENCY,
        CropHealthStatus.PEST_DAMAGE,
        CropHealthStatus.DISEASE,
        CropHealthStatus.ENVIRONMENTAL_STRESS
    )
    
    private val growthStages = listOf(
        GrowthStage.SEEDLING,
        GrowthStage.VEGETATIVE,
        GrowthStage.REPRODUCTIVE,
        GrowthStage.MATURITY
    )
    
    fun initializeDemoDataIfNeeded(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isInitialized = prefs.getBoolean(DEMO_DATA_INITIALIZED_KEY, false)
        
        if (!isInitialized) {
            generateDemoData(context)
            prefs.edit().putBoolean(DEMO_DATA_INITIALIZED_KEY, true).apply()
        }
    }
    
    private fun generateDemoData(context: Context) {
        val databaseHelper = CropHealthDatabaseHelper(context)
        
        // Generate 20 sample crop health records
        val cropHealthRecords = generateSampleCropHealthRecords()
        
        // Insert crop health records and get their IDs
        val recordIds = mutableListOf<Long>()
        cropHealthRecords.forEach { record ->
            val recordId = databaseHelper.insertCropHealthRecord(record)
            recordIds.add(recordId)
        }
        
        // Generate treatment recommendations for each record
        recordIds.forEach { recordId ->
            val record = cropHealthRecords[recordIds.indexOf(recordId)].copy(id = recordId)
            val recommendations = CropHealthSimulator.generateTreatmentRecommendations(record)
            recommendations.forEach { recommendation ->
                databaseHelper.insertTreatmentRecommendation(recommendation)
            }
        }
        
        // Generate 3 sample LGU reports
        val lguReports = generateSampleLGUReports()
        lguReports.forEach { report ->
            databaseHelper.insertLGUReport(report)
        }
    }
    
    private fun generateSampleCropHealthRecords(): List<CropHealthRecord> {
        val records = mutableListOf<CropHealthRecord>()
        val currentTime = System.currentTimeMillis()
        
        // Generate records with varied timestamps (last 30 days)
        repeat(20) { index ->
            val daysAgo = Random.nextInt(0, 30)
            val timestamp = currentTime - (daysAgo * 24 * 60 * 60 * 1000L)
            
            val healthStatus = healthStatuses.random()
            val confidence = generateRealisticConfidence(healthStatus)
            val growthStage = growthStages.random()
            val location = barangays.random()
            
            val record = CropHealthRecord(
                cropType = "Rice",
                healthStatus = healthStatus,
                confidence = confidence,
                growthStage = growthStage,
                location = location,
                timestamp = timestamp,
                notes = generateSampleNotes(healthStatus),
                sustainabilityScore = generateSustainabilityScore(healthStatus)
            )
            
            records.add(record)
        }
        
        return records.sortedByDescending { it.timestamp }
    }
    
    private fun generateRealisticConfidence(healthStatus: CropHealthStatus): Float {
        return when (healthStatus) {
            CropHealthStatus.HEALTHY -> Random.nextFloat() * 0.15f + 0.80f // 80-95%
            CropHealthStatus.NUTRIENT_DEFICIENCY -> Random.nextFloat() * 0.20f + 0.70f // 70-90%
            CropHealthStatus.PEST_DAMAGE -> Random.nextFloat() * 0.25f + 0.65f // 65-90%
            CropHealthStatus.DISEASE -> Random.nextFloat() * 0.30f + 0.60f // 60-90%
            CropHealthStatus.ENVIRONMENTAL_STRESS -> Random.nextFloat() * 0.25f + 0.65f // 65-90%
        }
    }
    
    private fun generateSustainabilityScore(healthStatus: CropHealthStatus): Float {
        return when (healthStatus) {
            CropHealthStatus.HEALTHY -> Random.nextFloat() * 0.2f + 0.8f // 80-100%
            CropHealthStatus.NUTRIENT_DEFICIENCY -> Random.nextFloat() * 0.3f + 0.6f // 60-90%
            CropHealthStatus.PEST_DAMAGE -> Random.nextFloat() * 0.4f + 0.4f // 40-80%
            CropHealthStatus.DISEASE -> Random.nextFloat() * 0.5f + 0.3f // 30-80%
            CropHealthStatus.ENVIRONMENTAL_STRESS -> Random.nextFloat() * 0.3f + 0.5f // 50-80%
        }
    }
    
    private fun generateSampleNotes(healthStatus: CropHealthStatus): String {
        return when (healthStatus) {
            CropHealthStatus.HEALTHY -> "Crop appears to be in excellent condition with no visible issues."
            CropHealthStatus.NUTRIENT_DEFICIENCY -> "Yellowing leaves observed, possible nitrogen deficiency."
            CropHealthStatus.PEST_DAMAGE -> "Visible pest damage on leaves, holes and discoloration present."
            CropHealthStatus.DISEASE -> "Fungal infection detected, brown spots on leaves."
            CropHealthStatus.ENVIRONMENTAL_STRESS -> "Signs of stress due to weather conditions."
        }
    }
    
    private fun generateSampleLGUReports(): List<LGUReport> {
        val reports = mutableListOf<LGUReport>()
        val currentTime = System.currentTimeMillis()
        
        // Generate 3 reports for different barangays
        val reportBarangays = barangays.take(3)
        
        reportBarangays.forEachIndexed { index, barangay ->
            val daysAgo = index * 7 // Weekly reports
            val timestamp = currentTime - (daysAgo * 24 * 60 * 60 * 1000L)
            
            val totalRecords = Random.nextInt(15, 35)
            val healthyCrops = Random.nextInt((totalRecords * 0.6).toInt(), (totalRecords * 0.9).toInt())
            val remaining = totalRecords - healthyCrops
            
            val nutrientDeficiency = Random.nextInt(0, remaining / 2)
            val pestDamage = Random.nextInt(0, remaining / 2)
            val disease = Random.nextInt(0, remaining / 3)
            val environmentalStress = remaining - nutrientDeficiency - pestDamage - disease
            
            val healthPercentage = (healthyCrops.toFloat() / totalRecords) * 100
            val riskLevel = when {
                healthPercentage >= 80f -> RiskLevel.LOW
                healthPercentage >= 60f -> RiskLevel.MEDIUM
                healthPercentage >= 40f -> RiskLevel.HIGH
                else -> RiskLevel.CRITICAL
            }
            
            val interventionNeeded = riskLevel == RiskLevel.HIGH || riskLevel == RiskLevel.CRITICAL
            
            val report = LGUReport(
                reportType = ReportType.WEEKLY,
                barangayName = barangay,
                totalRecords = totalRecords,
                healthyCrops = healthyCrops,
                nutrientDeficiency = nutrientDeficiency,
                pestDamage = pestDamage,
                disease = disease,
                environmentalStress = environmentalStress,
                riskLevel = riskLevel,
                interventionNeeded = interventionNeeded,
                recommendations = generateSampleRecommendations(riskLevel, nutrientDeficiency, pestDamage, disease),
                generatedBy = "AgriSight System",
                generatedDate = timestamp
            )
            
            reports.add(report)
        }
        
        return reports.sortedByDescending { it.generatedDate }
    }
    
    private fun generateSampleRecommendations(
        riskLevel: RiskLevel,
        nutrientDeficiency: Int,
        pestDamage: Int,
        disease: Int
    ): String {
        val recommendations = mutableListOf<String>()
        
        when (riskLevel) {
            RiskLevel.LOW -> {
                recommendations.add("Continue current agricultural practices")
                recommendations.add("Maintain regular monitoring schedule")
            }
            RiskLevel.MEDIUM -> {
                recommendations.add("Increase monitoring frequency")
                recommendations.add("Consider preventive measures")
            }
            RiskLevel.HIGH -> {
                recommendations.add("Immediate intervention required")
                recommendations.add("Coordinate with agricultural extension services")
            }
            RiskLevel.CRITICAL -> {
                recommendations.add("URGENT: Deploy emergency response team")
                recommendations.add("Coordinate with regional agricultural office")
                recommendations.add("Implement immediate treatment protocols")
            }
        }
        
        if (nutrientDeficiency > 0) {
            recommendations.add("Address soil nutrient deficiencies in $nutrientDeficiency areas")
        }
        if (pestDamage > 0) {
            recommendations.add("Implement pest control measures in $pestDamage affected areas")
        }
        if (disease > 0) {
            recommendations.add("Apply disease management protocols in $disease affected areas")
        }
        
        return recommendations.joinToString("\n• ", "• ")
    }
    
    fun resetDemoData(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(DEMO_DATA_INITIALIZED_KEY, false).apply()
        
        // Clear existing data
        val databaseHelper = CropHealthDatabaseHelper(context)
        databaseHelper.deleteAllCropHealthRecords()
        
        // Regenerate demo data
        generateDemoData(context)
    }
}




