package com.buchanancreative.loggerlibrary.Manager.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import com.buchanancreative.loggerlibrary.Model.RoomTimeLog
import com.buchanancreative.loggerlibrary.Model.TimeLog
import java.util.*

@Dao
interface TimeLogDao {
    @Insert
    suspend fun insert(log: RoomTimeLog)

    @Query("SELECT * FROM roomtimelog")
    fun getAll(): LiveData<List<RoomTimeLog>>

    @Query("""SELECT *
                    FROM roomtimelog
                    WHERE createdDate
                    BETWEEN :date AND :now
                    AND exerciseId = :exerciseId""")
    fun getLogsForExerciseBetween(date: Date, now: Date, exerciseId: Int): List<RoomTimeLog>

    @Query("""SELECT *
                    FROM roomtimelog
                    WHERE exerciseId = :exerciseId""")
    fun getAll(exerciseId: Int): List<RoomTimeLog>
}