package com.buchanancreative.gymlogger.View.LogFragments

import android.os.Bundle
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
import com.buchanancreative.gymlogger.Adapter.SavedRepLogsAdapter
import com.buchanancreative.gymlogger.View.ViewModel.BodyweightLogViewModel
import com.buchanancreative.loggerlibrary.Model.RepLog


import com.buchanancreative.gymlogger.R
import com.buchanancreative.gymlogger.Util.InjectorUtil
import kotlinx.android.synthetic.main.bodyweight_log_fragment.*

/**
 * Created by buchanancreative on 10/16/17.
 */

class BodyweightLogFragment : Fragment() {

    private lateinit var viewModel: BodyweightLogViewModel

    private lateinit var exerciseId: String
    private lateinit var exerciseName: String

    val args: BodyweightLogFragmentArgs by navArgs()


    companion object {
        private val TAG = BodyweightLogFragment::class.java.name
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bodyweight_log_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        exerciseId = args.exerciseId
        exerciseName = args.exerciseName

        exercise_name.text = exerciseName


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val application = activity?.application ?: return
        val exerciseId = exerciseId ?: return

        val viewModelFactory = InjectorUtil.provideBodyweightLogViewModelFactory(application, exerciseId)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BodyweightLogViewModel::class.java)




        reps_control.setLabel(getString(R.string.reps))

        setupControls()
        setupSavedLogsDisplay()
    }

    private fun setupSavedLogsDisplay() {
        val adapter = SavedRepLogsAdapter()

        val logsObserver = Observer<List<RepLog>> { logs ->
            logs?.let {
                adapter.setLogs(logs)
            }
        }

        viewModel.getRecentLogs(exerciseId).observe(this, logsObserver)
        savedBodyweightLogs.adapter = adapter
        savedBodyweightLogs.layoutManager = LinearLayoutManager(activity!!.applicationContext)
    }


    private fun setupControls() {
        saveButton.setOnClickListener {

            viewModel.save(reps_control.value, exerciseId, exerciseName)
        }

        nextExerciseButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}
