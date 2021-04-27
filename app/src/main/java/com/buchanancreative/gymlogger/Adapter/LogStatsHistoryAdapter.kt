package com.buchanancreative.gymlogger.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.buchanancreative.loggerlibrary.Model.AggregateLogStat
import com.buchanancreative.gymlogger.View.ViewModel.LogStatsViewModel
import com.buchanancreative.gymlogger.R
import java.util.*

enum class RowType {
    LOG,
    HEADER
}



class LogStatsHistoryAdapter(private val viewModel: LogStatsViewModel, var onLogClicked: (id: String, name: String?, type: String?) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var logList: List<AggregateLogStat>? = Collections.emptyList()

    companion object {
        private val TAG = LogStatsHistoryAdapter::class.java.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val inflatedView : View = layoutInflater.inflate(R.layout.stats_log_row, parent,false)

        val viewHolder = StatViewHolder(inflatedView)
        inflatedView.setOnClickListener(viewHolder)

        return viewHolder

    }

    override fun getItemCount(): Int {
        return logList?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        logList.let {
            val logRow = it!![position]

                val id = logRow.exerciseId
                val statText = "Completed " + logRow.count.toString() + " sets." //TODO handle single set
                val name = logRow.name

                (holder as StatViewHolder).exerciseId.text = name
                holder.exerciseAggregate.text = statText

        }
    }


    fun setLogs(logStats: List<AggregateLogStat>) {
        this.logList = logStats
        notifyDataSetChanged()
    }

    fun onSelectionChanged() {
        notifyDataSetChanged()
    }


    inner class StatViewHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        val exerciseId: TextView = view.findViewById(R.id.exerciseId)
        val exerciseAggregate: TextView = view.findViewById(R.id.exerciseAggregate)

        override fun onClick(p0: View?) {
            logList?.get(adapterPosition)?.let { logstat ->
                val exerciseId = logstat.exerciseId
                onLogClicked.invoke(exerciseId, logstat.name, logstat.type)
            }
        }
    }


}