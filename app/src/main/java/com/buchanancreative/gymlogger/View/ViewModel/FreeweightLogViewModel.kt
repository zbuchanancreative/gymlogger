package com.buchanancreative.gymlogger.View.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.buchanancreative.gymlogger.LoggerApp
import com.buchanancreative.loggerlibrary.Manager.SaveDataManager
import com.buchanancreative.loggerlibrary.Model.WeightRepLog
import com.buchanancreative.loggerlibrary.Persistence.*
import com.buchanancreative.loggerlibrary.Util.Constants

import kotlinx.coroutines.launch
import java.util.*

class FreeweightLogViewModel(application: Application, private val exerciseId: String): AndroidViewModel(application) {
    private val loggingRepository: LoggingRepository = FirebaseLoggingRepository((application as LoggerApp).getUserId())

    fun getRecentLogs(exerciseId: String): LiveData<List<WeightRepLog>> {
        return loggingRepository.getRecentWeightRepLogs(exerciseId)
    }

    fun save(weight: Double, reps: Int, exerciseId: String, exerciseName: String) {
        viewModelScope.launch {
            val weightToSave =  weight / Constants.kgToLbsConversion

            val log = WeightRepLog(null, exerciseId, exerciseName,  weightToSave, reps)
            loggingRepository.insert(log)
        }
    }

}