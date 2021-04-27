package com.buchanancreative.gymlogger.View.Stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.NO_ID
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.buchanancreative.gymlogger.Adapter.LogStatsHistoryAdapter
import com.buchanancreative.gymlogger.View.ViewModel.LogStatsViewModel


import com.buchanancreative.gymlogger.R
import com.buchanancreative.loggerlibrary.Util.Constants
import com.buchanancreative.gymlogger.View.BaseFragment
import com.buchanancreative.gymlogger.View.MainActivity
import com.buchanancreative.loggerlibrary.Model.AggregateLogStat
import com.buchanancreative.loggerlibrary.Model.LogType
import kotlinx.android.synthetic.main.log_history_list_fragment.*
import kotlinx.android.synthetic.main.log_history_list_fragment.savedLogs
import java.util.*

/**
 * Created by buchanancreative on 11/21/17.
 */

class LogHistoryListFragment : BaseFragment() {

    private var logSetName: String? = null

    private lateinit var viewModel: LogStatsViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.log_history_list_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()



        (activity as MainActivity).showLoginPopupIfNotLoggedIn(getString(R.string.sign_in_to_view_history))
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupViews()
    }


    private fun setupViews() {

        loading.visibility = View.VISIBLE
        savedLogs.visibility = View.GONE

        viewModel = ViewModelProviders.of(this).get(LogStatsViewModel::class.java)
        val adapter = LogStatsHistoryAdapter(viewModel) { id, name, type ->

            type?.let {
                val logType = LogType.valueOf(type)
                val action = LogHistoryListFragmentDirections.actionLogHistoryListFragmentToLogGraphFragment(id, name, logType )
                findNavController().navigate(action)
             }
        }

        val logsObserver = Observer<List<AggregateLogStat>> { logs ->
            loading.visibility = View.GONE

            if (logs.isEmpty()) {
                noDataText.text = getString(R.string.no_data, viewModel.getCurrentSelectionText())
                noDataText.visibility = View.VISIBLE
            } else {
                noDataText.visibility = View.GONE
                savedLogs.visibility = View.VISIBLE

            }
            adapter.setLogs(logs ?: emptyList())
            adapter.onSelectionChanged()
        }

        logFilterSelection.setOnCheckedChangeListener { group, checkedId ->
            // A double click will result in checkedId being NO_ID, if allowed to run, this will crash the app
            if (checkedId != NO_ID) {
                val chip: Chip = group.findViewById(checkedId)


                viewModel.selection = chip
                viewModel.getCurrentSelectionLogs().observe(viewLifecycleOwner, logsObserver)
                adapter.onSelectionChanged()

                (activity as MainActivity).statsFromDate = viewModel.getDateForStatsSelection()
            }
        }

        logFilterSelection.check(twentyFourHourSelection.id)

        val list = viewModel.getCurrentSelectionLogs()
        viewModel.getCurrentSelectionLogs().observe(viewLifecycleOwner, logsObserver)
        savedLogs.adapter = adapter
        savedLogs.layoutManager = LinearLayoutManager(activity!!.applicationContext)
    }




}
