package com.buchanancreative.loggerlibrary.Persistence

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buchanancreative.loggerlibrary.Model.Exercise
import com.buchanancreative.loggerlibrary.Model.LogType
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class ExerciseRealtimeDatabaseRepository: ExerciseRepository {
    override fun getAllExercises(onSuccess: (List<Exercise>) -> Unit) {
        getExercises(onSuccess)
    }

    private val exercises: MutableList<Exercise> = mutableListOf()

    private fun getExercises(onSuccess: (List<Exercise>) -> Unit) {
        val exerciseRef = FirebaseDatabase.getInstance().getReference("/exercises")

        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach { dataSnapshot ->
                    try {
                        val exercise = dataSnapshot.getValue<Exercise>(Exercise::class.java)!!
                        exercise.id = dataSnapshot.key ?: ""
                        exercises.add(exercise)
                    } catch (exception: Exception) {
                        Log.e("ERROR", exception.toString())
                    }
                }

                onSuccess.invoke(exercises.sortedBy { it.name })
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.i("HPF", "on FB Cancelled: " + p0.toString())
            }
        }

        exerciseRef.addListenerForSingleValueEvent(listener)
    }

}