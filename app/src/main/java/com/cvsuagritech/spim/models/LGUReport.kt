package com.cvsuagritech.spim.models

import java.text.SimpleDateFormat
import java.util.*

// Enum for report types
enum class ReportType {
    DAILY,
    WEEKLY,
    MONTHLY,
    SEASONAL,
    EMERGENCY
}

// Enum for risk levels
enum class RiskLevel {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

data class LGUReport(
    val id: Long = 0,
    val reportType: ReportType,
    val barangayName: String,
    val totalRecords: Int,
    val healthyCrops: Int,
    val nutrientDeficiency: Int,
    val pestDamage: Int,
    val disease: Int,
    val environmentalStress: Int,
    val riskLevel: RiskLevel,
    val interventionNeeded: Boolean,
    val recommendations: String,
    val generatedBy: String, // LGU officer name
    val generatedDate: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val syncDate: Long? = null
) {
    
    // Helper function to get formatted generation date
    fun getFormattedGeneratedDate(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault())
        return sdf.format(Date(generatedDate))
    }
    
    // Helper function to get formatted sync date
    fun getFormattedSyncDate(): String? {
        return syncDate?.let {
            val sdf = SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault())
            sdf.format(Date(it))
        }
    }
    
    // Helper function to get health percentage
    fun getHealthPercentage(): Int {
        return if (totalRecords > 0) {
            ((healthyCrops.toFloat() / totalRecords) * 100).toInt()
        } else 0
    }
    
    // Helper function to get risk level display name
    fun getRiskLevelDisplayName(): String {
        return when (riskLevel) {
            RiskLevel.LOW -> "Low Risk"
            RiskLevel.MEDIUM -> "Medium Risk"
            RiskLevel.HIGH -> "High Risk"
            RiskLevel.CRITICAL -> "Critical Risk"
        }
    }
    
    // Helper function to get report type display name
    fun getReportTypeDisplayName(): String {
        return when (reportType) {
            ReportType.DAILY -> "Daily Report"
            ReportType.WEEKLY -> "Weekly Report"
            ReportType.MONTHLY -> "Monthly Report"
            ReportType.SEASONAL -> "Seasonal Report"
            ReportType.EMERGENCY -> "Emergency Report"
        }
    }
    
    // Helper function to get summary statistics
    fun getSummaryStatistics(): String {
        return buildString {
            appendLine("Total Records: $totalRecords")
            appendLine("Healthy Crops: $healthyCrops (${getHealthPercentage()}%)")
            appendLine("Issues Detected:")
            if (nutrientDeficiency > 0) appendLine("  • Nutrient Deficiency: $nutrientDeficiency")
            if (pestDamage > 0) appendLine("  • Pest Damage: $pestDamage")
            if (disease > 0) appendLine("  • Disease: $disease")
            if (environmentalStress > 0) appendLine("  • Environmental Stress: $environmentalStress")
            appendLine("Risk Level: ${getRiskLevelDisplayName()}")
            if (interventionNeeded) {
                appendLine("⚠️ Intervention Required")
            }
        }
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LGUReport

        if (id != other.id) return false
        if (reportType != other.reportType) return false
        if (barangayName != other.barangayName) return false
        if (totalRecords != other.totalRecords) return false
        if (healthyCrops != other.healthyCrops) return false
        if (nutrientDeficiency != other.nutrientDeficiency) return false
        if (pestDamage != other.pestDamage) return false
        if (disease != other.disease) return false
        if (environmentalStress != other.environmentalStress) return false
        if (riskLevel != other.riskLevel) return false
        if (interventionNeeded != other.interventionNeeded) return false
        if (recommendations != other.recommendations) return false
        if (generatedBy != other.generatedBy) return false
        if (generatedDate != other.generatedDate) return false
        if (isSynced != other.isSynced) return false
        if (syncDate != other.syncDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + reportType.hashCode()
        result = 31 * result + barangayName.hashCode()
        result = 31 * result + totalRecords
        result = 31 * result + healthyCrops
        result = 31 * result + nutrientDeficiency
        result = 31 * result + pestDamage
        result = 31 * result + disease
        result = 31 * result + environmentalStress
        result = 31 * result + riskLevel.hashCode()
        result = 31 * result + interventionNeeded.hashCode()
        result = 31 * result + recommendations.hashCode()
        result = 31 * result + generatedBy.hashCode()
        result = 31 * result + generatedDate.hashCode()
        result = 31 * result + isSynced.hashCode()
        result = 31 * result + (syncDate?.hashCode() ?: 0)
        return result
    }
}

