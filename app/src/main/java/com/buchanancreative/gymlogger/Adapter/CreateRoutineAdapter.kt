package com.buchanancreative.gymlogger.Adapter

import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.buchanancreative.gymlogger.View.ViewModel.CreateRoutineViewModel
import com.buchanancreative.gymlogger.R
import com.buchanancreative.loggerlibrary.Interface.ExerciseClickListener
import com.buchanancreative.loggerlibrary.Interface.ExerciseFilterListener
import com.buchanancreative.loggerlibrary.Model.Exercise
import java.util.*


// TODO add method to convert exercises already in routine to positions in exercise list
class CreateRoutineAdapter(private val filterListener: ExerciseFilterListener, private val onExerciseClicked: (exercise: Exercise) -> Unit): RecyclerView.Adapter<CreateRoutineAdapter.CreateRoutineViewHolder>() {

    private var exercises: List<Exercise>? = Collections.emptyList()

    private var selected = SparseBooleanArray()

    private var onlyExercisesInRoutine: Boolean = false

    var exercisesInRoutine = Collections.emptyList<Exercise>()

    //id of exercise, position in original list
    var selectedExerciseIndexInFullList = mutableMapOf<String, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateRoutineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.in_routine_row, parent, false)
        return CreateRoutineViewHolder(view)
    }

    override fun getItemCount(): Int {
        return when {
            onlyExercisesInRoutine -> exercisesInRoutine.size
            else -> exercises?.size ?: 0
        }
    }

    override fun onBindViewHolder(holder: CreateRoutineViewHolder, position: Int) {
        if (onlyExercisesInRoutine) {

            holder.name.text = exercisesInRoutine[position].name

            holder.inRoutine.setOnCheckedChangeListener(null)

            holder.inRoutine.isChecked = true

            Log.i("test", "position: " + position)

            holder.inRoutine.setOnCheckedChangeListener { compoundButton, b ->
                // Maybe set original position as part of data in list???
                // remove exercise from selected
                // remove exercise from view model
                val exercise = exercisesInRoutine[position]

                val positionInFulllist = selectedExerciseIndexInFullList[exercise.id]
                selectedExerciseIndexInFullList.remove(exercise.id)

                selected.delete(positionInFulllist!!)
                onExerciseClicked.invoke(exercise)

            }

        } else {
            exercises?.let {
                val exercise = it[position]

                holder.name.text = exercise.name

                holder.inRoutine.setOnCheckedChangeListener(null)

                val isSelected = selected[position]

                holder.inRoutine.isChecked = isSelected

                holder.inRoutine.setOnCheckedChangeListener { compoundButton, isChecked ->
                    if ((!isChecked && isSelected) || (isChecked && !isSelected)) {
                        handleClick(position, exercise)
                    }
                }


            }
        }
    }

    fun handleClick(position: Int, exercise: Exercise) {
        val isAlreadySelected = selected[position]

        Log.i("TAG", "clicked")
        if (isAlreadySelected) {
            selected.delete(position)
            selectedExerciseIndexInFullList.remove(exercise.id)
        } else {
            selected.append(position, true)
            selectedExerciseIndexInFullList[exercise.id] = position
        }
        onExerciseClicked.invoke(exercise)
    }

    fun onlyExercisesChanged(onlyInRoutine: Boolean) {


        onlyExercisesInRoutine = onlyInRoutine

        notifyDataSetChanged()


    }


    fun setExercises(exercises : List<Exercise>) {
        this.exercises = exercises
        notifyDataSetChanged()
    }

    inner class CreateRoutineViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.exercise_name)
        var inRoutine: CheckBox = view.findViewById(R.id.exerciseSelectedCheckbox)
    }
}