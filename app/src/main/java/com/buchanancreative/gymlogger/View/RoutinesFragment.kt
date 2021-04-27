package com.buchanancreative.gymlogger.View

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.buchanancreative.gymlogger.Adapter.RoutinesAdapter
import com.buchanancreative.loggerlibrary.Interface.RoutinesListener
import com.buchanancreative.loggerlibrary.Interface.RoutineSelectedListener
import com.buchanancreative.loggerlibrary.Model.Routine
import com.buchanancreative.gymlogger.View.ViewModel.RoutinesViewModel

import com.buchanancreative.gymlogger.R
import com.buchanancreative.loggerlibrary.Interface.AccountListener
import kotlinx.android.synthetic.main.home_fragment.*

/**
 * Created by buchanancreative on 10/31/17.
 */

class RoutinesFragment : BaseFragment() {
    private lateinit var viewModel: RoutinesViewModel
    private lateinit var adapter: RoutinesAdapter
    private lateinit var accountListener: AccountListener

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).showLoginPopupIfNotLoggedIn(getString(R.string.sign_in_routines))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addRoutineButton.setOnClickListener {
            val action = RoutinesFragmentDirections.actionRoutinesFragmentToCreateRoutineFragment(null)
            view.findNavController().navigate(action)
        }

        viewModel = ViewModelProviders.of(this).get(RoutinesViewModel::class.java)

        editRoutines.setOnCheckedChangeListener { compoundButton, isChecked ->
//            adapter.isEditing(compoundButton.isChecked) // TODO Support editing Routine
            Toast.makeText(context, "Editing Routines not supported yet", Toast.LENGTH_LONG).show()
        }

        signIn.setOnClickListener {
            accountListener.onAccountClicked()
        }

        setupRoutinesList()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        accountListener = activity as AccountListener
    }

    private fun setupRoutinesList() {

        loading.visibility = View.VISIBLE
        routinesList.visibility = View.GONE

        val onSelected = { routineId: String, routineName: String ->
            val action = RoutinesFragmentDirections.actionRoutinesFragmentToCompleteRoutineFragment(routineId, routineName)
            findNavController().navigate(action)
        }

        val onEdit = { routineId: String ->
            val action = RoutinesFragmentDirections.actionRoutinesFragmentToCreateRoutineFragment(routineId)
            findNavController().navigate(action)
        }

        val onDelete = { routine: Routine ->
            viewModel.deleteRoutine(routine)
        }

        adapter = RoutinesAdapter (onSelected, onEdit, onDelete)

        val observer  = Observer<List<Routine>> { routines ->
            adapter.setRoutines(routines)

            loading.visibility = View.GONE
            routinesList.visibility = View.VISIBLE
        }

        viewModel.allRoutines.observe(viewLifecycleOwner, observer)

        routinesList.adapter = adapter
        routinesList.layoutManager = LinearLayoutManager(activity!!.applicationContext)
    }
}
