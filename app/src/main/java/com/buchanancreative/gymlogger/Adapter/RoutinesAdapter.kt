package com.buchanancreative.gymlogger.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import com.buchanancreative.loggerlibrary.Model.Routine
import com.buchanancreative.gymlogger.R
import java.util.*

class RoutinesAdapter(val onRoutineClicked: (id: String, name: String) -> Unit, val onEdit: (id: String) -> Unit, val onDelete: (routine: Routine) -> Unit): RecyclerView.Adapter<RoutinesAdapter.RoutinesViewHolder>() {

    var isEditing = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutinesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.routine_row, parent, false)
        return RoutinesViewHolder(view)
    }

    private var routines: List<Routine>? = Collections.emptyList()

    override fun getItemCount(): Int {
        return routines?.size ?: 0
    }

    override fun onBindViewHolder(holder: RoutinesViewHolder, position: Int) {
        routines?.let {
            val routine = it[position]

            val routineId = routine.id ?: return // this is why not having the id on the routine makes it not clickable

            holder.routineName.text = routine.name
            holder.itemView.setOnClickListener { onRoutineClicked.invoke(routineId, routine.name) }
            holder.edit.setOnClickListener { onEdit.invoke(routineId)}
            holder.delete.setOnClickListener { onDelete.invoke(routine) }

            if (isEditing) {
                holder.edit.visibility = View.VISIBLE
                holder.delete.visibility = View.VISIBLE
            } else {
                holder.edit.visibility = View.GONE
                holder.delete.visibility = View.GONE
            }
        }
    }

    fun isEditing(isEditing: Boolean) {
        this.isEditing = isEditing
        notifyDataSetChanged()
    }

    inner class RoutinesViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val routineName: TextView = view.findViewById<View>(R.id.routineName) as TextView
        val edit: ImageButton = view.findViewById(R.id.edit) as ImageButton
        val delete: ImageButton = view.findViewById(R.id.delete) as ImageButton
    }

    fun setRoutines(routines: List<Routine>) {
        this.routines = routines
        notifyDataSetChanged()
    }

}