package com.cvsuagritech.spim.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cvsuagritech.spim.R
import com.cvsuagritech.spim.databinding.FragmentMapboxMapBinding
import com.cvsuagritech.spim.models.FarmData
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.generated.circleLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.layers.addLayer

class MapboxMapFragment : Fragment() {

    private var _binding: FragmentMapboxMapBinding? = null
    private val binding get() = _binding!!

    private val farmDataList = mutableListOf<FarmData>()
    private var currentFilter = "crop_health" // Default filter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapboxMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        initializeFarmData()
        setupMapboxMap()
        updateMapMarkers()
        updateLegend()
    }

    private fun setupClickListeners() {
        binding.btnRefresh.setOnClickListener {
            refreshFarmData()
        }

        // Handle back button in toolbar
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        // Filter button click listeners
        binding.btnCropHealth.setOnClickListener {
            currentFilter = "crop_health"
            updateFilterButtons()
            updateMapMarkers()
            updateLegend()
        }

        binding.btnPestDisease.setOnClickListener {
            currentFilter = "pest_disease"
            updateFilterButtons()
            updateMapMarkers()
            updateLegend()
        }

        binding.btnGrowthStage.setOnClickListener {
            currentFilter = "growth_stage"
            updateFilterButtons()
            updateMapMarkers()
            updateLegend()
        }
    }

    private fun updateFilterButtons() {
        // Reset all buttons to default state
        binding.btnCropHealth.backgroundTintList = resources.getColorStateList(R.color.status_success, null)
        binding.btnPestDisease.backgroundTintList = resources.getColorStateList(R.color.status_warning, null)
        binding.btnGrowthStage.backgroundTintList = resources.getColorStateList(R.color.gradient_blue_start, null)

        // Highlight selected button
        when (currentFilter) {
            "crop_health" -> {
                binding.btnCropHealth.backgroundTintList = resources.getColorStateList(R.color.gradient_primary_start, null)
            }
            "pest_disease" -> {
                binding.btnPestDisease.backgroundTintList = resources.getColorStateList(R.color.gradient_accent_start, null)
            }
            "growth_stage" -> {
                binding.btnGrowthStage.backgroundTintList = resources.getColorStateList(R.color.gradient_teal_start, null)
            }
        }
    }

    private fun setupMapboxMap() {
        // Set up Mapbox map
        binding.mapView.mapboxMap.loadStyleUri(Style.MAPBOX_STREETS) { style ->
            // Map loaded successfully
            Toast.makeText(requireContext(), "Map loaded successfully!", Toast.LENGTH_SHORT).show()
        }

        // Center map on Naic Cavite
        binding.mapView.mapboxMap.setCamera(
            com.mapbox.maps.CameraOptions.Builder()
                .center(Point.fromLngLat(120.7667, 14.3167)) // Naic Cavite coordinates
                .zoom(13.0)
                .build()
        )
    }

    private fun initializeFarmData() {
        farmDataList.clear()

        // Farm A Series - Central Naic
        farmDataList.add(FarmData(
            id = "A-1",
            name = "Santos Rice Farm",
            location = "Barangay Santulan, Naic",
            coordinates = "14.3167°N, 120.7667°E",
            status = "Healthy",
            issues = listOf(),
            irrigationStatus = "Adequate",
            lastInspection = "2024-12-15",
            area = "2.5 hectares",
            cropHealth = "Excellent",
            pestDiseaseLevel = "None",
            growthStage = "Reproductive"
        ))

        farmDataList.add(FarmData(
            id = "A-2",
            name = "Cruz Agricultural Land",
            location = "Barangay Santulan, Naic",
            coordinates = "14.3170°N, 120.7670°E",
            status = "Warning",
            issues = listOf("Minor nutrient deficiency"),
            irrigationStatus = "Adequate",
            lastInspection = "2024-12-14",
            area = "1.8 hectares",
            cropHealth = "Good",
            pestDiseaseLevel = "Low",
            growthStage = "Vegetative"
        ))

        farmDataList.add(FarmData(
            id = "A-3",
            name = "Reyes Family Farm",
            location = "Barangay Santulan, Naic",
            coordinates = "14.3173°N, 120.7673°E",
            status = "Critical",
            issues = listOf("Brown Planthopper infestation", "High pest density"),
            irrigationStatus = "Adequate",
            lastInspection = "2024-12-13",
            area = "3.2 hectares",
            cropHealth = "Poor",
            pestDiseaseLevel = "High",
            growthStage = "Maturity"
        ))

        // Add more farms...
        farmDataList.addAll(getAdditionalFarms())
    }

    private fun getAdditionalFarms(): List<FarmData> {
        return listOf(
            FarmData("B-1", "Garcia Rice Plantation", "Barangay Bucana, Naic", "14.3180°N, 120.7800°E", "Healthy", listOf(), "Adequate", "2024-12-15", "4.1 hectares", "Excellent", "None", "Reproductive"),
            FarmData("B-2", "Mendoza Agricultural Area", "Barangay Bucana, Naic", "14.3183°N, 120.7803°E", "Warning", listOf("Soil pH imbalance"), "Adequate", "2024-12-14", "2.7 hectares", "Fair", "Medium", "Vegetative"),
            FarmData("B-3", "Torres Farm Land", "Barangay Bucana, Naic", "14.3186°N, 120.7806°E", "Healthy", listOf(), "Adequate", "2024-12-15", "1.9 hectares", "Good", "Low", "Seedling"),
            FarmData("B-4", "Lopez Rice Fields", "Barangay Bucana, Naic", "14.3189°N, 120.7809°E", "Warning", listOf("Leaf blight detected"), "Adequate", "2024-12-14", "3.5 hectares", "Fair", "Medium", "Reproductive"),
            FarmData("B-5", "Hernandez Agricultural Site", "Barangay Bucana, Naic", "14.3192°N, 120.7812°E", "Healthy", listOf(), "Adequate", "2024-12-15", "2.3 hectares", "Excellent", "None", "Maturity"),
            FarmData("B-6", "Villanueva Farm", "Barangay Bucana, Naic", "14.3195°N, 120.7815°E", "Warning", listOf("Minor fungal infection"), "Adequate", "2024-12-14", "1.6 hectares", "Good", "Low", "Vegetative"),
            FarmData("B-7", "Ramos Agricultural Land", "Barangay Bucana, Naic", "14.3198°N, 120.7818°E", "Critical", listOf("Insufficient irrigation system", "Water stress"), "Inadequate", "2024-12-13", "2.8 hectares", "Poor", "High", "Seedling"),
            FarmData("C-1", "Silva Rice Plantation", "Barangay Labac, Naic", "14.3150°N, 120.7500°E", "Healthy", listOf(), "Adequate", "2024-12-15", "3.8 hectares", "Excellent", "None", "Reproductive"),
            FarmData("C-2", "Morales Farm Land", "Barangay Labac, Naic", "14.3153°N, 120.7503°E", "Warning", listOf("Nutrient deficiency in rice crops", "Nitrogen shortage"), "Adequate", "2024-12-14", "2.4 hectares", "Fair", "Medium", "Vegetative"),
            FarmData("C-3", "Castillo Agricultural Area", "Barangay Labac, Naic", "14.3156°N, 120.7506°E", "Healthy", listOf(), "Adequate", "2024-12-15", "1.7 hectares", "Good", "Low", "Maturity"),
            FarmData("C-4", "Jimenez Rice Fields", "Barangay Labac, Naic", "14.3159°N, 120.7509°E", "Warning", listOf("Rice blast disease detected"), "Adequate", "2024-12-14", "3.1 hectares", "Poor", "High", "Reproductive"),
            FarmData("C-5", "Aquino Farm", "Barangay Labac, Naic", "14.3162°N, 120.7512°E", "Healthy", listOf(), "Adequate", "2024-12-15", "2.6 hectares", "Excellent", "None", "Seedling"),
            FarmData("C-6", "Dela Cruz Agricultural Site", "Barangay Labac, Naic", "14.3165°N, 120.7515°E", "Warning", listOf("Stem borer infestation"), "Adequate", "2024-12-14", "2.9 hectares", "Fair", "Medium", "Vegetative")
        )
    }

    private fun updateMapMarkers() {
        binding.mapView.mapboxMap.loadStyleUri(Style.MAPBOX_STREETS) { style ->
            // Clear existing layers
            try {
                style.removeStyleLayer("farm-markers")
                style.removeStyleLayer("farm-labels")
                style.removeStyleSource("farm-data")
            } catch (e: Exception) {
                // Ignore if layers don't exist
            }

            // Group farms by color for current filter
            val farmsByColor = farmDataList.groupBy { getMarkerColor(it) }

            // Create separate layers for each color
            farmsByColor.forEach { (color, farms) ->
                val features = farms.map { farm ->
                    val coordinates = parseCoordinates(farm.coordinates)
                    com.mapbox.geojson.Feature.fromGeometry(
                        Point.fromLngLat(coordinates.first, coordinates.second)
                    ).apply {
                        addStringProperty("farm_id", farm.id)
                        addStringProperty("farm_name", farm.name)
                    }
                }

                val featureCollection = com.mapbox.geojson.FeatureCollection.fromFeatures(features)
                val sourceId = "farm-data-$color"
                val layerId = "farm-markers-$color"

                // Add source
                style.addSource(geoJsonSource(sourceId) {
                    featureCollection(featureCollection)
                })

                // Add circle layer
                style.addLayer(
                    circleLayer(layerId, sourceId) {
                        circleRadius(10.0)
                        circleColor(color)
                        circleStrokeWidth(2.0)
                        circleStrokeColor("#FFFFFF")
                    }
                )

                // Add text layer
                style.addLayer(symbolLayer("farm-labels-$color", sourceId) {
                    textField("Farm")
                    textSize(8.0)
                    textColor("#FFFFFF")
                    textHaloColor("#000000")
                    textHaloWidth(1.0)
                    textAllowOverlap(true)
                    textIgnorePlacement(true)
                })
            }
        }
    }

    private fun parseCoordinates(coordinates: String): Pair<Double, Double> {
        // Parse "14.3167°N, 120.7667°E" format
        val parts = coordinates.split(", ")
        val lat = parts[0].replace("°N", "").toDouble()
        val lng = parts[1].replace("°E", "").toDouble()
        return Pair(lng, lat) // Note: longitude first for GeoJSON
    }

    private fun getMarkerColor(farm: FarmData): String {
        return when (currentFilter) {
            "crop_health" -> {
                when (farm.cropHealth) {
                    "Excellent" -> "#4CAF50" // Green
                    "Good" -> "#8BC34A" // Light Green
                    "Fair" -> "#FF9800" // Orange
                    "Poor" -> "#F44336" // Red
                    else -> "#9E9E9E" // Gray
                }
            }
            "pest_disease" -> {
                when (farm.pestDiseaseLevel) {
                    "None" -> "#4CAF50" // Green
                    "Low" -> "#8BC34A" // Light Green
                    "Medium" -> "#FF9800" // Orange
                    "High" -> "#F44336" // Red
                    else -> "#9E9E9E" // Gray
                }
            }
            "growth_stage" -> {
                when (farm.growthStage) {
                    "Seedling" -> "#2196F3" // Blue
                    "Vegetative" -> "#4CAF50" // Green
                    "Reproductive" -> "#FF9800" // Orange
                    "Maturity" -> "#9C27B0" // Purple
                    else -> "#9E9E9E" // Gray
                }
            }
            else -> "#9E9E9E" // Gray
        }
    }

    private fun getFilterValue(farm: FarmData): String {
        return when (currentFilter) {
            "crop_health" -> farm.cropHealth
            "pest_disease" -> farm.pestDiseaseLevel
            "growth_stage" -> farm.growthStage
            else -> farm.status
        }
    }

    private fun refreshFarmData() {
        Toast.makeText(requireContext(), "Map data refreshed!", Toast.LENGTH_SHORT).show()
        updateMapMarkers()
        updateLegend()
    }

    private fun updateLegend() {
        binding.legendContent.removeAllViews()
        
        val legendItems = when (currentFilter) {
            "crop_health" -> listOf(
                LegendItem("#4CAF50", "Excellent"),
                LegendItem("#8BC34A", "Good"),
                LegendItem("#FF9800", "Fair"),
                LegendItem("#F44336", "Poor")
            )
            "pest_disease" -> listOf(
                LegendItem("#4CAF50", "None"),
                LegendItem("#8BC34A", "Low"),
                LegendItem("#FF9800", "Medium"),
                LegendItem("#F44336", "High")
            )
            "growth_stage" -> listOf(
                LegendItem("#2196F3", "Seedling"),
                LegendItem("#4CAF50", "Vegetative"),
                LegendItem("#FF9800", "Reproductive"),
                LegendItem("#9C27B0", "Maturity")
            )
            else -> emptyList()
        }

        legendItems.forEach { item ->
            val legendItemView = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_legend, binding.legendContent, false)
            
            val colorView = legendItemView.findViewById<View>(R.id.legend_color)
            val textView = legendItemView.findViewById<TextView>(R.id.legend_text)
            
            colorView.setBackgroundColor(android.graphics.Color.parseColor(item.color))
            textView.text = item.label
            
            binding.legendContent.addView(legendItemView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private data class LegendItem(val color: String, val label: String)
}
