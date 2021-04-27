package com.buchanancreative.gymlogger.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.buchanancreative.loggerlibrary.Model.RepLog
import com.buchanancreative.gymlogger.R
import java.util.*

class SavedRepLogsAdapter: RecyclerView.Adapter<SavedRepLogsAdapter.SavedRepLogsViewHolder>() {
    private var logs: List<RepLog>? = Collections.emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRepLogsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.saved_rep_log_row, parent, false)
        return SavedRepLogsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return logs?.size ?: 0
    }

    override fun onBindViewHolder(holder: SavedRepLogsViewHolder, position: Int) {
        logs?.let {
            val log = it[position]

            holder.reps.text = log.reps.toString()
            holder.row.text = (position + 1).toString() + "."
        }
    }

    fun setLogs(logs: List<RepLog>) {
        this.logs = logs
        notifyDataSetChanged()
    }


    inner class SavedRepLogsViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val reps: TextView = view.findViewById(R.id.reps)
        val row: TextView = view.findViewById(R.id.row)
    }
}

