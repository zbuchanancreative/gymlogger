package com.buchanancreative.gymlogger.View.LogFragments

import android.os.Bundle
import androidx.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.buchanancreative.gymlogger.Adapter.SavedWeightRepsLogsAdapter
import com.buchanancreative.loggerlibrary.Model.WeightRepLog
import com.buchanancreative.gymlogger.View.ViewModel.FreeweightLogViewModel

import com.buchanancreative.gymlogger.R
import com.buchanancreative.gymlogger.Util.InjectorUtil
import com.buchanancreative.gymlogger.View.ViewModel.FreeweightLogViewModelFactory
import kotlinx.android.synthetic.main.freeweight_log_fragment.*

/**
 * Created by buchanancreative on 10/16/17.
 */

class FreeweightLogFragment: Fragment() {

    private lateinit var viewModel: FreeweightLogViewModel

    protected lateinit var exerciseId: String
    protected lateinit var exerciseName: String

    val args: FreeweightLogFragmentArgs by navArgs()

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.freeweight_log_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // This needs to be done before anything tries to use the view model
        val application = activity?.application ?: return
        val exerciseId = exerciseId ?: return
        val viewModelFactory = InjectorUtil.provideFreeweightLogViewModelFactory(application, exerciseId)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FreeweightLogViewModel::class.java)


        reps_control.setLabel(getString(R.string.reps))
        weight_control.setLabel(getString(R.string.lbs))

        setupControls()

        setupFreeweightLogsView()

        exercise_name.text = exerciseName
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseId = args.exerciseId
        exerciseName = args.exerciseName
    }

    private fun setupControls() {
        saveButton.setOnClickListener {
            viewModel.save(weight_control.value, reps_control.value, exerciseId, exerciseName ?: "")
        }

        nextExerciseButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupFreeweightLogsView() {
        val adapter = SavedWeightRepsLogsAdapter()

        val logsObserver = Observer<List<WeightRepLog>> {logs ->
            logs?.let {
                adapter.setLogs(it)
            }
        }

        viewModel.getRecentLogs(exerciseId).observe(viewLifecycleOwner, logsObserver)
        savedFreeweightLogs.adapter = adapter
        savedFreeweightLogs.layoutManager = LinearLayoutManager(activity!!.applicationContext)
    }



}
