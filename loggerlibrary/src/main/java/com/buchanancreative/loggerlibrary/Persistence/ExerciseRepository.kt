package com.buchanancreative.loggerlibrary.Persistence

import androidx.lifecycle.LiveData
import com.buchanancreative.loggerlibrary.Model.Exercise
import com.buchanancreative.loggerlibrary.Model.LogType

interface ExerciseRepository {
    fun getAllExercises(onSuccess: (exercises: List<Exercise>) -> Unit)

}