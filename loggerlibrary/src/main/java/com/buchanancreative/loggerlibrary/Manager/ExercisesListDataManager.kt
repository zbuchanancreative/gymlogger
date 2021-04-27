package com.buchanancreative.loggerlibrary.Manager

import android.util.Log

import com.google.gson.Gson
import com.buchanancreative.loggerlibrary.Interface.AsyncResponseDelegate
import com.buchanancreative.loggerlibrary.Interface.ExercisesListListener
import com.buchanancreative.loggerlibrary.Model.Exercise
import com.buchanancreative.loggerlibrary.Model.ExerciseResponse
import com.buchanancreative.loggerlibrary.Network.RetrofitFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

import java.util.ArrayList

/**
 * Created by buchanancreative on 10/11/17.
 */

class ExercisesListDataManager(listListener: ExercisesListListener) : AsyncResponseDelegate {
    var exercises: ArrayList<Exercise>? = null
        private set

    private var listListener: ExercisesListListener? = null

    init {
        this.listListener = listListener
        exercises = ArrayList()
    }

    fun loadExercises() {

        val service = RetrofitFactory.makeRetrofitService()
        GlobalScope.launch(Dispatchers.Main) {
            val request = service.getAllExercises()
            try {
                val response = request.await()
                Log.i("DownloadManager", "SUCCESS" + response.toString())
                listListener?.downloadComplete(response.exercises, response.types, response.exercise_types)
            } catch (e: HttpException) {
                Log.i("DownloadManager", " HTTP ERROR: " + e.message())
            } catch (e: Throwable) {
                Log.i("DownloadManager", "ERROR: " + e.localizedMessage)
            }
        }
    }

    private fun addExercisesToListFromJson(exerciseJson: String): ExerciseResponse {
        exercises?.clear()

        val gson = Gson()
        return  gson.fromJson(exerciseJson, ExerciseResponse::class.java)
    }

    override fun processFinished(result: String) {
        val exerciseResponse = addExercisesToListFromJson(result)

        listListener?.downloadComplete(exerciseResponse.exercises, exerciseResponse.types, exerciseResponse.exercise_types)
    }

    override fun requestDidSucceed() {
        Log.i("ExerciseListDataManager", "request did succeed")
    }
}
