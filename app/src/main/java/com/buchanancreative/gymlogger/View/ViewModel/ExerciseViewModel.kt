package com.buchanancreative.gymlogger.View.ViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.buchanancreative.gymlogger.LoggerApp
import com.buchanancreative.loggerlibrary.Model.Exercise
import com.buchanancreative.loggerlibrary.Model.LogType
import com.buchanancreative.loggerlibrary.Persistence.ExerciseRealtimeDatabaseRepository
import com.buchanancreative.loggerlibrary.Persistence.ExerciseRepository
import com.buchanancreative.loggerlibrary.Persistence.ExerciseRoomRepository


class ExerciseViewModel(application: Application, val lifecycleOwner: LifecycleOwner): AndroidViewModel(application) {
    private val roomRepository: ExerciseRepository  = ExerciseRealtimeDatabaseRepository()

    internal val allExercises = MutableLiveData<List<Exercise>>()

    init {
        roomRepository.getAllExercises {
            allExercises.postValue(it)
        }
    }

}



