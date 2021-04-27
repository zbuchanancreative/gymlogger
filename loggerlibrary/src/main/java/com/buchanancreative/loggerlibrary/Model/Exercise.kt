package com.buchanancreative.loggerlibrary.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomExercise(
        @PrimaryKey @ColumnInfo(name = "id") var id: Int,
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "type") var type: String?,
        @ColumnInfo(name = "description") var description: String?
)

@Entity(primaryKeys = ["routineId","exerciseId"])
data class RoutineEntry(
        @ColumnInfo(name = "routineId") var routineId: Int,
        @ColumnInfo(name = "exerciseId") var exerciseId: Int
)

@Entity
data class RoomRoutine(
        @PrimaryKey(autoGenerate = true)  var id: Int = 0,
        @ColumnInfo(name = "name") var name: String?
)