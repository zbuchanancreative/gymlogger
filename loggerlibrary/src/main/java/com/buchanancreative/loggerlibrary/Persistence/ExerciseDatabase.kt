package com.buchanancreative.loggerlibrary.Persistence


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.buchanancreative.loggerlibrary.Manager.Dao.ExerciseDao
import com.buchanancreative.loggerlibrary.Manager.Dao.ExerciseTypeDao
import com.buchanancreative.loggerlibrary.Manager.Dao.ExerciseTypeMapDao
import com.buchanancreative.loggerlibrary.Model.Exercise
import com.buchanancreative.loggerlibrary.Model.ExerciseTypeMap
import com.buchanancreative.loggerlibrary.Model.RoomExercise
import com.buchanancreative.loggerlibrary.Model.RoomExerciseType

@Database(entities = arrayOf(RoomExercise::class, RoomExerciseType::class, ExerciseTypeMap::class), version = 1)
abstract class ExerciseDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun typesDao(): ExerciseTypeDao
    abstract fun typeMapDao(): ExerciseTypeMapDao

    companion object {
        @Volatile private var INSTANCE: ExerciseDatabase? = null

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        ExerciseDatabase::class.java, "exercise.db")
                        .allowMainThreadQueries() // REMOVE allowing main thread queries
                        .build()

        fun getInstance(context: Context): ExerciseDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }
    }
}