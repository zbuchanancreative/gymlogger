package com.buchanancreative.gymlogger.View.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.buchanancreative.gymlogger.R
import com.buchanancreative.gymlogger.LoggerApp
import com.google.android.material.chip.Chip
import com.buchanancreative.loggerlibrary.Model.AggregateLogStat
import com.buchanancreative.loggerlibrary.Persistence.*
import java.util.*

import java.util.concurrent.TimeUnit

class LogStatsViewModel(application: Application): AndroidViewModel(application) {

    private val logRepo: LoggingRepository = FirebaseLoggingRepository((application as LoggerApp).getUserId())
    private val exerciseRepo: ExerciseRepository = ExerciseRealtimeDatabaseRepository()

    var selection : Chip? = null

    fun getCurrentSelectionLogs() : LiveData<List<AggregateLogStat>> {
        return when (selection?.id) {

            R.id.twentyFourHourSelection -> getLast24HrLogs()
            R.id.weekSelection -> getLast7DayLogs()
            R.id.monthSelection -> getLastMonthLogs()
            R.id.threeMonthSelection -> getLastThreeMonthsLogs()
            R.id.yearSelection -> getLastYearLogs()
            else -> getLastYearLogs()
        }
    }

    fun getCurrentSelectionText(): String {
        return when (selection?.id) {
            R.id.twentyFourHourSelection -> "Yesterday"
            R.id.weekSelection -> "Last Week"
            R.id.monthSelection -> "Last Month"
            R.id.threeMonthSelection -> "Last Three Months"
            R.id.yearSelection -> "Last Year"
            else -> "Last Year"
        }
    }


    fun getLast24HrLogs(): LiveData<List<AggregateLogStat>> {
        val then = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
        return logRepo.getLogsAggregatedByExerciseId(then)
    }

    fun getLast7DayLogs(): LiveData<List<AggregateLogStat>> {
        val then = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
        return logRepo.getLogsAggregatedByExerciseId( then)
    }

    fun getLastMonthLogs(): LiveData<List<AggregateLogStat>> {
        val then = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
        return logRepo.getLogsAggregatedByExerciseId(then)
    }

    fun getLastYearLogs(): LiveData<List<AggregateLogStat>> {
        val then = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365)
        return logRepo.getLogsAggregatedByExerciseId(then)
    }

    fun getLastThreeMonthsLogs(): LiveData<List<AggregateLogStat>> {
        val then = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(90)
        return logRepo.getLogsAggregatedByExerciseId(then)
    }

    fun getDateForStatsSelection(): Date = when (selection?.id) {
            R.id.twentyFourHourSelection -> Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))
            R.id.weekSelection -> Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7))
            R.id.monthSelection -> Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30))
            R.id.threeMonthSelection -> Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(90))
            R.id.yearSelection -> Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365))
            else -> Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365))
        }

}