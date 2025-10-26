package com.cvsuagritech.spim.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cvsuagritech.spim.R
import com.cvsuagritech.spim.database.CropHealthDatabaseHelper
import com.cvsuagritech.spim.models.CropHealthRecord

class LogbookAdapter(
    private val cropHealthRecords: List<CropHealthRecord>,
    private val onItemClick: (CropHealthRecord) -> Unit
) : RecyclerView.Adapter<LogbookAdapter.LogbookViewHolder>() {

    private lateinit var databaseHelper: CropHealthDatabaseHelper

    class LogbookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCropImage: ImageView = itemView.findViewById(R.id.iv_crop_image)
        val tvCropType: TextView = itemView.findViewById(R.id.tv_crop_type)
        val tvHealthStatus: TextView = itemView.findViewById(R.id.tv_health_status)
        val tvGrowthStage: TextView = itemView.findViewById(R.id.tv_growth_stage)
        val tvConfidence: TextView = itemView.findViewById(R.id.tv_confidence)
        val tvDateTime: TextView = itemView.findViewById(R.id.tv_date_time)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_location)
        val tvSustainability: TextView = itemView.findViewById(R.id.tv_sustainability)
        val btnViewDetails: View = itemView.findViewById(R.id.btn_view_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogbookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_logbook, parent, false)
        return LogbookViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogbookViewHolder, position: Int) {
        val cropHealthRecord = cropHealthRecords[position]
        
        // Set crop type
        holder.tvCropType.text = cropHealthRecord.cropType
        
        // Set health status with enhanced background
        holder.tvHealthStatus.text = cropHealthRecord.getHealthStatusDisplayName()
        holder.tvHealthStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        holder.tvHealthStatus.background = getHealthStatusBackground(holder.itemView.context, cropHealthRecord.healthStatus)
        
        // Set growth stage
        holder.tvGrowthStage.text = cropHealthRecord.getGrowthStageDisplayName()
        
        // Set confidence
        holder.tvConfidence.text = "${cropHealthRecord.getConfidencePercentage()}%"
        
        // Set date and time separately
        holder.tvDateTime.text = cropHealthRecord.getFormattedDate()
        holder.tvTime.text = cropHealthRecord.getFormattedTime()
        
        // Set location
        holder.tvLocation.text = cropHealthRecord.location ?: "Unknown Location"
        
        // Set sustainability
        holder.tvSustainability.text = cropHealthRecord.getSustainabilityLevel().replace(" Sustainability", "")
        holder.tvSustainability.setTextColor(getSustainabilityColor(holder.itemView.context, cropHealthRecord.sustainabilityScore))
        
        // Set image
        if (cropHealthRecord.imageBlob != null) {
            val bitmap = BitmapFactory.decodeByteArray(cropHealthRecord.imageBlob, 0, cropHealthRecord.imageBlob.size)
            if (bitmap != null) {
                holder.ivCropImage.setImageBitmap(bitmap)
            } else {
                holder.ivCropImage.setImageResource(R.drawable.place_holder)
            }
        } else {
            holder.ivCropImage.setImageResource(R.drawable.place_holder)
        }
        
        // Set click listeners
        holder.itemView.setOnClickListener {
            onItemClick(cropHealthRecord)
        }
        
        holder.btnViewDetails.setOnClickListener {
            onItemClick(cropHealthRecord)
        }
    }

    override fun getItemCount(): Int = cropHealthRecords.size
    
    private fun getHealthStatusColor(context: android.content.Context, healthStatus: com.cvsuagritech.spim.models.CropHealthStatus): Int {
        return when (healthStatus) {
            com.cvsuagritech.spim.models.CropHealthStatus.HEALTHY -> ContextCompat.getColor(context, R.color.health_healthy)
            com.cvsuagritech.spim.models.CropHealthStatus.NUTRIENT_DEFICIENCY -> ContextCompat.getColor(context, R.color.health_nutrient_deficiency)
            com.cvsuagritech.spim.models.CropHealthStatus.PEST_DAMAGE -> ContextCompat.getColor(context, R.color.health_pest_damage)
            com.cvsuagritech.spim.models.CropHealthStatus.DISEASE -> ContextCompat.getColor(context, R.color.health_disease)
            com.cvsuagritech.spim.models.CropHealthStatus.ENVIRONMENTAL_STRESS -> ContextCompat.getColor(context, R.color.health_environmental_stress)
        }
    }
    
    private fun getHealthStatusBackground(context: android.content.Context, healthStatus: com.cvsuagritech.spim.models.CropHealthStatus): android.graphics.drawable.Drawable? {
        return when (healthStatus) {
            com.cvsuagritech.spim.models.CropHealthStatus.HEALTHY -> ContextCompat.getDrawable(context, R.drawable.health_status_healthy)
            com.cvsuagritech.spim.models.CropHealthStatus.NUTRIENT_DEFICIENCY -> ContextCompat.getDrawable(context, R.drawable.health_status_warning)
            com.cvsuagritech.spim.models.CropHealthStatus.PEST_DAMAGE -> ContextCompat.getDrawable(context, R.drawable.health_status_danger)
            com.cvsuagritech.spim.models.CropHealthStatus.DISEASE -> ContextCompat.getDrawable(context, R.drawable.health_status_danger)
            com.cvsuagritech.spim.models.CropHealthStatus.ENVIRONMENTAL_STRESS -> ContextCompat.getDrawable(context, R.drawable.health_status_info)
        }
    }
    
    private fun getSustainabilityColor(context: android.content.Context, sustainabilityScore: Float): Int {
        return when {
            sustainabilityScore >= 0.8f -> ContextCompat.getColor(context, R.color.sustainability_high)
            sustainabilityScore >= 0.6f -> ContextCompat.getColor(context, R.color.sustainability_medium)
            sustainabilityScore >= 0.4f -> ContextCompat.getColor(context, R.color.sustainability_low)
            else -> ContextCompat.getColor(context, R.color.sustainability_chemical)
        }
    }
}
