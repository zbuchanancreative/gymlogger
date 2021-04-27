package com.buchanancreative.loggerlibrary.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.buchanancreative.loggerlibrary.Util.DateTypeConverters
import com.google.firebase.firestore.DocumentId
import java.util.*


@Entity
data class RoomRepLog(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        @ColumnInfo(name = "exerciseId") var exerciseId: Int,
        @ColumnInfo(name = "reps") var reps: Int,

        @ColumnInfo(name = "createdDate")
        @TypeConverters(DateTypeConverters::class)
        var createdDate: Date?
)


data class RepLog(
        @DocumentId var id: String? = "",
        val exerciseId: String = "",
        val name: String = "",
        val reps: Int = 0,

        // Why no Server Timestamp?
        val createdDate: Date = Date(),

        var userId: String = "",
        val type: String = "rep"
)