package com.buchanancreative.gymlogger.View

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.buchanancreative.gymlogger.Adapter.CreateRoutineAdapter
import com.buchanancreative.gymlogger.Adapter.ExerciseListAdapter
import com.buchanancreative.loggerlibrary.Interface.ExerciseClickListener
import com.buchanancreative.loggerlibrary.Interface.ExerciseFilterListener
import com.buchanancreative.loggerlibrary.Model.Exercise
import com.buchanancreative.gymlogger.View.ViewModel.CreateRoutineViewModel
import com.buchanancreative.gymlogger.R
import com.buchanancreative.gymlogger.Util.InjectorUtil
import com.buchanancreative.loggerlibrary.Util.Constants
import kotlinx.android.synthetic.main.complete_routine_fragment.*

import kotlinx.android.synthetic.main.create_routine_fragment.*
import kotlinx.android.synthetic.main.create_routine_fragment.routineName


// TODO this class needs to be updated to handle editing routines as well
// ...TODO for example saveRoutine creates a new routine, but it should take the id of an existing routine and get the
// ...TODO exercises in that routine, find some way to select the exercises, so the user can edit the routine

class CreateRoutineFragment: BaseFragment(), ExerciseFilterListener {
    private lateinit var viewModel: CreateRoutineViewModel

    val args: CreateRoutineFragmentArgs by navArgs()


    override fun filterReturnedNoResults() {
        TODO("Handle filter returning no results") //To change body of created functions use File | Settings | File Templates.
    }

    override fun filterReturnedResults() {
        TODO("handle filter results") //To change body of created functions use File | Settings | File Templates.
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.create_routine_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // if this is not null, then we are going to edit/update a routine, not create a new one
        val routineId = args.routineId
        
        val factory = InjectorUtil.provideCreateRoutineViewModeFactory(activity!!.application, routineId, this)
        viewModel = ViewModelProviders.of(this, factory).get(CreateRoutineViewModel::class.java)

        // Convert current method to local method that the user saves before leaving this screen (it will be more snappy), and no reason to observe something only they are editing
        routineName.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // NO op yet
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // no op yet
            }

            override fun afterTextChanged(name: Editable?) {
                viewModel.routineName = name.toString()
            }
        })


        save.setOnClickListener {
            viewModel.saveRoutine()
        }



        setupExerciseList()
    }

    // CHange these two so that there is only one list
    // A routine exercises list that displays all exercises, then the switch can be hit witch will switch
    //  to show only exercises that are currently in the routine
    private fun setupExerciseList() {
        exerciseListLoading.visibility = View.VISIBLE
        exerciseList.visibility = View.INVISIBLE

        val adapter = CreateRoutineAdapter(this) {
            viewModel.exerciseClicked(it)
        }

        val exercisesObserver = Observer<List<Exercise>> { exercises ->
            adapter.setExercises(exercises)

            exerciseListLoading.visibility = View.GONE
            exerciseList.visibility = View.VISIBLE
        }

        val inRoutineObserver = Observer<List<Exercise>> { exercises ->
            adapter.exercisesInRoutine = exercises
            if (onlySelectedExercises.isChecked) {
                adapter.notifyDataSetChanged()
            }
        }

        onlySelectedExercises.setOnCheckedChangeListener { compoundButton, isChecked ->
            adapter.onlyExercisesChanged(isChecked)
        }


        viewModel.inRoutineExercises.observe(viewLifecycleOwner, inRoutineObserver)
        viewModel.allExercises.observe(viewLifecycleOwner, exercisesObserver)

        exerciseList.adapter = adapter
        exerciseList.layoutManager = LinearLayoutManager(activity!!.applicationContext)
    }


}