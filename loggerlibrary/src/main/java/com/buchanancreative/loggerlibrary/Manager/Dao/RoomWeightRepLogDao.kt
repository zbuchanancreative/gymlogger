package com.buchanancreative.loggerlibrary.Manager.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.buchanancreative.loggerlibrary.Model.RoomWeightRepLog
import com.buchanancreative.loggerlibrary.Model.WeightRepLog
import java.util.*

@Dao
interface RoomWeightRepLogDao {
    @Insert
    suspend fun insert(log: RoomWeightRepLog)

    @Query("SELECT * FROM roomweightreplog")
    fun getAll(): LiveData<List<RoomWeightRepLog>>

    @Query("""SELECT *
                    FROM roomweightreplog
                    WHERE createdDate BETWEEN :date AND :now
                    AND exerciseId = :exerciseId""")
    fun getLogsForExerciseBetween(date: Date, now: Date, exerciseId: Int): List<RoomWeightRepLog>

    @Query("""SELECT *
                    FROM roomweightreplog
                    WHERE exerciseId = :exerciseId""")
    fun getAllLogsForExercise(exerciseId: Int): List<RoomWeightRepLog>

}

