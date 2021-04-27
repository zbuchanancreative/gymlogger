package com.buchanancreative.gymlogger.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.buchanancreative.gymlogger.R
import com.buchanancreative.gymlogger.View.ViewModel.LogTableRowData
import java.util.*
import kotlin.math.log

class LogTableAdapter(val onDelete: (logId: String) -> Unit) : RecyclerView.Adapter<LogTableAdapter.LogTableViewHolder>() {

    private var logs: List<LogTableRowData>? = Collections.emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogTableViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.log_table_row, parent, false)
        return LogTableViewHolder(view)
    }

    override fun getItemCount(): Int {
        return logs?.size ?: 0
    }

    override fun onBindViewHolder(holder: LogTableViewHolder, position: Int) {
        logs?.let { logs ->
            val log = logs[position]
            holder.title.text = log.title
            holder.timeInfo.text = log.dateOrTime
            holder.delete.setOnClickListener {
                log.logId?.let {
                    onDelete.invoke(it)
                }
            }
        }
    }

    fun setLogs(logs: List<LogTableRowData>) {
        this.logs = logs
        notifyDataSetChanged()
    }

    inner class LogTableViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val timeInfo: TextView = view.findViewById(R.id.timeInfo)
        val delete: ImageButton = view.findViewById(R.id.delete)
    }
}