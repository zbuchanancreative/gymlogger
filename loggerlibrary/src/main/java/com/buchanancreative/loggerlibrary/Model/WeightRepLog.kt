package com.buchanancreative.loggerlibrary.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.buchanancreative.loggerlibrary.Util.DateTypeConverters
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.sql.Timestamp
import java.util.*

@Entity
data class RoomWeightRepLog(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        @ColumnInfo(name = "exerciseId") var exerciseId: Int,
        @ColumnInfo(name = "weight") var weight: Int,
        @ColumnInfo(name = "reps") var reps: Int,

        @ColumnInfo(name = "createdDate")
        @TypeConverters(DateTypeConverters::class)
        var createdDate: Date?
)



data class WeightRepLog(
        @DocumentId var id: String? = "",
        val exerciseId: String = "",
        val name: String = "",
        val weight: Double = 0.0,
        val reps: Int = 0,

        @ServerTimestamp
        val createdDate: Date? = null,

        var userId: String = "",
        val type: String = "weightRep"
)
