package com.buchanancreative.gymlogger.View.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.buchanancreative.gymlogger.LoggerApp

import com.buchanancreative.loggerlibrary.Model.TimeLog

import com.buchanancreative.loggerlibrary.Manager.SaveDataManager
import com.buchanancreative.loggerlibrary.Persistence.*

import kotlinx.coroutines.launch
import java.util.*

class TimeLogViewModel(application: Application, private val exerciseId: String): AndroidViewModel(application) {
    private val loggingRepository: LoggingRepository = FirebaseLoggingRepository((application as LoggerApp).getUserId())


    fun getRecentTimeLogs(): LiveData<List<TimeLog>> {
        return loggingRepository.getRecentTimeLogs(exerciseId)
    }

    fun save(exerciseId: String, exerciseName: String, duration: Int) {
        viewModelScope.launch {
            val datetime = Date(System.currentTimeMillis())
            val log = TimeLog(null, exerciseId, exerciseName, duration, datetime)
            loggingRepository.insert(log)
        }
    }
}