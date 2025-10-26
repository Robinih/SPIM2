package com.cvsuagritech.spim.utils

import com.cvsuagritech.spim.models.CropHealthRecord
import com.cvsuagritech.spim.models.CropHealthStatus
import com.cvsuagritech.spim.models.GrowthStage
import com.cvsuagritech.spim.models.TreatmentRecommendation
import com.cvsuagritech.spim.models.TreatmentType
import com.cvsuagritech.spim.models.SustainabilityLevel
import kotlin.random.Random

object CropHealthSimulator {
    
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
    
    private val barangays = listOf(
        "Barangay 1", "Barangay 2", "Barangay 3", "Barangay 4", "Barangay 5",
        "Poblacion", "San Jose", "Santa Maria", "San Pedro", "San Miguel"
    )
    
    /**
     * Simulate crop health analysis from an image
     */
    fun simulateCropHealthAnalysis(imageBlob: ByteArray?): CropHealthRecord {
        val healthStatus = healthStatuses.random()
        val confidence = generateRealisticConfidence(healthStatus)
        val growthStage = growthStages.random()
        val location = barangays.random()
        
        return CropHealthRecord(
            cropType = "Rice",
            healthStatus = healthStatus,
            confidence = confidence,
            growthStage = growthStage,
            imageBlob = imageBlob,
            location = location,
            timestamp = System.currentTimeMillis(),
            sustainabilityScore = generateSustainabilityScore(healthStatus)
        )
    }
    
    /**
     * Generate realistic confidence scores based on health status
     */
    private fun generateRealisticConfidence(healthStatus: CropHealthStatus): Float {
        return when (healthStatus) {
            CropHealthStatus.HEALTHY -> Random.nextFloat() * 0.15f + 0.80f // 80-95%
            CropHealthStatus.NUTRIENT_DEFICIENCY -> Random.nextFloat() * 0.20f + 0.70f // 70-90%
            CropHealthStatus.PEST_DAMAGE -> Random.nextFloat() * 0.25f + 0.65f // 65-90%
            CropHealthStatus.DISEASE -> Random.nextFloat() * 0.30f + 0.60f // 60-90%
            CropHealthStatus.ENVIRONMENTAL_STRESS -> Random.nextFloat() * 0.25f + 0.65f // 65-90%
        }
    }
    
    /**
     * Generate sustainability score based on health status
     */
    private fun generateSustainabilityScore(healthStatus: CropHealthStatus): Float {
        return when (healthStatus) {
            CropHealthStatus.HEALTHY -> Random.nextFloat() * 0.2f + 0.8f // 80-100%
            CropHealthStatus.NUTRIENT_DEFICIENCY -> Random.nextFloat() * 0.3f + 0.6f // 60-90%
            CropHealthStatus.PEST_DAMAGE -> Random.nextFloat() * 0.4f + 0.4f // 40-80%
            CropHealthStatus.DISEASE -> Random.nextFloat() * 0.5f + 0.3f // 30-80%
            CropHealthStatus.ENVIRONMENTAL_STRESS -> Random.nextFloat() * 0.3f + 0.5f // 50-80%
        }
    }
    
    /**
     * Generate treatment recommendations based on crop health status
     */
    fun generateTreatmentRecommendations(cropHealthRecord: CropHealthRecord): List<TreatmentRecommendation> {
        val recommendations = mutableListOf<TreatmentRecommendation>()
        
        when (cropHealthRecord.healthStatus) {
            CropHealthStatus.HEALTHY -> {
                // Preventive recommendations for healthy crops
                recommendations.add(createRecommendation(
                    cropHealthRecord.id,
                    TreatmentType.PREVENTION,
                    "Maintain Soil Health",
                    "Continue current practices and monitor regularly",
                    "Apply organic compost monthly, maintain proper irrigation, and monitor for early signs of issues.",
                    SustainabilityLevel.HIGH,
                    0.85f,
                    0f,
                    "Monthly"
                ))
            }
            
            CropHealthStatus.NUTRIENT_DEFICIENCY -> {
                // Cultural and biological methods first
                recommendations.add(createRecommendation(
                    cropHealthRecord.id,
                    TreatmentType.CULTURAL,
                    "Soil Amendment",
                    "Improve soil nutrient content naturally",
                    "Apply organic compost and green manure. Test soil pH and adjust if needed.",
                    SustainabilityLevel.HIGH,
                    0.75f,
                    50f,
                    "2-3 weeks"
                ))
                
                recommendations.add(createRecommendation(
                    cropHealthRecord.id,
                    TreatmentType.BIOLOGICAL,
                    "Biofertilizer Application",
                    "Use beneficial microorganisms to improve nutrient uptake",
                    "Apply nitrogen-fixing bacteria and mycorrhizal fungi to enhance root nutrient absorption.",
                    SustainabilityLevel.MEDIUM,
                    0.80f,
                    100f,
                    "1-2 weeks"
                ))
            }
            
            CropHealthStatus.PEST_DAMAGE -> {
                // Biological control first, then cultural
                recommendations.add(createRecommendation(
                    cropHealthRecord.id,
                    TreatmentType.BIOLOGICAL,
                    "Beneficial Insects",
                    "Introduce natural predators to control pest populations",
                    "Release ladybugs, lacewings, or parasitic wasps to control pest populations naturally.",
                    SustainabilityLevel.HIGH,
                    0.70f,
                    80f,
                    "1 week"
                ))
                
                recommendations.add(createRecommendation(
                    cropHealthRecord.id,
                    TreatmentType.CULTURAL,
                    "Crop Rotation & Sanitation",
                    "Remove infected plants and improve field hygiene",
                    "Remove and destroy infected plant material. Implement crop rotation to break pest cycles.",
                    SustainabilityLevel.HIGH,
                    0.65f,
                    20f,
                    "Immediate"
                ))
                
                // Chemical as last resort
                if (cropHealthRecord.confidence > 0.8f) {
                    recommendations.add(createRecommendation(
                        cropHealthRecord.id,
                        TreatmentType.CHEMICAL,
                        "Targeted Pesticide",
                        "Apply specific pesticide for identified pest",
                        "Apply neem oil or pyrethrin-based pesticide. Follow safety guidelines and apply during early morning.",
                        SustainabilityLevel.LOW,
                        0.90f,
                        200f,
                        "3-5 days",
                        "Neem oil, Pyrethrin",
                        "Wear protective equipment. Avoid application during flowering. Follow pre-harvest intervals."
                    ))
                }
            }
            
            CropHealthStatus.DISEASE -> {
                // Cultural methods first
                recommendations.add(createRecommendation(
                    cropHealthRecord.id,
                    TreatmentType.CULTURAL,
                    "Disease Management",
                    "Improve air circulation and reduce humidity",
                    "Prune affected areas, improve spacing between plants, and ensure proper drainage.",
                    SustainabilityLevel.HIGH,
                    0.60f,
                    30f,
                    "Immediate"
                ))
                
                recommendations.add(createRecommendation(
                    cropHealthRecord.id,
                    TreatmentType.BIOLOGICAL,
                    "Biological Fungicide",
                    "Use beneficial microorganisms to combat disease",
                    "Apply Bacillus subtilis or Trichoderma-based biological fungicide to suppress disease-causing pathogens.",
                    SustainabilityLevel.MEDIUM,
                    0.75f,
                    120f,
                    "1 week"
                ))
                
                // Chemical treatment for severe cases
                if (cropHealthRecord.confidence > 0.75f) {
                    recommendations.add(createRecommendation(
                        cropHealthRecord.id,
                        TreatmentType.CHEMICAL,
                        "Fungicide Treatment",
                        "Apply targeted fungicide for disease control",
                        "Apply copper-based or systemic fungicide. Follow label instructions and safety precautions.",
                        SustainabilityLevel.LOW,
                        0.85f,
                        150f,
                        "5-7 days",
                        "Copper hydroxide, Azoxystrobin",
                        "Wear protective equipment. Avoid application during hot weather. Follow re-entry intervals."
                    ))
                }
            }
            
            CropHealthStatus.ENVIRONMENTAL_STRESS -> {
                // Focus on environmental management
                recommendations.add(createRecommendation(
                    cropHealthRecord.id,
                    TreatmentType.CULTURAL,
                    "Environmental Management",
                    "Adjust growing conditions to reduce stress",
                    "Improve irrigation scheduling, provide shade during extreme heat, and ensure proper soil moisture.",
                    SustainabilityLevel.HIGH,
                    0.70f,
                    40f,
                    "Immediate"
                ))
                
                recommendations.add(createRecommendation(
                    cropHealthRecord.id,
                    TreatmentType.BIOLOGICAL,
                    "Stress Relief Treatment",
                    "Apply plant growth regulators and stress-relief compounds",
                    "Apply seaweed extract or humic acid to help plants cope with environmental stress.",
                    SustainabilityLevel.MEDIUM,
                    0.65f,
                    90f,
                    "1-2 weeks"
                ))
            }
        }
        
        return recommendations
    }
    
    private fun createRecommendation(
        recordId: Long,
        type: TreatmentType,
        title: String,
        description: String,
        instructions: String,
        sustainability: SustainabilityLevel,
        effectiveness: Float,
        cost: Float,
        timeToApply: String,
        activeIngredients: String? = null,
        safetyNotes: String? = null
    ): TreatmentRecommendation {
        return TreatmentRecommendation(
            cropHealthRecordId = recordId,
            treatmentType = type,
            title = title,
            description = description,
            instructions = instructions,
            sustainabilityLevel = sustainability,
            effectiveness = effectiveness,
            cost = cost,
            timeToApply = timeToApply,
            activeIngredients = activeIngredients,
            safetyNotes = safetyNotes,
            timestamp = System.currentTimeMillis()
        )
    }
    
    /**
     * Generate random location coordinates (simulated GPS)
     */
    fun generateRandomLocation(): String {
        val lat = Random.nextDouble(14.0, 15.0) // Philippines latitude range
        val lng = Random.nextDouble(120.0, 121.0) // Philippines longitude range
        return "$lat,$lng"
    }
}

