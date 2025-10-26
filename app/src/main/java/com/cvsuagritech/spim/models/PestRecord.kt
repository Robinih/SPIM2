package com.cvsuagritech.spim.models

import java.text.SimpleDateFormat
import java.util.*

// Enum for crop health status
enum class CropHealthStatus {
    HEALTHY,
    NUTRIENT_DEFICIENCY,
    PEST_DAMAGE,
    DISEASE,
    ENVIRONMENTAL_STRESS
}

// Enum for growth stages
enum class GrowthStage {
    SEEDLING,
    VEGETATIVE,
    REPRODUCTIVE,
    MATURITY
}

data class CropHealthRecord(
    val id: Long = 0,
    val cropType: String = "Rice", // Default to rice, expandable to other crops
    val healthStatus: CropHealthStatus,
    val confidence: Float,
    val growthStage: GrowthStage,
    val imagePath: String? = null,
    val imageBlob: ByteArray? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val location: String? = null, // GPS coordinates or location name
    val notes: String? = null,
    val treatmentApplied: String? = null,
    val sustainabilityScore: Float = 0.0f // 0-1 scale for treatment sustainability
) {
    // Helper function to get formatted date and time
    fun getFormattedDateTime(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy • h:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    // Helper function to get formatted date only
    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    // Helper function to get formatted time only
    fun getFormattedTime(): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    // Helper function to get confidence percentage
    fun getConfidencePercentage(): Int {
        return (confidence * 100).toInt()
    }

    // Helper function to get confidence level description
    fun getConfidenceLevel(): String {
        return when {
            confidence >= 0.8f -> "High"
            confidence >= 0.6f -> "Medium"
            confidence >= 0.4f -> "Low"
            else -> "Very Low"
        }
    }

    // Helper function to check if record has image
    fun hasImage(): Boolean {
        return !imagePath.isNullOrEmpty() || imageBlob != null
    }

    // Helper function to get health status display name
    fun getHealthStatusDisplayName(): String {
        return when (healthStatus) {
            CropHealthStatus.HEALTHY -> "Healthy"
            CropHealthStatus.NUTRIENT_DEFICIENCY -> "Nutrient Deficiency"
            CropHealthStatus.PEST_DAMAGE -> "Pest Damage"
            CropHealthStatus.DISEASE -> "Disease"
            CropHealthStatus.ENVIRONMENTAL_STRESS -> "Environmental Stress"
        }
    }

    // Helper function to get growth stage display name
    fun getGrowthStageDisplayName(): String {
        return when (growthStage) {
            GrowthStage.SEEDLING -> "Seedling"
            GrowthStage.VEGETATIVE -> "Vegetative"
            GrowthStage.REPRODUCTIVE -> "Reproductive"
            GrowthStage.MATURITY -> "Maturity"
        }
    }

    // Helper function to get sustainability level
    fun getSustainabilityLevel(): String {
        return when {
            sustainabilityScore >= 0.8f -> "High Sustainability"
            sustainabilityScore >= 0.6f -> "Medium Sustainability"
            sustainabilityScore >= 0.4f -> "Low Sustainability"
            else -> "Chemical Treatment"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CropHealthRecord

        if (id != other.id) return false
        if (cropType != other.cropType) return false
        if (healthStatus != other.healthStatus) return false
        if (confidence != other.confidence) return false
        if (growthStage != other.growthStage) return false
        if (imagePath != other.imagePath) return false
        if (!imageBlob.contentEquals(other.imageBlob)) return false
        if (timestamp != other.timestamp) return false
        if (location != other.location) return false
        if (notes != other.notes) return false
        if (treatmentApplied != other.treatmentApplied) return false
        if (sustainabilityScore != other.sustainabilityScore) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + cropType.hashCode()
        result = 31 * result + healthStatus.hashCode()
        result = 31 * result + confidence.hashCode()
        result = 31 * result + growthStage.hashCode()
        result = 31 * result + (imagePath?.hashCode() ?: 0)
        result = 31 * result + (imageBlob?.contentHashCode() ?: 0)
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + (notes?.hashCode() ?: 0)
        result = 31 * result + (treatmentApplied?.hashCode() ?: 0)
        result = 31 * result + sustainabilityScore.hashCode()
        return result
    }
}
