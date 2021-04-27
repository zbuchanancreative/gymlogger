package com.buchanancreative.gymlogger.View

import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.buchanancreative.gymlogger.Adapter.ExerciseListAdapter
import com.buchanancreative.gymlogger.Model.ViewModel.ExerciseViewModelFactory
import com.buchanancreative.loggerlibrary.Interface.ExerciseClickListener
import com.buchanancreative.loggerlibrary.Interface.ExerciseFilterListener
import com.buchanancreative.loggerlibrary.Model.Exercise
import com.buchanancreative.gymlogger.View.ViewModel.ExerciseViewModel
import com.buchanancreative.gymlogger.R
import com.buchanancreative.loggerlibrary.Interface.AccountListener
import com.buchanancreative.loggerlibrary.Model.ExerciseType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.exercises_fragment.*
import kotlinx.android.synthetic.main.exercises_fragment.search_view

/**
 * Created by buchanancreative on 10/10/17.
 */

class ExercisesFragment : BaseFragment(), ExerciseClickListener, ExerciseFilterListener {

    private var exerciseClickListener: ExerciseClickListener? = null
    private lateinit var viewModel: ExerciseViewModel
    private lateinit var accountListener: AccountListener

    private fun isUserSignedIn() = FirebaseAuth.getInstance().currentUser != null


    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.exercises_fragment, container, false)
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory =  ExerciseViewModelFactory(activity!!.application, this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ExerciseViewModel::class.java)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        accountListener = activity as AccountListener

        signIn.setOnClickListener {
            accountListener.onAccountClicked()
        }
    }

    override fun onResume() {
        super.onResume()

        loading.visibility = View.VISIBLE
        exercise_list.visibility = View.GONE

        setupExerciseList()
        setupSearchView()
    }

    private fun setupExerciseList() {
        val adapter = ExerciseListAdapter(this, this)
        val exercisesObserver = Observer<List<Exercise>> { exercises ->
            adapter.setExercises(exercises)
            loading.visibility = View.GONE
            exercise_list.visibility = View.VISIBLE

        }
        viewModel.allExercises.observe(this, exercisesObserver)

        exercise_list.adapter = adapter
        exercise_list.layoutManager = LinearLayoutManager(activity!!.applicationContext)
    }

    private fun setupSearchView() {
        search_view.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterResults(newText)
                return false
            }
        })
    }

    override fun onExerciseClicked(exercise: Exercise) {
        if(!isUserSignedIn()) {
            showSignInDialog(getString(R.string.sign_in_to_log_exercises))
            return
        }

        val name = exercise.name
        val id = exercise.id

        val action = when(exercise.type) {
            "barbell" -> ExercisesFragmentDirections.actionExercisesFragmentToBarbellLogFragment(exercise.id, exercise.name, ExerciseType.Barbell)
            "time" -> ExercisesFragmentDirections.actionExercisesFragmentToTimeLogFragment(name, id)
            "bodyweight" -> ExercisesFragmentDirections.actionExercisesFragmentToBodyweightLogFragment(id, name)
            "freeweight" -> ExercisesFragmentDirections.actionExercisesFragmentToFreeweightLogFragment(exercise.name, exercise.id)
            "machine" -> ExercisesFragmentDirections.actionExercisesFragmentToMachineLogFragment(name, id)
            else -> return
        }

        findNavController().navigate(action)
        exerciseClickListener?.onExerciseClicked(exercise)
        search_view.setQuery("", false)
        search_view.clearFocus()
    }

    private fun showSignInDialog(message: String) {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage(message)
                .setPositiveButton("Sign in") { _, _ ->
                    accountListener.onAccountClicked()
                }
                .setNegativeButton(R.string.cancel,null)
        builder.create().show()
    }

    private fun filterResults(text: String) {
        val filter = (exercise_list.adapter as ExerciseListAdapter).filter
        filter.filter(text)
    }

    override fun filterReturnedNoResults() {
        exercise_list?.visibility = View.GONE
        no_results_layout?.visibility = View.VISIBLE
    }

    override fun filterReturnedResults() {
        exercise_list?.visibility = View.VISIBLE
        no_results_layout?.visibility = View.GONE
    }

    companion object {
        private val TAG = ExercisesFragment::class.java.name
    }
}


