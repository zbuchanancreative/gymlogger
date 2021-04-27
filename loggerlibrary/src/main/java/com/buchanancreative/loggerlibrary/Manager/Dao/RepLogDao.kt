package com.buchanancreative.loggerlibrary.Manager.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.buchanancreative.loggerlibrary.Model.RoomRepLog
import java.util.*

@Dao
interface RepLogDao {
    @Insert
    suspend fun insert(log: RoomRepLog)

    @Query("SELECT * FROM roomreplog")
    fun getAll(): LiveData<List<RoomRepLog>>


    @Query("""SELECT *
                    FROM roomreplog
                    WHERE createdDate BETWEEN :date AND :now AND exerciseId = :exerciseId""")
    fun getLogsForExerciseBetween(date: Date, now: Date, exerciseId: Int): List<RoomRepLog>

    @Query("""SELECT *
                    FROM roomreplog
                    WHERE exerciseId = :exerciseId""")
    fun getAll(exerciseId: Int): List<RoomRepLog>
}


