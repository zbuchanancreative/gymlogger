package com.buchanancreative.loggerlibrary.Interface

import com.buchanancreative.loggerlibrary.Model.Exercise
import com.buchanancreative.loggerlibrary.Model.ExerciseType

/**
 * Created by buchanancreative on 10/12/17.
 */

interface ExerciseClickListener {
    // change this to send the exercise type through, so that the main activity can trigger the right
    // log type setup, instead of looking it up again
    fun onExerciseClicked(exercise: Exercise)
}

