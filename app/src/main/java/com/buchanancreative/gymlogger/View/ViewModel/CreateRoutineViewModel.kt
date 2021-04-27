package com.buchanancreative.gymlogger.View.ViewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.buchanancreative.gymlogger.LoggerApp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.buchanancreative.gymlogger.R
import com.buchanancreative.loggerlibrary.Model.Exercise
import com.buchanancreative.loggerlibrary.Model.Routine

import com.buchanancreative.loggerlibrary.Persistence.*
import kotlinx.coroutines.launch

class CreateRoutineViewModel(application: Application, private var lifecycleOwner: LifecycleOwner): AndroidViewModel(application) {
    private val exerciseRepository: ExerciseRepository = ExerciseRealtimeDatabaseRepository()
    private val loggingRepository: LoggingRepository = FirebaseLoggingRepository((application as LoggerApp).getUserId())

    // Used to display the full list of exercises
    val allExercises = MutableLiveData<List<Exercise>>()

    // Used to store the exercises that the user has selected for this routine
    private var exercisesInRoutine = arrayListOf<Exercise>()

    val inRoutineExercises = MutableLiveData<List<Exercise>>()


    var routineName: String = "Routine"

    init {
        exerciseRepository.getAllExercises {
            allExercises.value = it
        }
    }

    fun saveRoutine() {
        viewModelScope.launch {
            val routine = Routine(name = routineName)
            val selectedRoutine = loggingRepository.insert(routine)
            // add exercises to routine all at once, after the routine has been created
            loggingRepository.insert(exercisesInRoutine, selectedRoutine)
        }
    }




    fun exerciseClicked(exercise: Exercise) {
        if (isInRoutine(exercise)) {
            exercisesInRoutine.remove(exercise)
        } else {
            exercisesInRoutine.add(exercise)
        }
        inRoutineExercises.postValue(exercisesInRoutine)
    }

    private fun isInRoutine(exercise: Exercise): Boolean = exercisesInRoutine.contains(exercise)



}