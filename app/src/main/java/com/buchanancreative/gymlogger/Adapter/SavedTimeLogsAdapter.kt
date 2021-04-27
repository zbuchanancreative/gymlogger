package com.buchanancreative.gymlogger.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.buchanancreative.loggerlibrary.Model.TimeLog
import com.buchanancreative.gymlogger.R
import java.util.*


class SavedTimeLogsAdapter: RecyclerView.Adapter<SavedTimeLogsAdapter.SavedTimeLogsViewHolder>() {

    private var logs: List<TimeLog>? = Collections.emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedTimeLogsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.saved_time_log_row, parent, false)
        return SavedTimeLogsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return logs?.size ?: 0
    }

    override fun onBindViewHolder(holder: SavedTimeLogsViewHolder, position: Int) {
        logs?.let {
            val log = it[position]

            holder.duration.text = log.duration.toString()
            holder.row.text = (position + 1).toString() + "."
        }
    }

    fun setLogs(logs: List<TimeLog>) {
        this.logs = logs
        notifyDataSetChanged()
    }

    inner class SavedTimeLogsViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val duration: TextView = view.findViewById(R.id.duration)
        val row: TextView = view.findViewById(R.id.row)
    }
}