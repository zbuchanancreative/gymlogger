package com.buchanancreative.gymlogger.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.buchanancreative.loggerlibrary.Model.WeightRepLog
import com.buchanancreative.gymlogger.R
import com.buchanancreative.loggerlibrary.Util.Constants
import com.buchanancreative.loggerlibrary.Util.Utility
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToLong

class SavedWeightRepsLogsAdapter : RecyclerView.Adapter<SavedWeightRepsLogsAdapter.SavedLogsViewHolder>() {

    private var logs: List<WeightRepLog>? = Collections.emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedLogsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.saved_barbell_log_row, parent, false)
        return SavedLogsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return logs?.size ?: 0
    }

    fun setLogs(logs: List<WeightRepLog>) {
        this.logs = logs
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SavedLogsViewHolder, position: Int) {
        logs?.let {
            val log = it[position]

            holder.weight.text = Utility.getConvertedKgToLbsValue(log.weight)
            holder.reps.text = log.reps.toString()
            holder.row.text = (position + 1).toString() + "."
        }
    }


    inner class SavedLogsViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val weight: TextView = view.findViewById(R.id.weight)
        val reps: TextView = view.findViewById(R.id.reps)
        val row: TextView = view.findViewById(R.id.row)
    }
}


