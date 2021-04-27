package com.buchanancreative.gymlogger.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.buchanancreative.gymlogger.Adapter.CompleteRoutineAdapter
import com.buchanancreative.gymlogger.View.ViewModel.CompleteRoutineViewModel
import com.buchanancreative.gymlogger.R
import com.buchanancreative.gymlogger.Util.InjectorUtil
import com.buchanancreative.loggerlibrary.Model.Exercise
import com.buchanancreative.loggerlibrary.Model.ExerciseType
import kotlinx.android.synthetic.main.complete_routine_fragment.*

class CompleteRoutineFragment: BaseFragment() {

    private var routineId: String? = null

    private lateinit var viewModel: CompleteRoutineViewModel

    val args: CompleteRoutineFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.complete_routine_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        routineId = args.routineId
        routineName.text = args.routineName


        if (routineId.isNullOrEmpty()) {
            Toast.makeText(context, getString(R.string.routine_id_missing), Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
            return
        }

        val viewModelFactory = InjectorUtil.provideCompleteRoutineViewModelFactory(activity!!.application, routineId!!)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CompleteRoutineViewModel::class.java)


        setupExercisesInRoutineList()

        finish.setOnClickListener {
            findNavController().popBackStack()
        }


    }


    fun setupExercisesInRoutineList() {
        val adapter = CompleteRoutineAdapter(viewModel) { exercise ->
            val name = exercise.name
            val id = exercise.id

            val action = when(exercise.type) {
                "barbell" -> CompleteRoutineFragmentDirections.actionCompleteRoutineFragmentToBarbellLogFragment(id, name, ExerciseType.Barbell)
                "time"-> CompleteRoutineFragmentDirections.actionCompleteRoutineFragmentToTimeLogFragment(name, id)
                "bodyweight" -> CompleteRoutineFragmentDirections.actionCompleteRoutineFragmentToBodyweightLogFragment(id, name)
                "freeweight" -> CompleteRoutineFragmentDirections.actionCompleteRoutineFragmentToFreeweightLogFragment(exercise.name, exercise.id)
                "machine" -> CompleteRoutineFragmentDirections.actionCompleteRoutineFragmentToMachineLogFragment(name, id)
                else -> CompleteRoutineFragmentDirections.actionCompleteRoutineFragmentToBarbellLogFragment(id, name, ExerciseType.Barbell)
            }

            findNavController().navigate(action)
        }

        val observer = Observer<List<Exercise>> { exercises ->
            adapter.setExercises(exercises)
        }

        viewModel.exerciseIdsInRoutine?.observe(this, observer)

        exercisesInRoutine.adapter = adapter
        exercisesInRoutine.layoutManager = LinearLayoutManager(activity!!.applicationContext)
    }
}