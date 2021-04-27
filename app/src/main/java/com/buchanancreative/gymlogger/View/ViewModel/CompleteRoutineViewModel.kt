package com.buchanancreative.gymlogger.View.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import com.buchanancreative.gymlogger.LoggerApp

import com.buchanancreative.loggerlibrary.Model.Exercise
import com.buchanancreative.loggerlibrary.Persistence.*

class CompleteRoutineViewModel(application: Application, routineId: String): AndroidViewModel(application) {
    private val logRepo: LoggingRepository = FirebaseLoggingRepository((application as LoggerApp).getUserId())

    var exerciseIdsInRoutine: LiveData<List<Exercise>>? = null
    var routineId: Int? = null

    init {
        // create an interface to get exercises in routine
        exerciseIdsInRoutine = logRepo.getAllExercisesInRoutine(routineId) {} //  logRepo.getAllExercisesInRoutine(routineId)
    }

}