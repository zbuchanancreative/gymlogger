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

class WeightRepsLogViewModel(application: Application, private val exerciseId: String): AndroidViewModel(application) {
    private val loggingRepository: LoggingRepository = FirebaseLoggingRepository((application as LoggerApp).getUserId())

    fun getRecentWeightRepLogs(): LiveData<List<WeightRepLog>> {
        return loggingRepository.getRecentWeightRepLogs(exerciseId)
    }

    fun save(weight: Double, reps: Int, exerciseId: String, name: String) {

        val weightToSave =  weight / Constants.kgToLbsConversion

        viewModelScope.launch {
            val log = WeightRepLog(null, exerciseId, name, weightToSave, reps)
            loggingRepository.insert(log)
        }
    }
}