package com.buchanancreative.loggerlibrary.Interface

interface RoutineSelectedListener {
    fun onRoutineSelected(routineId: String, routineName: String)
    fun onEditRoutineSelected(routineId: String)
}