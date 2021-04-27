package com.buchanancreative.loggerlibrary.Persistence

import androidx.lifecycle.LiveData
import com.buchanancreative.loggerlibrary.Model.*
import java.util.*

interface LoggingRepository {
    fun getAllExercisesInRoutine(routineId: String, onSuccess: (exercises: List<Exercise>) -> Unit): LiveData<List<Exercise>>

    fun deleteRoutine(routineId: String)

    fun deleteLog(logId: String)

    fun getAllRoutines(): LiveData<List<Routine>>

    fun getAllRoutines(onSuccess: (routines: List<Routine>) -> Unit)

    fun updateRoutineName(routineId: String, name: String)

    fun getRecentWeightRepLogs(exerciseId: String): LiveData<List<WeightRepLog>>

    suspend fun insert(log: WeightRepLog)

    suspend fun insert(log: RepLog)

    suspend fun insert(log: TimeLog)

    suspend fun insert(exercise: Exercise, routineId: String)

    suspend fun insert(exercises: List<Exercise>, routineId: String)

    suspend fun insert(routine: Routine): String

    fun getAllRepLogs(exerciseId: String, after: Date): LiveData<List<RepLog>>

    fun getAllDurationLogs(exerciseId: String, after: Date): LiveData<List<TimeLog>>

    fun getRecentRepLogs(exerciseId: String): LiveData<List<RepLog>>

    fun getRecentTimeLogs(exerciseId: String): LiveData<List<TimeLog>>

    fun getLogsAggregatedByExerciseId(fromDate: Long): LiveData<List<AggregateLogStat>>

    fun getAllWeightRepLogs(exerciseId: String, after: Date): LiveData<List<WeightRepLog>>

    fun getAllTimeLogs(exerciseId: String, after: Date): LiveData<List<TimeLog>>
}
