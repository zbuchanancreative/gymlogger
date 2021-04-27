package com.buchanancreative.gymlogger.Adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView

import com.buchanancreative.loggerlibrary.Interface.ExerciseClickListener
import com.buchanancreative.loggerlibrary.Interface.ExerciseFilterListener
import com.buchanancreative.loggerlibrary.Model.Exercise
import com.buchanancreative.gymlogger.R
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by buchanancreative on 10/10/17.
 */

class ExerciseListAdapter(private val clickListener: ExerciseClickListener, private val filterListener: ExerciseFilterListener) : RecyclerView.Adapter<ExerciseListAdapter.ExerciseListViewHolder>(), Filterable {

    companion object {
        private val TAG = ExerciseListAdapter::class.java.name
    }

    private var exercises: List<Exercise>? = Collections.emptyList()
    private var exercisesToFilter: List<Exercise>? = Collections.emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_list_row, parent, false)
        return ExerciseListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseListViewHolder, position: Int) {
        exercises?.let {
            val exercise = it[position]

            holder.exerciseName.text = exercise.name
            holder.itemView.setOnClickListener { clickListener.onExerciseClicked(exercise) }
        }

    }


    override fun getItemCount(): Int {
        return exercises?.size ?: 0
    }

    fun setExercises(exercises : List<Exercise>) {
        this.exercises = exercises
        this.exercisesToFilter = exercises
        notifyDataSetChanged()
    }

    inner class ExerciseListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val exerciseName: TextView = view.findViewById<View>(R.id.exercise_name) as TextView
    }



    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val exercises = ArrayList(exercisesToFilter)
                if (constraint == null || constraint.isEmpty()) {
                    results.values = exercises
                    results.count = exercises.size
                } else {
                    val filteredResults = getFilteredResults(constraint)
                    results.values = filteredResults
                    results.count = filteredResults.size
                }

                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                if (results.count != 0) {
                    try {
                        /// TODO figure out how to cast this correctly on editing a search???
                        exercises =  results.values as? ArrayList<Exercise>
                        notifyDataSetChanged()
                        filterListener.filterReturnedResults()
                    } catch (error: Exception) {
                        Log.e(TAG, error.toString())
                        exercises = exercisesToFilter
                    }

                } else {
                    exercises = null
                    filterListener.filterReturnedNoResults()
                    notifyDataSetChanged()
                }
            }

            private fun getFilteredResults(constraint: CharSequence?): ArrayList<Exercise> {
                val listResult = ArrayList<Exercise>()

                val exercises = this@ExerciseListAdapter.exercisesToFilter?: return ArrayList()

                for (exercise in exercises) {
                    constraint?.let {
                        if (doesExerciseSatisfyConstraint(exercise, constraint)) {
                            listResult.add(exercise)
                        }
                    }
                }

                return listResult
            }

            private fun doesExerciseSatisfyConstraint(exercise: Exercise, constraint: CharSequence): Boolean {
                return exercise.name.toUpperCase().startsWith(constraint.toString().toUpperCase())
            }
        }

    }
}
