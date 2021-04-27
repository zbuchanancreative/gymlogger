package com.buchanancreative.loggerlibrary.Manager.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.buchanancreative.loggerlibrary.Model.RoutineEntry

@Dao
interface RoutineEntryDao {
    @Insert
    suspend fun insert(routineEntry: RoutineEntry)

    @Query("""SELECT exerciseId
                    FROM routineentry
                    WHERE routineId = :routineId""")
    fun getAllExercisesForRoutine(routineId: Int): LiveData<List<Int>>

    @Query("""SELECT routineId
                    FROM routineentry
                    GROUP BY routineId""")
    fun getRoutinesById(): LiveData<List<Int>>

    @Query("""SELECT exerciseId
                    FROM routineentry""")
    fun getAllExercises(): LiveData<List<Int>>
}