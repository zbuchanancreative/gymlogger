package com.buchanancreative.loggerlibrary.Manager.Dao

import androidx.room.Dao
import androidx.room.Query
import com.buchanancreative.loggerlibrary.Model.AggregateLogStat
import com.buchanancreative.loggerlibrary.Model.GeneralLogStat
import java.util.*


@Dao
interface GeneralStatLogDao {

    @Query("""SELECT id, exerciseId, createdDate
                    FROM roomtimelog
                    UNION ALL
                    SELECT id, exerciseId, createdDate
                    FROM roomweightreplog
                    UNION ALL
                    SELECT id, exerciseId, createdDate
                    FROM roomreplog
                    ORDER BY createdDate""")
    fun getAllLogsByDate(): List<GeneralLogStat>


    @Query("""SELECT exerciseId
                    FROM roomtimelog
                    UNION ALL
                    SELECT exerciseId
                    FROM roomweightreplog
                    UNION ALL
                    SELECT exerciseId
                    FROM roomreplog
                    WHERE createdDate
                    BETWEEN :date1 AND :date2""")
    fun getLogsBetweenDates(date1: Date, date2: Date): List<Int>


    @Query("""SELECT exerciseId, COUNT(exerciseId) as count
                    FROM roomtimelog
                    GROUP BY exerciseId
                    UNION
                    SELECT exerciseId, COUNT(exerciseId)
                    FROM roomweightreplog
                    UNION
                    SELECT exerciseId, COUNT(exerciseId)
                    FROM roomreplog
                    GROUP BY exerciseId""")
    fun getLogsAggregatedById(): List<AggregateLogStat>


    @Query("""SELECT exerciseId, COUNT(exerciseId) as count
                    FROM roomtimelog
                    WHERE createdDate BETWEEN :date2 AND :date1
                    GROUP BY exerciseId
                    UNION ALL
                    SELECT exerciseId, COUNT(exerciseId)
                    FROM roomweightreplog
                    WHERE createdDate BETWEEN :date2 AND :date1
                    GROUP BY exerciseId
                    UNION ALL
                    SELECT exerciseId, COUNT(exerciseId)
                    FROM roomreplog
                    WHERE createdDate BETWEEN :date2 AND :date1
                    GROUP BY exerciseId""")
    fun getLogsAggregatedByIdBetweenDates(date1: Long, date2: Long): List<AggregateLogStat>

}
