package com.buchanancreative.loggerlibrary.Persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.buchanancreative.loggerlibrary.Manager.Dao.*
import com.buchanancreative.loggerlibrary.Model.*
import com.buchanancreative.loggerlibrary.Util.DateTypeConverters


@Database(entities = arrayOf(RoomWeightRepLog::class, RoomRepLog::class, RoomTimeLog::class, RoutineEntry::class, RoomRoutine::class), version = 1)
@TypeConverters(DateTypeConverters::class)
abstract class LoggingDatabase : RoomDatabase() {

    abstract fun logDao(): RoomWeightRepLogDao
    abstract fun repLogDao(): RepLogDao
    abstract fun timeLogDao(): TimeLogDao
    abstract fun generalLogDao(): GeneralStatLogDao
    abstract fun routineEntryDao(): RoutineEntryDao
    abstract fun routineDao(): RoutineDao
    
    companion object {
        @Volatile private var INSTANCE: LoggingDatabase? = null

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        LoggingDatabase::class.java, "logging.db")
                        .allowMainThreadQueries() // REMOVE allowing main thread queries
                        .build()

        fun getInstance(context: Context): LoggingDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }
    }

}