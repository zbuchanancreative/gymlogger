package com.buchanancreative.loggerlibrary.Manager.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.buchanancreative.loggerlibrary.Model.RoomRoutine
import com.buchanancreative.loggerlibrary.Model.Routine


@Dao
interface RoutineDao {
    @Insert
    suspend fun insert(routine: RoomRoutine): Long

    @Query("""SELECT MAX(id)
                    FROM roomroutine""")
    fun getLastInsertedRoutine(): Int

    @Update
    fun updateRoutine(routine: RoomRoutine)

    @Query("""SELECT *
                    FROM roomroutine """)
    fun getAllRoutines(): LiveData<List<RoomRoutine>>

    @Query("""SELECT name
                    FROM roomroutine
                    WHERE id = :id""")
    fun getRoutineNameByFromId(id: Int): String

    @Delete
    fun deleteRoutine(routine: RoomRoutine)

}