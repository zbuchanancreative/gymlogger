package com.buchanancreative.gymlogger.View.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope

import com.buchanancreative.loggerlibrary.Model.RepLog

import com.buchanancreative.gymlogger.LoggerApp
import com.buchanancreative.loggerlibrary.Manager.SaveDataManager
import com.buchanancreative.loggerlibrary.Persistence.*

import kotlinx.coroutines.launch
import java.util.*

class BodyweightLogViewModel(application: Application, exerciseId: String): AndroidViewModel(application) {
    private val loggingRepository: LoggingRepository = FirebaseLoggingRepository((application as LoggerApp).getUserId())

    fun getRecentLogs(exerciseId: String): LiveData<List<RepLog>> {
        return loggingRepository.getRecentRepLogs(exerciseId)
    }


    fun save(reps: Int, exerciseId: String, exerciseName: String) {
        viewModelScope.launch {
            val datetime = Date(System.currentTimeMillis())
            val log = RepLog(null, exerciseId, exerciseName, reps, datetime)
            loggingRepository.insert(log)
        }
    }
}