package com.buchanancreative.gymlogger.View.LogFragments

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
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
import com.buchanancreative.gymlogger.Adapter.SavedTimeLogsAdapter
import com.buchanancreative.loggerlibrary.Model.TimeLog
import com.buchanancreative.gymlogger.View.ViewModel.TimeLogViewModel

import com.buchanancreative.gymlogger.R
import com.buchanancreative.gymlogger.Util.InjectorUtil
import kotlinx.android.synthetic.main.time_log_fragment.*

/**
 * Created by buchanancreative on 10/16/17.
 */

class TimeLogFragment: Fragment() {

    private lateinit var viewModel: TimeLogViewModel

    private var stopwatchMode: STOPWATCH_MODE? = null

    private var handler: Handler? = null
    internal var msTime: Long = 0
    internal var startTime: Long = 0
    internal var timeBuff: Long = 0
    internal var updateTime = 0L

    internal var seconds: Int = 0
    internal var minutes: Int = 0
    internal var milliseconds: Int = 0


    private lateinit var exerciseId: String
    private lateinit var exerciseName: String

    val args: TimeLogFragmentArgs by navArgs()


    private var runnable: Runnable? = null


    internal enum class STOPWATCH_MODE {
        RUNNING,
        STOPPED
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val factory = InjectorUtil.provideTimeLogViewModelFactory(activity!!.application, exerciseId)
        viewModel = ViewModelProviders.of(this, factory).get(TimeLogViewModel::class.java)


        saveButton.setOnClickListener {
            val durationInSeconds = convertMinutesSecondsToSeconds(minutes, seconds)

            exerciseId?.let {
                viewModel.save(it, exerciseName, durationInSeconds)
            }
        }

        setupSavedLogsView()

        nextExerciseButton.setOnClickListener {
            findNavController().popBackStack()
        }

        reset.setOnClickListener {
            msTime = 0L
            startTime = 0L
            timeBuff = 0L
            updateTime = 0L
            milliseconds = 0
            minutes = milliseconds
            seconds = minutes

            timer.text = resources.getString(R.string.zero_time)
        }
    }

    private fun setupSavedLogsView() {
        val adapter = SavedTimeLogsAdapter()

        val logsObserver = Observer<List<TimeLog>> {logs ->
            if (logs != null) {
                adapter.setLogs(logs)
            }
        }

        viewModel.getRecentTimeLogs().observe(this, logsObserver)
        savedLogs.adapter = adapter
        savedLogs.layoutManager = LinearLayoutManager(activity!!.applicationContext)
    }


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler = Handler()

        runnable = object : Runnable {
            override fun run() {
                msTime = SystemClock.uptimeMillis() - startTime

                updateTime = timeBuff + msTime

                seconds = (updateTime / 1000).toInt()

                minutes = seconds / 60

                seconds = seconds % 60

                milliseconds = updateTime.toInt() % 1000

                timer.text = ("" + minutes + ":"
                        + String.format("%02d", seconds) + ":"
                        + String.format("%03d", milliseconds))

                handler?.postDelayed(this, 0)
            }
        }

        stopwatchMode = STOPWATCH_MODE.STOPPED
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.time_log_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseId = args.exerciseId
        exerciseName = args.exerciseName
        exercise_name.text = args.exerciseName
    }

    override fun onResume() {
        super.onResume()

        start_stop_button.setOnClickListener {
            if (stopwatchMode == STOPWATCH_MODE.STOPPED) {
                handleRun()
            } else if (stopwatchMode == STOPWATCH_MODE.RUNNING) {
                handleStop()
            }
        }

        exercise_name.text = exerciseName
    }

    private fun handleRun() {
        startTime = SystemClock.uptimeMillis()
        handler?.postDelayed(runnable, 0)

        start_stop_button.setText(R.string.stop)
        stopwatchMode = STOPWATCH_MODE.RUNNING
    }

    private fun handleStop() {
        timeBuff += msTime
        handler?.removeCallbacks(runnable)

        start_stop_button.setText(R.string.start)
        if (seconds > 0) {
//            enableSaveButton()
        }
        stopwatchMode = STOPWATCH_MODE.STOPPED
    }

    private fun convertMinutesSecondsToSeconds(minutes: Int, seconds: Int): Int {
        return minutes * 60 + seconds
    }

    companion object {
        private val TAG = TimeLogFragment::class.java.name
    }
}
