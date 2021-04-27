package com.buchanancreative.loggerlibrary.Interface


import com.buchanancreative.loggerlibrary.Model.Exercise
import com.buchanancreative.loggerlibrary.Model.ExerciseTypeMap
import com.buchanancreative.loggerlibrary.Model.RoomExercise
import com.buchanancreative.loggerlibrary.Model.RoomExerciseType

import java.util.ArrayList

/**
 * Created by buchanancreative on 7/7/18.
 */

interface ExercisesListListener {
    fun downloadComplete(exercise: ArrayList<RoomExercise>, types: ArrayList<RoomExerciseType>, exerciseTypes: ArrayList<ExerciseTypeMap>)
}
