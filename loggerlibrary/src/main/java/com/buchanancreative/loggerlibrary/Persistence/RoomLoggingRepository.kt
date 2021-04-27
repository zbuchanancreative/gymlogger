package com.buchanancreative.loggerlibrary.Persistence

import androidx.lifecycle.LiveData
import com.buchanancreative.loggerlibrary.Manager.Dao.*
import com.buchanancreative.loggerlibrary.Model.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class RoomLoggingRepository(loggingDatabase: LoggingDatabase) {

    private var weightRepDao: RoomWeightRepLogDao? = null
    private var repLogDao: RepLogDao? = null
    private var timeLogDao: TimeLogDao? = null
    private var generalStatLogDao: GeneralStatLogDao? = null
    private var routineEntryDao: RoutineEntryDao? = null
    private var routineDao: RoutineDao? = null

    private var weightRepLogs: LiveData<List<RoomWeightRepLog>>
    private var repLogs: LiveData<List<RoomRepLog>>
    private var timeLogs: LiveData<List<RoomTimeLog>>

    init {
        weightRepDao = loggingDatabase.logDao()
        repLogDao = loggingDatabase.repLogDao()
        timeLogDao = loggingDatabase.timeLogDao()
        generalStatLogDao = loggingDatabase.generalLogDao()

        routineEntryDao = loggingDatabase.routineEntryDao()
        routineDao = loggingDatabase.routineDao()

        weightRepLogs = weightRepDao!!.getAll()
        repLogs = repLogDao!!.getAll()
        timeLogs = timeLogDao!!.getAll()
    }


    companion object {

        @Volatile private var INSTANCE: RoomLoggingRepository? = null

        fun getInstance(loggingDatabase: LoggingDatabase): RoomLoggingRepository =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: RoomLoggingRepository(loggingDatabase).also { INSTANCE = it }
                }
    }

    suspend fun insert(log: RoomWeightRepLog) {
        weightRepDao?.insert(log)
    }

    suspend fun insert(log: RoomRepLog) {
        repLogDao?.insert(log)
    }

    suspend fun insert(log: RoomTimeLog) {
        timeLogDao?.insert(log)
    }

    suspend fun insert(routineEntry: RoutineEntry) {
        routineEntryDao?.insert(routineEntry)
    }


    suspend fun insert(routine: RoomRoutine): Long? {
        return routineDao?.insert(routine)
    }



    fun delete(routine: RoomRoutine) {
        routineDao?.deleteRoutine(routine)
    }


    fun getAllSavedWeightRepLogs(): LiveData<List<RoomWeightRepLog>> {
        return weightRepLogs
    }

    fun getAllWeightRepLogs(exerciseId: String): List<WeightRepLog> {
//        return weightRepDao!!.getAllLogsForExercise(exerciseId)
        return ArrayList()
    }

    fun getAllRepLogs(exerciseId: String): List<RoomRepLog> {
//        return repLogDao!!.getAll(exerciseId)
        return ArrayList() //  return real list
    }

    fun getAllDurationLogs(exerciseId: String): List<TimeLog> {
//        return timeLogDao!!.getAll(exerciseId)
        return ArrayList() //  return real list
    }

    fun getAllExercisesInRoutine(routineId: Int): LiveData<List<Int>> {
        return routineEntryDao!!.getAllExercisesForRoutine(routineId)
    }



    fun getAllRoutines(): LiveData<List<RoomRoutine>> {
        return routineDao!!.getAllRoutines()
    }

    fun getLastInsertedRoutineId(): Int {
        return routineDao!!.getLastInsertedRoutine()
    }


    fun updateRoutine(routine: RoomRoutine) {
        routineDao!!.updateRoutine(routine)
    }

    fun getRoutineName(id: Int): String {
        return  routineDao!!.getRoutineNameByFromId(id)
    }

    fun getRecentWeightRepLogs(exerciseId: Int): List<RoomWeightRepLog> {
        val now = Date(System.currentTimeMillis())
        val past = Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(12))
        return weightRepDao!!.getLogsForExerciseBetween( past, now, exerciseId)
    }

    fun getAllRepLogs(): LiveData<List<RoomRepLog>> {
        return repLogs
    }

    fun getRecentRepLogs(exerciseId: String): List<RoomRepLog> {
        val now = Date(System.currentTimeMillis())
        val past = Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(12))
        // return repLogDao!!.getLogsForExerciseBetween(past, now, exerciseId) // fix this in ROOM
        return ArrayList()
    }

    fun getAllTimeLogs(): LiveData<List<RoomTimeLog>> {
        return timeLogs
    }

    fun getRecentTimeLogs(exerciseId: Int): List<RoomTimeLog> {
        val now = Date(System.currentTimeMillis())
        val past = Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(12))
        return timeLogDao!!.getLogsForExerciseBetween(past, now, exerciseId)
    }

    fun getLogsAggregatedByExerciseIdBetweenDates(date1: Long, date2: Long): List<AggregateLogStat> {
        return generalStatLogDao!!.getLogsAggregatedByIdBetweenDates(date1, date2)
    }


}