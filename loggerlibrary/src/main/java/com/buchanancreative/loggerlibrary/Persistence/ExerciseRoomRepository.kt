package com.buchanancreative.loggerlibrary.Persistence

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.buchanancreative.loggerlibrary.Interface.ExercisesListListener
import com.buchanancreative.loggerlibrary.Manager.Dao.ExerciseDao
import com.buchanancreative.loggerlibrary.Manager.Dao.ExerciseTypeDao
import com.buchanancreative.loggerlibrary.Manager.Dao.ExerciseTypeMapDao
import com.buchanancreative.loggerlibrary.Manager.ExercisesListDataManager

import com.buchanancreative.loggerlibrary.Model.*
import kotlin.collections.ArrayList

class ExerciseRoomRepository(exerciseDatabase: ExerciseDatabase) : ExercisesListListener, ExerciseRepository {

    private var exerciseDao: ExerciseDao? = null
    private var exerciseTypeDao: ExerciseTypeDao? = null
    private var typesMapMapDao: ExerciseTypeMapDao? = null

    private var allExercises: LiveData<List<RoomExercise>>

    init {
        exerciseDao = exerciseDatabase.exerciseDao()
        exerciseTypeDao = exerciseDatabase.typesDao()
        typesMapMapDao = exerciseDatabase.typeMapDao()
        allExercises = exerciseDao!!.getAll()
    }

    companion object {
        @Volatile private var INSTANCE: ExerciseRoomRepository? = null

        fun getInstance(exerciseDatabase: ExerciseDatabase): ExerciseRoomRepository =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: ExerciseRoomRepository(exerciseDatabase).also { INSTANCE = it }
                }
    }


    override fun downloadComplete(exercise: ArrayList<RoomExercise>, types: ArrayList<RoomExerciseType>, exerciseTypes: ArrayList<ExerciseTypeMap>) {
        exercise.let {
            exerciseDao?.insertAll(it)
        }

        types.let {
            exerciseTypeDao?.insertAll(it)
        }

        exerciseTypes.let {
            typesMapMapDao?.insertAll(it)
        }
    }

    override fun getAllExercises(onSuccess: (exercises: List<Exercise>) -> Unit) {
        downloadExerciseList()
    }


    private fun downloadExerciseList() {
        val dataMgr = ExercisesListDataManager(this)
        dataMgr.loadExercises()
    }

    fun insert(exercise: RoomExercise) {
        exerciseDao?.let {
            InsertAsyncTask(it).execute(exercise)
        }
    }


    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: ExerciseDao) : AsyncTask<RoomExercise, Void, Void>() {
        override fun doInBackground(vararg params: RoomExercise): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

}