package com.buchanancreative.loggerlibrary.Manager.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.buchanancreative.loggerlibrary.Model.RoomExerciseType

@Dao
interface ExerciseTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(types: List<RoomExerciseType>)


}