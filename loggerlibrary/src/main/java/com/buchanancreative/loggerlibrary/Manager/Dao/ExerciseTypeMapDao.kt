package com.buchanancreative.loggerlibrary.Manager.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.buchanancreative.loggerlibrary.Model.ExerciseTypeMap


@Dao
interface ExerciseTypeMapDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(exerciseTypes: List<ExerciseTypeMap>)


    @Query("SELECT * FROM exercisetypemap where exerciseId LIKE :exerciseId")
    fun getExerciseType(exerciseId: Int): ExerciseTypeMap
}
