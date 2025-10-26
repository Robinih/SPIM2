package com.cvsuagritech.spim.models

data class FarmData(
    val id: String,
    val name: String,
    val location: String,
    val coordinates: String,
    val status: String, // Healthy, Warning, Critical
    val issues: List<String>,
    val irrigationStatus: String,
    val lastInspection: String,
    val area: String,
    val cropHealth: String, // Excellent, Good, Fair, Poor
    val pestDiseaseLevel: String, // None, Low, Medium, High
    val growthStage: String // Seedling, Vegetative, Reproductive, Maturity
)
