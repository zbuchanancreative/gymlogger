package com.buchanancreative.loggerlibrary.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.buchanancreative.loggerlibrary.Util.DateTypeConverters
import com.google.firebase.firestore.DocumentId
import java.util.*


@Entity
data class RoomTimeLog(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        @ColumnInfo(name = "exerciseId") var exerciseId: Int,
        @ColumnInfo(name = "reps") var duration: Int,

        @ColumnInfo(name = "createdDate")
        @TypeConverters(DateTypeConverters::class)
        var createdDate: Date?
)

data class TimeLog(
        @DocumentId var id: String? = "",
        val exerciseId: String = "",
        val name: String = "",
        val duration: Int = 0,

        // Why no servertimestamp?
        val createdDate: Date = Date(),

        var userId: String = "",
        val type: String = "time"
)

