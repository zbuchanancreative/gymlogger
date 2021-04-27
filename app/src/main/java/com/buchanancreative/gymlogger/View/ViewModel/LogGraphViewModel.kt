package com.buchanancreative.gymlogger.View.ViewModel

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

import com.buchanancreative.gymlogger.LoggerApp
import com.buchanancreative.loggerlibrary.Model.*
import com.buchanancreative.loggerlibrary.Persistence.*
import com.buchanancreative.loggerlibrary.Util.Constants

import com.buchanancreative.loggerlibrary.Util.Utility
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LogGraphViewModel(application: Application, private var lifecycleOwner: LifecycleOwner): AndroidViewModel(application) {
    private val repo: LoggingRepository = FirebaseLoggingRepository((application as LoggerApp).getUserId())

    var exerciseId: String? = null
    var exerciseName: String? = null
    var exerciseType: LogType? = null

    var lineData = MutableLiveData<LineData>()

    var tableData = MutableLiveData<List<LogTableRowData>>()

    var multipleDaysOfData: Boolean = false

    var firstDataPointValue = MutableLiveData<String>()

    var currentORMMax = 0

    val firstDataPointLabel: String
        get () {
            return when(exerciseType) {
                LogType.weightRep -> "ONE RM MAX"
                else -> "NOT SET"
            }
        }

    var secondDataPointValue = MutableLiveData<String>()

    val secondDataPointLabel: String
        get () {
            return when(exerciseType) {
                LogType.weightRep -> "VOLUME"
                else -> "NOT SET"
            }
        }

    var thirdDataPointValue = MutableLiveData<String>()

    val thirdDataPointLabel: String
        get () {
            return when(exerciseType) {
                LogType.weightRep -> "MAX SET"
                else -> "NOT SET"
            }
        }

    fun getDataRangeString(date: Date): String {
        if (multipleDaysOfData) {
            val dateString = SimpleDateFormat("MMM dd").format(date)
            return "Since $dateString"
        } else {
            val dateString = SimpleDateFormat("MMM dd").format(date)
            return dateString
        }
    }

    fun getLineData(after: Date): LiveData<LineData> {
        when (exerciseType) {
            LogType.weightRep  -> getWeightRepsLineData(after)
            LogType.rep -> getRepsLineData(after)
            LogType.time -> getDurationLineData(after)
        }

        return lineData
    }

    fun getTableData(after: Date): LiveData<List<LogTableRowData>> {
        when(exerciseType) {
            LogType.weightRep -> getWeightRepsTableData(after)
            LogType.rep -> getRepsTableData(after)
            LogType.time -> getDurationTableData(after)
        }

        return tableData
    }

    fun deleteLog(id: String) {
        repo.deleteLog(id)
    }

    // region start of duration section
    private fun getDurationTableData(after: Date) {
        val id = exerciseId ?: return

        val freshLogs =  repo.getAllTimeLogs(id,after)

        freshLogs.observe(lifecycleOwner, Observer<List<TimeLog>> { logs ->

            val groupedList = logs.groupBy { SimpleDateFormat("yyyyMMdd").format(it.createdDate) }

            val data = when(groupedList.size) {
                1 -> getOneDayDurationTableData(logs)
                else -> getMultipleDayTableData(groupedList)
            }

            tableData.postValue(data)
        })
    }

    private fun getOneDayDurationTableData(logs: List<TimeLog>): ArrayList<LogTableRowData> {
        val data = ArrayList<LogTableRowData>()
        for (i in 0 until logs.size) {
            // TODO allow for this to Kilograms instead of LBS
            val title = logs[i].duration.toString() + "s"
            val dateString = SimpleDateFormat("HH: mm").format(logs[i].createdDate)
            val rowData = LogTableRowData(title, dateString)
            data.add(rowData)
        }

        return data
    }


    private fun getDurationLineData(after: Date) {
        val id = exerciseId ?: return
        val freshLogs =  repo.getAllDurationLogs(id, after)

        freshLogs.observe(lifecycleOwner, Observer<List<TimeLog>> { logs ->
            val groupedList = logs.groupBy { SimpleDateFormat("yyyyMMdd").format(it.createdDate) }

            val entries=  when(groupedList.size) {
                1 -> getDurationEntriesFromDay(logs)
                else -> getEntriesFromMultipleDays(groupedList)
            }

            val dataSet = LineDataSet(entries, "Max Time")
            lineData.postValue(LineData(dataSet))
        })
    }

    private fun getDurationEntriesFromDay(logs: List<TimeLog>):ArrayList<Entry>  {
        val entries = ArrayList<Entry>()
        for (i in 0 until logs.size) {
            val log = logs[i]

            val entry = Entry((i + 1).toFloat(), log.duration.toFloat())
            entries.add(entry)
        }

        return entries
    }

    // endregion end of duration section
    
    // region start of weight reps section
    private fun getWeightRepsTableData(after: Date) {
        val id = exerciseId ?: return

        val freshLogs =  repo.getAllWeightRepLogs(id,after)

        freshLogs.observe(lifecycleOwner, Observer<List<WeightRepLog>> { logs ->

            val groupedList = logs.groupBy { SimpleDateFormat("yyyyMMdd").format(it.createdDate) }

            val data = when(groupedList.size) {
                1 -> getOneDayTableData(logs)
                else -> getMultipleDayTableData(groupedList)
            }

            tableData.postValue(data)
        })
    }

    private fun getOneDayTableData(logs: List<WeightRepLog>): ArrayList<LogTableRowData> {
        val data = ArrayList<LogTableRowData>()
        for (i in 0 until logs.size) {
            val weight = logs[i].weight * Constants.kgToLbsConversion
            // TODO allow for this to Kilograms instead of LBS
            val title = weight.toString() + " LBS" + " X " + logs[i].reps.toString() + " REPS"

            val dateString = SimpleDateFormat("HH: mm").format(logs[i].createdDate)
            val rowData = LogTableRowData(title, dateString, logs[i].id)
            data.add(rowData)
        }

        return data
    }

    @Suppress("UNCHECKED_CAST")
    private fun getMultipleDayTableData(groupedList: Map<String, List<Any>>): ArrayList<LogTableRowData> {
        val data = ArrayList<LogTableRowData>()

        var dateOfLogs: Date? = null

        groupedList.forEach {logs ->
            val title = when(logs.value[0]) {
                is WeightRepLog -> {
                    val weightRepLogs = logs as Map.Entry<String, List<WeightRepLog>>
                    val logWithOneRepMaxForDay = weightRepLogs.value.maxBy { Utility.calculateOneRepMax(it.weight * Constants.kgToLbsConversion, it.reps) } as WeightRepLog
                    dateOfLogs = logWithOneRepMaxForDay.createdDate
                    // Get the max one rep max off the log identified
                    val oneRepMaxForDay = Utility.calculateOneRepMax(logWithOneRepMaxForDay.weight * Constants.kgToLbsConversion, logWithOneRepMaxForDay.reps)
                    oneRepMaxForDay.toString() + "LBS ORM"
                }

                is RepLog -> {
                    val repLogs = logs as Map.Entry<String, List<RepLog>>
                    val repMaxLog = repLogs.value.maxBy { it.reps } as RepLog
                    dateOfLogs = repMaxLog.createdDate
                    repMaxLog.reps.toString() + " Reps"
                }

                is TimeLog -> {
                    val timeLogs = logs as Map.Entry<String, List<TimeLog>>
                    val logWithOneRepMaxForDay = timeLogs.value.maxBy { it.duration } as TimeLog
                    dateOfLogs = logWithOneRepMaxForDay.createdDate
                    logWithOneRepMaxForDay.duration.toString() + "s"
                }

                else -> ""
            }

            val dateString = SimpleDateFormat("MMM dd").format(dateOfLogs)


            val rowData = LogTableRowData(title, dateString)
            data.add(rowData)
        }

        return data
    }

    private fun getWeightRepsLineData(after: Date){
        val id = exerciseId ?: return
        val freshLogs =  repo.getAllWeightRepLogs(id,after)

        freshLogs.observe(lifecycleOwner, Observer<List<WeightRepLog>> {logs ->
            val groupedList = logs.groupBy { SimpleDateFormat("yyyyMMdd").format(it.createdDate) }

            val entries=  when(groupedList.size) {
                1 -> getEntriesFromDay(logs)
                else -> getEntriesFromMultipleDays(groupedList)
            }

            val dataSet = LineDataSet(entries, "One Rep Max")
            lineData.postValue(LineData(dataSet))
        })
    }

    private fun getEntriesFromDay(logs: List<WeightRepLog>):ArrayList<Entry>  {
        val entries = ArrayList<Entry>()
        for (i in logs.indices) {
            val log = logs[i]
            val oneRM =  Utility.calculateOneRepMax(log.weight * Constants.kgToLbsConversion, log.reps)

            val entry = Entry((i + 1).toFloat(), oneRM.toFloat())
            entries.add(entry)
        }

        return entries
    }

    @Suppress("UNCHECKED_CAST")
    private fun getEntriesFromMultipleDays(groupedList: Map<String, List<Any>>): ArrayList<Entry>{
        multipleDaysOfData = true

        val entries = ArrayList<Entry>()

        var dateOfLogs: Date? = null

        groupedList.forEach { logs ->
            val oneRepMaxForDay = when(logs.value[0]) {
                is WeightRepLog -> {
                    val weightRepLogs = logs as Map.Entry<String, List<WeightRepLog>>
                    dateOfLogs = weightRepLogs.value[0].createdDate
                    firstDataPointValue.postValue("$currentORMMax LBS")
                    getORMForDayAndSetORM(weightRepLogs)
                }
                is RepLog -> {
                    val repLogs = logs as Map.Entry<String, List<RepLog>>
                    val repMaxLog = repLogs.value.maxBy { it.reps } as RepLog
                    dateOfLogs = repLogs.value[0].createdDate
                    repMaxLog.reps.toFloat()

                }
                is TimeLog -> {
                    val timeLogs = logs as Map.Entry<String, List<TimeLog>>
                    val timeLog = timeLogs.value.maxBy { it.duration } as TimeLog
                    dateOfLogs = timeLog.createdDate
                    timeLog.duration.toFloat()
                }
                else -> 0.toFloat()
            }

            // pull the date from the first one in the list, since they are all the same

            dateOfLogs?.time?.toFloat()?.let {
                val entry = Entry(it, oneRepMaxForDay.toFloat())
                entries.add(entry)
            }
        }


        return entries
    }


    private fun getORMForDayAndSetORM(logs: Map.Entry<String, List<WeightRepLog>>): Float {
        // This block of code should be pulled out into a seperate WeightRepLog function, same for the other duration, rep exercises
        val logWithOneRepMaxForDay = logs.value.maxBy { Utility.calculateOneRepMax(it.weight * Constants.kgToLbsConversion, it.reps) } as WeightRepLog

        val oneRepMaxForDay = Utility.calculateOneRepMax(logWithOneRepMaxForDay.weight * Constants.kgToLbsConversion, logWithOneRepMaxForDay.reps)

        if (oneRepMaxForDay > currentORMMax) {
            currentORMMax = oneRepMaxForDay
        }

        return oneRepMaxForDay.toFloat()
    }

    // endregion end of weight reps section


    // region start of reps section
    private fun getRepsLineData(after: Date) {
        val id = exerciseId ?: return
        val freshLogs =  repo.getAllRepLogs(id, after)

        freshLogs.observe(lifecycleOwner, Observer<List<RepLog>> { logs ->
            val groupedList = logs.groupBy { SimpleDateFormat("yyyyMMdd").format(it.createdDate) }

            val entries=  when(groupedList.size) {
                1 -> getRepsEntriesFromDay(logs)
                else -> getEntriesFromMultipleDays(groupedList)
            }

            val dataSet = LineDataSet(entries, "One Rep Max")
            lineData.postValue(LineData(dataSet))
        })
    }

    private fun getRepsEntriesFromDay(logs: List<RepLog>):ArrayList<Entry>  {
        val entries = ArrayList<Entry>()
        for (i in 0 until logs.size) {
            val log = logs[i]

            val entry = Entry((i + 1).toFloat(), log.reps.toFloat())
            entries.add(entry)
        }

        return entries
    }

    private fun getRepsTableData(after: Date) {
        val id = exerciseId ?: return

        val freshLogs =  repo.getAllRepLogs(id,after)

        freshLogs.observe(lifecycleOwner, Observer<List<RepLog>> { logs ->

            val groupedList = logs.groupBy { SimpleDateFormat("yyyyMMdd").format(it.createdDate) }

            val data = when(groupedList.size) {
                1 -> getOneDayRepTableData(logs)
                else -> getMultipleDayTableData(groupedList)
            }

            tableData.postValue(data)
        })
    }



    private fun getOneDayRepTableData(logs: List<RepLog>):ArrayList<LogTableRowData> {
        val data = ArrayList<LogTableRowData>()
        for (i in 0 until logs.size) {
            // TODO allow for this to Kilograms instead of LBS
            val title = logs[i].reps.toString() + " REPS"
            val dateString = SimpleDateFormat("HH: mm").format(logs[i].createdDate)
            val rowData = LogTableRowData(title, dateString, logs[i].id)
            data.add(rowData)
        }

        return data
    }

    // endregion end of reps section
}


class DateValueFormatter: IAxisValueFormatter {
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        val date = Date(value.toLong())
        val formatter = SimpleDateFormat("MM/dd")
        return formatter.format(date)
    }

}

data class LogTableRowData(
        // TODO The ID should be here so the log can be updated/deleted
        var title: String,
        var dateOrTime: String,
        var logId: String? = null
)


