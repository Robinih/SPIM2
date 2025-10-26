package com.cvsuagritech.spim.models

import java.text.SimpleDateFormat
import java.util.*

// Enum for treatment types
enum class TreatmentType {
    CULTURAL,
    BIOLOGICAL,
    CHEMICAL,
    PREVENTION
}

// Enum for sustainability levels
enum class SustainabilityLevel {
    HIGH,      // Organic, cultural methods
    MEDIUM,    // Biological control
    LOW,       // Minimal chemical use
    CHEMICAL   // Chemical treatment required
}

data class TreatmentRecommendation(
    val id: Long = 0,
    val cropHealthRecordId: Long,
    val treatmentType: TreatmentType,
    val title: String,
    val description: String,
    val instructions: String,
    val sustainabilityLevel: SustainabilityLevel,
    val effectiveness: Float, // 0-1 scale
    val cost: Float, // Estimated cost
    val timeToApply: String, // e.g., "2-3 days", "Weekly"
    val activeIngredients: String? = null, // For chemical treatments
    val safetyNotes: String? = null,
    val isApplied: Boolean = false,
    val appliedDate: Long? = null,
    val results: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) {
    
    // Helper function to get formatted application date
    fun getFormattedAppliedDate(): String? {
        return appliedDate?.let {
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            sdf.format(Date(it))
        }
    }
    
    // Helper function to get treatment type display name
    fun getTreatmentTypeDisplayName(): String {
        return when (treatmentType) {
            TreatmentType.CULTURAL -> "Cultural Methods"
            TreatmentType.BIOLOGICAL -> "Biological Control"
            TreatmentType.CHEMICAL -> "Chemical Treatment"
            TreatmentType.PREVENTION -> "Prevention"
        }
    }
    
    // Helper function to get sustainability level display name
    fun getSustainabilityLevelDisplayName(): String {
        return when (sustainabilityLevel) {
            SustainabilityLevel.HIGH -> "High Sustainability"
            SustainabilityLevel.MEDIUM -> "Medium Sustainability"
            SustainabilityLevel.LOW -> "Low Sustainability"
            SustainabilityLevel.CHEMICAL -> "Chemical Treatment"
        }
    }
    
    // Helper function to get effectiveness percentage
    fun getEffectivenessPercentage(): Int {
        return (effectiveness * 100).toInt()
    }
    
    // Helper function to get cost display
    fun getCostDisplay(): String {
        return when {
            cost == 0f -> "Free"
            cost < 100f -> "Low Cost"
            cost < 500f -> "Medium Cost"
            else -> "High Cost"
        }
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TreatmentRecommendation

        if (id != other.id) return false
        if (cropHealthRecordId != other.cropHealthRecordId) return false
        if (treatmentType != other.treatmentType) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (instructions != other.instructions) return false
        if (sustainabilityLevel != other.sustainabilityLevel) return false
        if (effectiveness != other.effectiveness) return false
        if (cost != other.cost) return false
        if (timeToApply != other.timeToApply) return false
        if (activeIngredients != other.activeIngredients) return false
        if (safetyNotes != other.safetyNotes) return false
        if (isApplied != other.isApplied) return false
        if (appliedDate != other.appliedDate) return false
        if (results != other.results) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + cropHealthRecordId.hashCode()
        result = 31 * result + treatmentType.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + instructions.hashCode()
        result = 31 * result + sustainabilityLevel.hashCode()
        result = 31 * result + effectiveness.hashCode()
        result = 31 * result + cost.hashCode()
        result = 31 * result + timeToApply.hashCode()
        result = 31 * result + (activeIngredients?.hashCode() ?: 0)
        result = 31 * result + (safetyNotes?.hashCode() ?: 0)
        result = 31 * result + isApplied.hashCode()
        result = 31 * result + (appliedDate?.hashCode() ?: 0)
        result = 31 * result + (results?.hashCode() ?: 0)
        result = 31 * result + timestamp.hashCode()
        return result
    }
}

