package com.buchanancreative.gymlogger.View.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.buchanancreative.gymlogger.LoggerApp
import com.buchanancreative.loggerlibrary.Model.Routine
import com.buchanancreative.loggerlibrary.Persistence.FirebaseLoggingRepository
import com.buchanancreative.loggerlibrary.Persistence.LoggingRepository
import com.buchanancreative.loggerlibrary.Persistence.RoomLoggingRepository
import java.util.logging.Logger

class RoutinesViewModel(application: Application): AndroidViewModel(application) {
    private val loggingRepository: LoggingRepository = FirebaseLoggingRepository((application as LoggerApp).getUserId())

    internal var allRoutines: LiveData<List<Routine>>

    internal var exercisesInRoutine: LiveData<List<Int>>? = null


    var selectedRoutine: Int? = null

    init {
        allRoutines = loggingRepository.getAllRoutines()
    }

    fun deleteRoutine(routine: Routine) {
        routine.id?.let { id ->
            loggingRepository.deleteRoutine(id)

        }
    }
}