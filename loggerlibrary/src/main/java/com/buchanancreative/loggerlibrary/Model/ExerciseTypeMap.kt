package com.buchanancreative.loggerlibrary.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExerciseTypeMap(
        @PrimaryKey @ColumnInfo(name = "exerciseId") val exerciseId: Int,
        @ColumnInfo(name = "typeId") val typeId: Int
)