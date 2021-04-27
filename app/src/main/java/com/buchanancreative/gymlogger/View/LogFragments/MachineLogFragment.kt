package com.buchanancreative.gymlogger.View.LogFragments

import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.buchanancreative.gymlogger.Adapter.SavedWeightRepsLogsAdapter
import com.buchanancreative.loggerlibrary.Model.WeightRepLog
import com.buchanancreative.gymlogger.View.ViewModel.WeightRepsLogViewModel

import com.buchanancreative.gymlogger.R
import com.buchanancreative.gymlogger.Util.InjectorUtil
import kotlinx.android.synthetic.main.machine_log_fragment.*

/**
 * Created by buchanancreative on 10/16/17.
 */

class MachineLogFragment : Fragment() {

    private lateinit var exerciseId: String
    private lateinit var exerciseName: String

    val args: MachineLogFragmentArgs by navArgs()

    private lateinit var viewModel: WeightRepsLogViewModel

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.machine_log_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = InjectorUtil.provideWeightRepsLogViewModelFactory(activity!!.application, exerciseId)
        viewModel = ViewModelProviders.of(this, factory).get(WeightRepsLogViewModel::class.java)

        saveButton.setOnClickListener {
            viewModel.save(weight_control.value, reps_control.value, exerciseId, exerciseName)
        }

        setupSavedLogsDisplay()


        reps_control.setLabel(getString(R.string.reps))
        weight_control.setLabel(getString(R.string.lbs))

        nextExerciseButton.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseId = args.exerciseId
        exerciseName = args.exerciseName
        exercise_name.text = args.exerciseName
    }

    private fun setupSavedLogsDisplay() {
        val adapter = SavedWeightRepsLogsAdapter()

        val logsObserver = Observer<List<WeightRepLog>> { logs ->
            if (logs != null) {
                adapter.setLogs(logs)
            }
        }

        viewModel.getRecentWeightRepLogs().observe(this, logsObserver)
        savedMachineLogs.adapter = adapter
        savedMachineLogs.layoutManager = LinearLayoutManager(activity!!.applicationContext)
    }

    companion object {

        private val TAG = MachineLogFragment::class.java.name

        private val MULTIPLIER = 5
    }
}
