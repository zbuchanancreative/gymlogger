package com.buchanancreative.loggerlibrary.Manager.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.buchanancreative.loggerlibrary.Model.Exercise
import com.buchanancreative.loggerlibrary.Model.RoomExercise

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(exercises: List<RoomExercise>)

    @Insert
    fun insert(exercise: RoomExercise)

    @Query("""SELECT *
                    FROM roomexercise
                    ORDER BY name""")
    fun getAll(): LiveData<List<RoomExercise>>

    @Query("""SELECT name
                    FROM roomexercise
                    WHERE id = :exerciseId""")
    fun getExerciseName(exerciseId: Int): String

    @Query("""SELECT typeId
                    FROM roomexercisetype JOIN ExerciseTypeMap
                    where ExerciseTypeMap.typeId = roomexercisetype.id and ExerciseTypeMap.exerciseId = :exerciseId""")
    fun getExerciseType(exerciseId: Int): Int
}

