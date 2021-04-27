package com.buchanancreative.loggerlibrary.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomExerciseType (
        @ColumnInfo(name = "name") val name: String,
        @PrimaryKey @ColumnInfo(name = "id")val id: Int
)