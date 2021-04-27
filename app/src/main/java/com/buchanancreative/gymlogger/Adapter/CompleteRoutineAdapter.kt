package com.buchanancreative.gymlogger.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.buchanancreative.gymlogger.View.ViewModel.CompleteRoutineViewModel
import com.buchanancreative.gymlogger.R
import com.buchanancreative.loggerlibrary.Model.Exercise
import com.buchanancreative.loggerlibrary.Model.ExerciseType
import kotlinx.android.synthetic.main.complete_routine_fragment.view.*
import kotlinx.android.synthetic.main.in_routine_row.view.*
import java.util.*

class CompleteRoutineAdapter(private val viewModel: CompleteRoutineViewModel, private val onExerciseClicked: (exercise: Exercise) -> Unit): RecyclerView.Adapter<CompleteRoutineAdapter.CompleteRoutineViewHolder>() {


    private var exercisesInRoutine: List<Exercise>? = Collections.emptyList()

    override fun getItemCount(): Int {
        return exercisesInRoutine?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompleteRoutineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.in_routine_row, parent, false)
        return CompleteRoutineViewHolder(view)
    }


    override fun onBindViewHolder(holder: CompleteRoutineViewHolder, position: Int) {
        exercisesInRoutine?.let {
            val exercise = it[position]

            holder.name.text = exercise.name
            holder.itemView.setOnClickListener { onExerciseClicked.invoke(exercise) }


            // This view doesnt need to be shown on complete routine screen
            holder.itemView.exerciseSelectedCheckbox.isVisible = false
        }
    }

    fun setExercises(exercises : List<Exercise>) {
        this.exercisesInRoutine = exercises
        notifyDataSetChanged()
    }

    inner class CompleteRoutineViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.exercise_name)
    }
}