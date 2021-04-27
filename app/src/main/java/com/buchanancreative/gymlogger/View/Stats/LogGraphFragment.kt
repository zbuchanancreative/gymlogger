package com.buchanancreative.gymlogger.View.Stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.buchanancreative.gymlogger.Adapter.LogTableAdapter
import com.github.mikephil.charting.components.XAxis
import com.buchanancreative.gymlogger.View.ViewModel.LogGraphViewModel
import com.buchanancreative.gymlogger.R
import com.buchanancreative.gymlogger.Util.InjectorUtil
import com.buchanancreative.loggerlibrary.Util.Constants
import com.buchanancreative.gymlogger.View.BaseFragment
import com.buchanancreative.gymlogger.View.MainActivity
import com.github.mikephil.charting.data.LineData
import kotlinx.android.synthetic.main.log_graph_fragment.*
import com.buchanancreative.gymlogger.View.ViewModel.DateValueFormatter
import com.buchanancreative.loggerlibrary.Model.LogType
import java.util.*


class LogGraphFragment: BaseFragment() {

    lateinit var viewModel: LogGraphViewModel

    val args: LogGraphFragmentArgs by navArgs()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.log_graph_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = activity?.application ?: return

        val factory = InjectorUtil.provideLogGraphViewModelFactory(application,this)
        viewModel = ViewModelProviders.of(this, factory).get(LogGraphViewModel::class.java)
        
        viewModel.exerciseId = args.exerciseId
        viewModel.exerciseName = args.exerciseName
        viewModel.exerciseType = args.logType

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        setupViews()
    }

    override fun onResume() {
        super.onResume()


    }

    private fun setupViews() {
        exercise_name.text = viewModel.exerciseName

        val dataFromDate = (activity as MainActivity).statsFromDate ?: return

        // TODO the view model should tell us whether to setup a chart for multiple days, or a table for one day
        setupChart(dataFromDate)

        setupTable(dataFromDate)

        graphViewSelection.setOnClickListener {
            table.visibility = View.GONE
            chart.visibility = View.VISIBLE
        }

        tableViewSelection.setOnClickListener {
            chart.visibility = View.GONE
            table.visibility = View.VISIBLE
        }

        val firstDataPointObserver = Observer<String> {  value ->
            firstDataPoint.text = viewModel.firstDataPointValue.value
            firstDataPointLayout.visibility = View.VISIBLE
        }

        viewModel.firstDataPointValue.observe(this, firstDataPointObserver)
        firstDataPointLabel.text = viewModel.firstDataPointLabel


        val secondDataPointObserver = Observer<String> { value ->
            secondDataPoint.text = viewModel.secondDataPointValue.value
            secondDataPointLayout.visibility = View.VISIBLE
        }
        viewModel.secondDataPointValue.observe(this, secondDataPointObserver)
        secondDataPointLabel.text = viewModel.secondDataPointLabel


        val thirdDataPointObserver = Observer<String> { value ->
            thirdDataPoint.text = viewModel.thirdDataPointValue.value
            thirdDataPointLayout.visibility = View.VISIBLE
        }
        viewModel.thirdDataPointValue.observe(this, thirdDataPointObserver)
        thirdDataPointLabel.text = viewModel.thirdDataPointLabel
    }


    private fun setupChart(dataFromDate: Date) {
        dataPointLayout.visibility = View.GONE
        tableAndGraphLayout.visibility = View.GONE
        loading.visibility = View.VISIBLE

        viewModel.getLineData(dataFromDate).observe(this, Observer<LineData> {lineData ->
            // TODO figure out if there are multiple days of data outside of this data call
            dataRange.text = viewModel.getDataRangeString(dataFromDate)

            chart.data = lineData
            if (viewModel.multipleDaysOfData) {
                chart.xAxis.valueFormatter = DateValueFormatter()
                chart.xAxis.spaceMin = 5f
            } else {
                chart.xAxis.axisMinimum = 0F
                chart.xAxis.spaceMin = 1f
                chart.xAxis.granularity = 1f
            }

            chart.invalidate() // refresh

            dataPointLayout.visibility = View.VISIBLE
            tableAndGraphLayout.visibility = View.VISIBLE
            loading.visibility = View.GONE
        })

        chart.axisLeft.setDrawGridLines(false)
        chart.axisRight.isEnabled = false
        chart.xAxis.setDrawGridLines(false)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.description.isEnabled = false
    }


    private fun setupTable(dataFromDate: Date) {

        val onDelete = { logId: String ->
            viewModel.deleteLog(logId)
        }

        val adapter = LogTableAdapter(onDelete)
        table.adapter = adapter
        table.layoutManager = LinearLayoutManager(activity!!.applicationContext)


        viewModel.getTableData(dataFromDate).observe(this, Observer { tableData ->
            adapter.setLogs(tableData)
        })
    }



}