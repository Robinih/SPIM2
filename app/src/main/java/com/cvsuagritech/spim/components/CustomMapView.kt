package com.cvsuagritech.spim.components

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.cvsuagritech.spim.R
import com.cvsuagritech.spim.models.FarmData

class CustomMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val textPaint = Paint()
    private val markerPaint = Paint()
    private val backgroundPaint = Paint()
    
    private var farmDataList = mutableListOf<FarmData>()
    private var selectedFarm: FarmData? = null
    private var onFarmClickListener: ((FarmData) -> Unit)? = null
    
    // Map bounds for Naic Cavite area
    private val mapBounds = RectF(0f, 0f, 0f, 0f)
    private val naicBounds = RectF(14.31f, 120.75f, 14.32f, 120.78f) // Approximate Naic area
    
    init {
        setupPaints()
    }
    
    private fun setupPaints() {
        // Background paint
        backgroundPaint.color = Color.parseColor("#E8F5E8")
        backgroundPaint.style = Paint.Style.FILL
        
        // Map area paint
        paint.color = Color.parseColor("#4CAF50")
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        
        // Text paint
        textPaint.color = Color.parseColor("#2E7D32")
        textPaint.textSize = 32f
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
        
        // Marker paint
        markerPaint.isAntiAlias = true
    }
    
    fun setFarmData(farms: List<FarmData>) {
        farmDataList.clear()
        farmDataList.addAll(farms)
        invalidate()
    }
    
    fun setOnFarmClickListener(listener: (FarmData) -> Unit) {
        onFarmClickListener = listener
    }
    
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mapBounds.set(0f, 0f, w.toFloat(), h.toFloat())
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // Draw background
        canvas.drawRect(mapBounds, backgroundPaint)
        
        // Draw map area (simplified representation of Naic)
        val mapArea = RectF(
            mapBounds.left + 50f,
            mapBounds.top + 50f,
            mapBounds.right - 50f,
            mapBounds.bottom - 200f
        )
        canvas.drawRoundRect(mapArea, 20f, 20f, paint)
        
        // Draw title
        canvas.drawText(
            "📍 Naic Cavite Farm Map",
            mapBounds.centerX(),
            mapBounds.top + 40f,
            textPaint
        )
        
        // Draw farm markers
        drawFarmMarkers(canvas, mapArea)
        
        // Draw legend
        drawLegend(canvas)
    }
    
    private fun drawFarmMarkers(canvas: Canvas, mapArea: RectF) {
        val markerRadius = 15f
        
        farmDataList.forEachIndexed { index, farm ->
            val x = mapArea.left + (index % 5) * (mapArea.width() / 4) + markerRadius
            val y = mapArea.top + (index / 5) * (mapArea.height() / 3) + markerRadius
            
            // Choose marker color based on status
            val markerColor = when (farm.status) {
                "Healthy" -> Color.parseColor("#4CAF50")
                "Warning" -> Color.parseColor("#FF9800")
                "Critical" -> Color.parseColor("#F44336")
                else -> Color.parseColor("#9E9E9E")
            }
            
            markerPaint.color = markerColor
            canvas.drawCircle(x, y, markerRadius, markerPaint)
            
            // Draw farm ID
            textPaint.color = Color.WHITE
            textPaint.textSize = 20f
            canvas.drawText(farm.id, x, y + 7f, textPaint)
            
            // Draw selection highlight
            if (selectedFarm == farm) {
                val highlightPaint = Paint()
                highlightPaint.color = Color.parseColor("#2196F3")
                highlightPaint.style = Paint.Style.STROKE
                highlightPaint.strokeWidth = 4f
                highlightPaint.isAntiAlias = true
                canvas.drawCircle(x, y, markerRadius + 5f, highlightPaint)
            }
        }
    }
    
    private fun drawLegend(canvas: Canvas) {
        val legendY = mapBounds.bottom - 150f
        val legendX = mapBounds.left + 20f
        
        textPaint.color = Color.parseColor("#2E7D32")
        textPaint.textSize = 24f
        textPaint.textAlign = Paint.Align.LEFT
        canvas.drawText("Legend:", legendX, legendY, textPaint)
        
        val legendItems = listOf(
            Pair("✅ Healthy", Color.parseColor("#4CAF50")),
            Pair("⚠️ Warning", Color.parseColor("#FF9800")),
            Pair("🚨 Critical", Color.parseColor("#F44336"))
        )
        
        legendItems.forEachIndexed { index, (label, color) ->
            val itemY = legendY + 30f + (index * 30f)
            
            // Draw colored circle
            markerPaint.color = color
            canvas.drawCircle(legendX + 10f, itemY - 10f, 8f, markerPaint)
            
            // Draw label
            textPaint.color = Color.parseColor("#2E7D32")
            textPaint.textSize = 20f
            canvas.drawText(label, legendX + 30f, itemY, textPaint)
        }
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val y = event.y
            
            // Check if touch is on any farm marker
            farmDataList.forEachIndexed { index, farm ->
                val mapArea = RectF(
                    mapBounds.left + 50f,
                    mapBounds.top + 50f,
                    mapBounds.right - 50f,
                    mapBounds.bottom - 200f
                )
                val markerX = mapArea.left + (index % 5) * (mapArea.width() / 4) + 15f
                val markerY = mapArea.top + (index / 5) * (mapArea.height() / 3) + 15f
                
                val distance = kotlin.math.sqrt(
                    ((x - markerX) * (x - markerX) + (y - markerY) * (y - markerY)).toDouble()
                )
                
                if (distance <= 20) {
                    selectedFarm = farm
                    onFarmClickListener?.invoke(farm)
                    invalidate()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }
}
