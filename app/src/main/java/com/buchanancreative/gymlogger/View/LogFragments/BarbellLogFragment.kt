package com.buchanancreative.gymlogger.View.LogFragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.annotation.Nullable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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
import com.buchanancreative.loggerlibrary.Model.ExerciseType
import com.buchanancreative.loggerlibrary.Util.Constants.DEFAULT_BAR_WEIGHT
import kotlinx.android.synthetic.main.barbell_control.*

import kotlinx.android.synthetic.main.barbell_log_fragment.*


/**
 * Created by buchanancreative on 10/12/17.
 */

class BarbellLogFragment : Fragment() {

    private lateinit var viewModel: WeightRepsLogViewModel

    private lateinit var exerciseId: String
    private lateinit var exerciseName: String
    private lateinit var exerciseType: ExerciseType

    val args: BarbellLogFragmentArgs by navArgs()

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.barbell_log_fragment, container, false)
    }

    private fun setupControls() {
        saveButton.setOnClickListener {
            val weight = totalWeight.text.toString().toDoubleOrNull()

            weight?.let { weightTotal ->
                viewModel.save(weightTotal, repControl.value, exerciseId, exerciseName)
            }
        }

        repControl.setLabel(getString(R.string.reps))


        barbellControll.onWeightUpdateListener = {
            totalWeight.setText(barbellControll.weightSum.toString())
        }

        nextExerciseButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseId = args.exerciseId
        exerciseName = args.exerciseName
        exerciseType = args.exerciseType
    }

    override fun onResume() {
        super.onResume()

        totalWeight.setText(DEFAULT_BAR_WEIGHT.toString())

        setupControls()

        setupSavedBarbellLogsView()

        exercise_name.text = exerciseName

    }

    private fun setupSavedBarbellLogsView() {
        val adapter = SavedWeightRepsLogsAdapter()

        val factory = InjectorUtil.provideWeightRepsLogViewModelFactory(activity!!.application, exerciseId)
        viewModel = ViewModelProviders.of(this, factory).get(WeightRepsLogViewModel::class.java)
        val logsObserver = Observer<List<WeightRepLog>> { logs ->
            if (logs != null) {
                adapter.setLogs(logs)
            }
        }

        viewModel.getRecentWeightRepLogs().observe(this, logsObserver)
        savedLogs.adapter = adapter
        savedLogs.layoutManager = LinearLayoutManager(activity!!.applicationContext)
    }
}
