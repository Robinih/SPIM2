package com.cvsuagritech.spim.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.cvsuagritech.spim.models.CropHealthRecord
import com.cvsuagritech.spim.models.CropHealthStatus
import com.cvsuagritech.spim.models.GrowthStage
import com.cvsuagritech.spim.models.TreatmentRecommendation
import com.cvsuagritech.spim.models.TreatmentType
import com.cvsuagritech.spim.models.SustainabilityLevel
import com.cvsuagritech.spim.models.LGUReport
import com.cvsuagritech.spim.models.ReportType
import com.cvsuagritech.spim.models.RiskLevel
import java.io.ByteArrayOutputStream

class CropHealthDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_NAME = "crop_health_database.db"
        private const val DATABASE_VERSION = 2
        
        // Table names
        private const val TABLE_CROP_HEALTH_RECORDS = "crop_health_records"
        private const val TABLE_TREATMENT_RECOMMENDATIONS = "treatment_recommendations"
        private const val TABLE_LGU_REPORTS = "lgu_reports"
        
        // Common column names
        private const val COLUMN_ID = "id"
        private const val COLUMN_TIMESTAMP = "timestamp"
        
        // Crop Health Records columns
        private const val COLUMN_CROP_TYPE = "crop_type"
        private const val COLUMN_HEALTH_STATUS = "health_status"
        private const val COLUMN_CONFIDENCE = "confidence"
        private const val COLUMN_GROWTH_STAGE = "growth_stage"
        private const val COLUMN_IMAGE_PATH = "image_path"
        private const val COLUMN_IMAGE_BLOB = "image_blob"
        private const val COLUMN_LOCATION = "location"
        private const val COLUMN_NOTES = "notes"
        private const val COLUMN_TREATMENT_APPLIED = "treatment_applied"
        private const val COLUMN_SUSTAINABILITY_SCORE = "sustainability_score"
        
        // Treatment Recommendations columns
        private const val COLUMN_CROP_HEALTH_RECORD_ID = "crop_health_record_id"
        private const val COLUMN_TREATMENT_TYPE = "treatment_type"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_INSTRUCTIONS = "instructions"
        private const val COLUMN_SUSTAINABILITY_LEVEL = "sustainability_level"
        private const val COLUMN_EFFECTIVENESS = "effectiveness"
        private const val COLUMN_COST = "cost"
        private const val COLUMN_TIME_TO_APPLY = "time_to_apply"
        private const val COLUMN_ACTIVE_INGREDIENTS = "active_ingredients"
        private const val COLUMN_SAFETY_NOTES = "safety_notes"
        private const val COLUMN_IS_APPLIED = "is_applied"
        private const val COLUMN_APPLIED_DATE = "applied_date"
        private const val COLUMN_RESULTS = "results"
        
        // LGU Reports columns
        private const val COLUMN_REPORT_TYPE = "report_type"
        private const val COLUMN_BARANGAY_NAME = "barangay_name"
        private const val COLUMN_TOTAL_RECORDS = "total_records"
        private const val COLUMN_HEALTHY_CROPS = "healthy_crops"
        private const val COLUMN_NUTRIENT_DEFICIENCY = "nutrient_deficiency"
        private const val COLUMN_PEST_DAMAGE = "pest_damage"
        private const val COLUMN_DISEASE = "disease"
        private const val COLUMN_ENVIRONMENTAL_STRESS = "environmental_stress"
        private const val COLUMN_RISK_LEVEL = "risk_level"
        private const val COLUMN_INTERVENTION_NEEDED = "intervention_needed"
        private const val COLUMN_RECOMMENDATIONS = "recommendations"
        private const val COLUMN_GENERATED_BY = "generated_by"
        private const val COLUMN_GENERATED_DATE = "generated_date"
        private const val COLUMN_IS_SYNCED = "is_synced"
        private const val COLUMN_SYNC_DATE = "sync_date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Crop Health Records table
        val createCropHealthTable = """
            CREATE TABLE $TABLE_CROP_HEALTH_RECORDS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CROP_TYPE TEXT NOT NULL,
                $COLUMN_HEALTH_STATUS TEXT NOT NULL,
                $COLUMN_CONFIDENCE REAL NOT NULL,
                $COLUMN_GROWTH_STAGE TEXT NOT NULL,
                $COLUMN_IMAGE_PATH TEXT,
                $COLUMN_IMAGE_BLOB BLOB,
                $COLUMN_LOCATION TEXT,
                $COLUMN_NOTES TEXT,
                $COLUMN_TREATMENT_APPLIED TEXT,
                $COLUMN_SUSTAINABILITY_SCORE REAL DEFAULT 0.0,
                $COLUMN_TIMESTAMP INTEGER NOT NULL
            )
        """.trimIndent()
        
        // Create Treatment Recommendations table
        val createTreatmentTable = """
            CREATE TABLE $TABLE_TREATMENT_RECOMMENDATIONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CROP_HEALTH_RECORD_ID INTEGER NOT NULL,
                $COLUMN_TREATMENT_TYPE TEXT NOT NULL,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_INSTRUCTIONS TEXT NOT NULL,
                $COLUMN_SUSTAINABILITY_LEVEL TEXT NOT NULL,
                $COLUMN_EFFECTIVENESS REAL NOT NULL,
                $COLUMN_COST REAL NOT NULL,
                $COLUMN_TIME_TO_APPLY TEXT NOT NULL,
                $COLUMN_ACTIVE_INGREDIENTS TEXT,
                $COLUMN_SAFETY_NOTES TEXT,
                $COLUMN_IS_APPLIED INTEGER DEFAULT 0,
                $COLUMN_APPLIED_DATE INTEGER,
                $COLUMN_RESULTS TEXT,
                $COLUMN_TIMESTAMP INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_CROP_HEALTH_RECORD_ID) REFERENCES $TABLE_CROP_HEALTH_RECORDS($COLUMN_ID)
            )
        """.trimIndent()
        
        // Create LGU Reports table
        val createLGUReportsTable = """
            CREATE TABLE $TABLE_LGU_REPORTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_REPORT_TYPE TEXT NOT NULL,
                $COLUMN_BARANGAY_NAME TEXT NOT NULL,
                $COLUMN_TOTAL_RECORDS INTEGER NOT NULL,
                $COLUMN_HEALTHY_CROPS INTEGER NOT NULL,
                $COLUMN_NUTRIENT_DEFICIENCY INTEGER NOT NULL,
                $COLUMN_PEST_DAMAGE INTEGER NOT NULL,
                $COLUMN_DISEASE INTEGER NOT NULL,
                $COLUMN_ENVIRONMENTAL_STRESS INTEGER NOT NULL,
                $COLUMN_RISK_LEVEL TEXT NOT NULL,
                $COLUMN_INTERVENTION_NEEDED INTEGER DEFAULT 0,
                $COLUMN_RECOMMENDATIONS TEXT,
                $COLUMN_GENERATED_BY TEXT NOT NULL,
                $COLUMN_GENERATED_DATE INTEGER NOT NULL,
                $COLUMN_IS_SYNCED INTEGER DEFAULT 0,
                $COLUMN_SYNC_DATE INTEGER
            )
        """.trimIndent()
        
        db.execSQL(createCropHealthTable)
        db.execSQL(createTreatmentTable)
        db.execSQL(createLGUReportsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop old tables if they exist
        db.execSQL("DROP TABLE IF EXISTS pest_records")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CROP_HEALTH_RECORDS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TREATMENT_RECOMMENDATIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LGU_REPORTS")
        onCreate(db)
    }

    fun insertCropHealthRecord(cropHealthRecord: CropHealthRecord): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CROP_TYPE, cropHealthRecord.cropType)
            put(COLUMN_HEALTH_STATUS, cropHealthRecord.healthStatus.name)
            put(COLUMN_CONFIDENCE, cropHealthRecord.confidence)
            put(COLUMN_GROWTH_STAGE, cropHealthRecord.growthStage.name)
            put(COLUMN_IMAGE_PATH, cropHealthRecord.imagePath)
            put(COLUMN_IMAGE_BLOB, cropHealthRecord.imageBlob)
            put(COLUMN_LOCATION, cropHealthRecord.location)
            put(COLUMN_NOTES, cropHealthRecord.notes)
            put(COLUMN_TREATMENT_APPLIED, cropHealthRecord.treatmentApplied)
            put(COLUMN_SUSTAINABILITY_SCORE, cropHealthRecord.sustainabilityScore)
            put(COLUMN_TIMESTAMP, cropHealthRecord.timestamp)
        }
        
        return db.insert(TABLE_CROP_HEALTH_RECORDS, null, values)
    }

    fun getAllCropHealthRecords(): List<CropHealthRecord> {
        val records = mutableListOf<CropHealthRecord>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CROP_HEALTH_RECORDS,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_TIMESTAMP DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val record = CropHealthRecord(
                    id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)),
                    cropType = it.getString(it.getColumnIndexOrThrow(COLUMN_CROP_TYPE)),
                    healthStatus = CropHealthStatus.valueOf(it.getString(it.getColumnIndexOrThrow(COLUMN_HEALTH_STATUS))),
                    confidence = it.getFloat(it.getColumnIndexOrThrow(COLUMN_CONFIDENCE)),
                    growthStage = GrowthStage.valueOf(it.getString(it.getColumnIndexOrThrow(COLUMN_GROWTH_STAGE))),
                    imagePath = it.getString(it.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)),
                    imageBlob = it.getBlob(it.getColumnIndexOrThrow(COLUMN_IMAGE_BLOB)),
                    location = it.getString(it.getColumnIndexOrThrow(COLUMN_LOCATION)),
                    notes = it.getString(it.getColumnIndexOrThrow(COLUMN_NOTES)),
                    treatmentApplied = it.getString(it.getColumnIndexOrThrow(COLUMN_TREATMENT_APPLIED)),
                    sustainabilityScore = it.getFloat(it.getColumnIndexOrThrow(COLUMN_SUSTAINABILITY_SCORE)),
                    timestamp = it.getLong(it.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                )
                records.add(record)
            }
        }

        return records
    }

    fun getCropHealthRecordById(id: Long): CropHealthRecord? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CROP_HEALTH_RECORDS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        cursor.use {
            if (it.moveToFirst()) {
                return CropHealthRecord(
                    id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)),
                    cropType = it.getString(it.getColumnIndexOrThrow(COLUMN_CROP_TYPE)),
                    healthStatus = CropHealthStatus.valueOf(it.getString(it.getColumnIndexOrThrow(COLUMN_HEALTH_STATUS))),
                    confidence = it.getFloat(it.getColumnIndexOrThrow(COLUMN_CONFIDENCE)),
                    growthStage = GrowthStage.valueOf(it.getString(it.getColumnIndexOrThrow(COLUMN_GROWTH_STAGE))),
                    imagePath = it.getString(it.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)),
                    imageBlob = it.getBlob(it.getColumnIndexOrThrow(COLUMN_IMAGE_BLOB)),
                    location = it.getString(it.getColumnIndexOrThrow(COLUMN_LOCATION)),
                    notes = it.getString(it.getColumnIndexOrThrow(COLUMN_NOTES)),
                    treatmentApplied = it.getString(it.getColumnIndexOrThrow(COLUMN_TREATMENT_APPLIED)),
                    sustainabilityScore = it.getFloat(it.getColumnIndexOrThrow(COLUMN_SUSTAINABILITY_SCORE)),
                    timestamp = it.getLong(it.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                )
            }
        }
        return null
    }

    fun deleteCropHealthRecord(id: Long): Boolean {
        val db = writableDatabase
        return db.delete(TABLE_CROP_HEALTH_RECORDS, "$COLUMN_ID = ?", arrayOf(id.toString())) > 0
    }

    fun deleteAllCropHealthRecords(): Int {
        val db = writableDatabase
        return db.delete(TABLE_CROP_HEALTH_RECORDS, null, null)
    }

    fun getTotalRecordsCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_CROP_HEALTH_RECORDS", null)
        return if (cursor.moveToFirst()) {
            cursor.getInt(0)
        } else {
            0
        }.also { cursor.close() }
    }

    fun updateCropHealthRecord(cropHealthRecord: CropHealthRecord): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CROP_TYPE, cropHealthRecord.cropType)
            put(COLUMN_HEALTH_STATUS, cropHealthRecord.healthStatus.name)
            put(COLUMN_CONFIDENCE, cropHealthRecord.confidence)
            put(COLUMN_GROWTH_STAGE, cropHealthRecord.growthStage.name)
            put(COLUMN_IMAGE_PATH, cropHealthRecord.imagePath)
            put(COLUMN_IMAGE_BLOB, cropHealthRecord.imageBlob)
            put(COLUMN_LOCATION, cropHealthRecord.location)
            put(COLUMN_NOTES, cropHealthRecord.notes)
            put(COLUMN_TREATMENT_APPLIED, cropHealthRecord.treatmentApplied)
            put(COLUMN_SUSTAINABILITY_SCORE, cropHealthRecord.sustainabilityScore)
            put(COLUMN_TIMESTAMP, cropHealthRecord.timestamp)
        }
        
        return db.update(
            TABLE_CROP_HEALTH_RECORDS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(cropHealthRecord.id.toString())
        ) > 0
    }

    // Helper function to convert Bitmap to ByteArray
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    // Helper function to convert ByteArray to Bitmap
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return try {
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } catch (e: Exception) {
            null
        }
    }

    // Treatment Recommendations methods
    fun insertTreatmentRecommendation(recommendation: TreatmentRecommendation): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CROP_HEALTH_RECORD_ID, recommendation.cropHealthRecordId)
            put(COLUMN_TREATMENT_TYPE, recommendation.treatmentType.name)
            put(COLUMN_TITLE, recommendation.title)
            put(COLUMN_DESCRIPTION, recommendation.description)
            put(COLUMN_INSTRUCTIONS, recommendation.instructions)
            put(COLUMN_SUSTAINABILITY_LEVEL, recommendation.sustainabilityLevel.name)
            put(COLUMN_EFFECTIVENESS, recommendation.effectiveness)
            put(COLUMN_COST, recommendation.cost)
            put(COLUMN_TIME_TO_APPLY, recommendation.timeToApply)
            put(COLUMN_ACTIVE_INGREDIENTS, recommendation.activeIngredients)
            put(COLUMN_SAFETY_NOTES, recommendation.safetyNotes)
            put(COLUMN_IS_APPLIED, if (recommendation.isApplied) 1 else 0)
            put(COLUMN_APPLIED_DATE, recommendation.appliedDate)
            put(COLUMN_RESULTS, recommendation.results)
            put(COLUMN_TIMESTAMP, recommendation.timestamp)
        }
        
        return db.insert(TABLE_TREATMENT_RECOMMENDATIONS, null, values)
    }

    fun getTreatmentRecommendationsByRecordId(recordId: Long): List<TreatmentRecommendation> {
        val recommendations = mutableListOf<TreatmentRecommendation>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_TREATMENT_RECOMMENDATIONS,
            null,
            "$COLUMN_CROP_HEALTH_RECORD_ID = ?",
            arrayOf(recordId.toString()),
            null,
            null,
            "$COLUMN_TIMESTAMP DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val recommendation = TreatmentRecommendation(
                    id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)),
                    cropHealthRecordId = it.getLong(it.getColumnIndexOrThrow(COLUMN_CROP_HEALTH_RECORD_ID)),
                    treatmentType = TreatmentType.valueOf(it.getString(it.getColumnIndexOrThrow(COLUMN_TREATMENT_TYPE))),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE)),
                    description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    instructions = it.getString(it.getColumnIndexOrThrow(COLUMN_INSTRUCTIONS)),
                    sustainabilityLevel = SustainabilityLevel.valueOf(it.getString(it.getColumnIndexOrThrow(COLUMN_SUSTAINABILITY_LEVEL))),
                    effectiveness = it.getFloat(it.getColumnIndexOrThrow(COLUMN_EFFECTIVENESS)),
                    cost = it.getFloat(it.getColumnIndexOrThrow(COLUMN_COST)),
                    timeToApply = it.getString(it.getColumnIndexOrThrow(COLUMN_TIME_TO_APPLY)),
                    activeIngredients = it.getString(it.getColumnIndexOrThrow(COLUMN_ACTIVE_INGREDIENTS)),
                    safetyNotes = it.getString(it.getColumnIndexOrThrow(COLUMN_SAFETY_NOTES)),
                    isApplied = it.getInt(it.getColumnIndexOrThrow(COLUMN_IS_APPLIED)) == 1,
                    appliedDate = it.getLong(it.getColumnIndexOrThrow(COLUMN_APPLIED_DATE)).takeIf { date -> date > 0 },
                    results = it.getString(it.getColumnIndexOrThrow(COLUMN_RESULTS)),
                    timestamp = it.getLong(it.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                )
                recommendations.add(recommendation)
            }
        }

        return recommendations
    }

    // LGU Reports methods
    fun insertLGUReport(report: LGUReport): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_REPORT_TYPE, report.reportType.name)
            put(COLUMN_BARANGAY_NAME, report.barangayName)
            put(COLUMN_TOTAL_RECORDS, report.totalRecords)
            put(COLUMN_HEALTHY_CROPS, report.healthyCrops)
            put(COLUMN_NUTRIENT_DEFICIENCY, report.nutrientDeficiency)
            put(COLUMN_PEST_DAMAGE, report.pestDamage)
            put(COLUMN_DISEASE, report.disease)
            put(COLUMN_ENVIRONMENTAL_STRESS, report.environmentalStress)
            put(COLUMN_RISK_LEVEL, report.riskLevel.name)
            put(COLUMN_INTERVENTION_NEEDED, if (report.interventionNeeded) 1 else 0)
            put(COLUMN_RECOMMENDATIONS, report.recommendations)
            put(COLUMN_GENERATED_BY, report.generatedBy)
            put(COLUMN_GENERATED_DATE, report.generatedDate)
            put(COLUMN_IS_SYNCED, if (report.isSynced) 1 else 0)
            put(COLUMN_SYNC_DATE, report.syncDate)
        }
        
        return db.insert(TABLE_LGU_REPORTS, null, values)
    }

    fun getAllLGUReports(): List<LGUReport> {
        val reports = mutableListOf<LGUReport>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_LGU_REPORTS,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_GENERATED_DATE DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val report = LGUReport(
                    id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)),
                    reportType = ReportType.valueOf(it.getString(it.getColumnIndexOrThrow(COLUMN_REPORT_TYPE))),
                    barangayName = it.getString(it.getColumnIndexOrThrow(COLUMN_BARANGAY_NAME)),
                    totalRecords = it.getInt(it.getColumnIndexOrThrow(COLUMN_TOTAL_RECORDS)),
                    healthyCrops = it.getInt(it.getColumnIndexOrThrow(COLUMN_HEALTHY_CROPS)),
                    nutrientDeficiency = it.getInt(it.getColumnIndexOrThrow(COLUMN_NUTRIENT_DEFICIENCY)),
                    pestDamage = it.getInt(it.getColumnIndexOrThrow(COLUMN_PEST_DAMAGE)),
                    disease = it.getInt(it.getColumnIndexOrThrow(COLUMN_DISEASE)),
                    environmentalStress = it.getInt(it.getColumnIndexOrThrow(COLUMN_ENVIRONMENTAL_STRESS)),
                    riskLevel = RiskLevel.valueOf(it.getString(it.getColumnIndexOrThrow(COLUMN_RISK_LEVEL))),
                    interventionNeeded = it.getInt(it.getColumnIndexOrThrow(COLUMN_INTERVENTION_NEEDED)) == 1,
                    recommendations = it.getString(it.getColumnIndexOrThrow(COLUMN_RECOMMENDATIONS)),
                    generatedBy = it.getString(it.getColumnIndexOrThrow(COLUMN_GENERATED_BY)),
                    generatedDate = it.getLong(it.getColumnIndexOrThrow(COLUMN_GENERATED_DATE)),
                    isSynced = it.getInt(it.getColumnIndexOrThrow(COLUMN_IS_SYNCED)) == 1,
                    syncDate = it.getLong(it.getColumnIndexOrThrow(COLUMN_SYNC_DATE)).takeIf { date -> date > 0 }
                )
                reports.add(report)
            }
        }

        return reports
    }
}
