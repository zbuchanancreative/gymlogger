package com.buchanancreative.loggerlibrary.Model


public data class ExerciseResponse (
    val exercises: ArrayList<RoomExercise>,
    val types: ArrayList<RoomExerciseType>,
    val exercise_types: ArrayList<ExerciseTypeMap>
)