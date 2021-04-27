package com.buchanancreative.loggerlibrary.Persistence


import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buchanancreative.loggerlibrary.Model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.sql.Time
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class FirebaseLoggingRepository(val userId: String?): LoggingRepository {

    override fun getLogsAggregatedByExerciseId(fromDate: Long): LiveData<List<AggregateLogStat>> {

        val data = MutableLiveData<List<AggregateLogStat>>()

        val userId = userId ?: return data

        val date = Date(fromDate)

        val ref = db.collection("logs").whereEqualTo("userId", userId)
        ref.get().addOnSuccessListener { result ->
            val logs = ArrayList<GeneralLogStat>()
            for (document in result) {
                val log =  document.toObject(GeneralLogStat::class.java)
                if(log.createdDate?.after(date) == true) {
                    logs.add(log)
                }
            }

             data.postValue(convertLogListToAggregatedLogStats(logs))
        }

        return data

    }

    private fun convertLogListToAggregatedLogStats(list: List<GeneralLogStat>): ArrayList<AggregateLogStat> {
        // put logs into buckets depending on what their exercise id is, then convert these buckets to aggregated log stats and return

        val groupedList = list.groupBy { it.exerciseId }

        val aggregatedArray = ArrayList<AggregateLogStat>()
        groupedList.forEach {
            val logStat = AggregateLogStat(it.key, it.value.count(), it.value.firstOrNull()?.name, it.value.firstOrNull()?.type)
            aggregatedArray.add(logStat)
        }

        return aggregatedArray
    }

    private val db = FirebaseFirestore.getInstance()

    override fun getRecentTimeLogs(exerciseId: String): LiveData<List<TimeLog>> {
        var recentLogs: MutableLiveData<List<TimeLog>> = MutableLiveData()

        val userId = userId ?: return recentLogs

        val date = Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(10))

        // get the reference, convert the snapshot to a list of weightRepLogs and return
        val query = db.collection("logs")
                .whereEqualTo("exerciseId", exerciseId)
                .whereEqualTo("userId", userId)
                .whereGreaterThan("createdDate", date)

        query.addSnapshotListener { value, _ ->
            val documents = value ?: return@addSnapshotListener
            val logs = ArrayList<TimeLog>()
            for ( document in documents) {
                val log =  document.toObject(TimeLog::class.java)
                logs.add(log)
            }

            recentLogs.postValue(logs)
        }

        return recentLogs
    }

    override fun getAllTimeLogs(exerciseId: String, after: Date): LiveData<List<TimeLog>> {
        var recentLogs: MutableLiveData<List<TimeLog>> = MutableLiveData()

        val userId = userId ?: return recentLogs

        // get the reference, convert the snapshot to a list of weightRepLogs and return
        val query = db.collection("logs")
                .whereEqualTo("exerciseId", exerciseId)
                .whereEqualTo("userId", userId)
                .whereGreaterThan("createdDate", after)

        query.addSnapshotListener { value, _ ->
            val documents = value ?: return@addSnapshotListener
            val logs = ArrayList<TimeLog>()
            for ( document in documents) {
                val log =  document.toObject(TimeLog::class.java)
                logs.add(log)
            }

            recentLogs.postValue(logs)
        }

        return recentLogs
     }

    override fun getAllRepLogs(exerciseId: String, after: Date): LiveData<List<RepLog>> {
        var recentLogs: MutableLiveData<List<RepLog>> = MutableLiveData()

        val userId = userId ?: return recentLogs

        // get the reference, convert the snapshot to a list of weightRepLogs and return
        val query = db.collection("logs")
                .whereEqualTo("exerciseId", exerciseId)
                .whereEqualTo("userId", userId)
                .whereGreaterThan("createdDate", after)


        query.addSnapshotListener { value, _ ->
            val documents = value ?: return@addSnapshotListener
            val logs = ArrayList<RepLog>()
            for ( document in documents) {
                val log =  document.toObject(RepLog::class.java)
                logs.add(log)
            }

            recentLogs.postValue(logs)
        }

        return recentLogs
    }

    override fun getRecentRepLogs(exerciseId: String): LiveData<List<RepLog>> {
        var recentLogs: MutableLiveData<List<RepLog>> = MutableLiveData()

        val userId = userId ?: return recentLogs
        val date = Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(10))

        val query = db.collection("logs")
                .whereEqualTo("exerciseId", exerciseId)
                .whereEqualTo("userId", userId)
                .whereGreaterThan("createdDate", date)

        query.addSnapshotListener { value, _ ->
            val documents = value ?: return@addSnapshotListener
            val logs = ArrayList<RepLog>()
            for ( document in documents) {
                val log =  document.toObject(RepLog::class.java)
                logs.add(log)
            }

            recentLogs.postValue(logs)
        }

        return recentLogs
    }

    override fun getRecentWeightRepLogs(exerciseId: String): LiveData<List<WeightRepLog>> {
        var recentLogs: MutableLiveData<List<WeightRepLog>> = MutableLiveData()

        Log.i("zzz", "USER ID: $userId")


        val userId = userId ?: return recentLogs

        val tenHoursAgo = Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(10))

        // get the reference, convert the snapshot to a list of weightRepLogs and return
        val query = db.collection("logs")
                .whereEqualTo("exerciseId", exerciseId)
                .whereEqualTo("userId", userId)
                // TODO Add where to this query to only get exercises in the last ten hours in the query.
                // TODO  Might need a composite index to do this and use .where("createdDate", ">", date)

        query.addSnapshotListener { value, _ ->
            Log.i("zzz", "NEW LOG: $value")
            val documents = value ?: return@addSnapshotListener
            val logs = ArrayList<WeightRepLog>()
            for ( document in documents) {
                val log =  document.toObject(WeightRepLog::class.java)
                if (log.createdDate?.after(tenHoursAgo) == true) {
                    logs.add(log)
                }
            }

            recentLogs.postValue(logs)
        }

        return recentLogs
    }

    override fun getAllWeightRepLogs(exerciseId: String, after: Date): LiveData<List<WeightRepLog>> {
        var recentLogs: MutableLiveData<List<WeightRepLog>> = MutableLiveData()

        val userId = userId ?: return recentLogs

        // get the reference, convert the snapshot to a list of weightRepLogs and return
        val query = db.collection("logs")
                .whereEqualTo("exerciseId", exerciseId)
                .whereEqualTo("userId", userId)
                .whereGreaterThan("createdDate", after)


        query.addSnapshotListener { value, _ ->
            val documents = value ?: return@addSnapshotListener
            val logs = ArrayList<WeightRepLog>()
            for ( document in documents) {
                val log =  document.toObject(WeightRepLog::class.java)
                log.id = document.id
                logs.add(log)
            }

            recentLogs.postValue(logs)
        }

        return recentLogs
    }

    override fun getAllDurationLogs(exerciseId: String, after: Date): LiveData<List<TimeLog>> {
        var recentLogs: MutableLiveData<List<TimeLog>> = MutableLiveData()

        val userId = userId ?: return recentLogs

        // get the reference, convert the snapshot to a list of weightRepLogs and return
        val query = db.collection("logs")
                .whereEqualTo("exerciseId", exerciseId)
                .whereEqualTo("userId", userId)
                .whereGreaterThan("createdDate", after)

        query.addSnapshotListener { value, _ ->
            val documents = value ?: return@addSnapshotListener
            val logs = ArrayList<TimeLog>()
            for ( document in documents) {
                val log =  document.toObject(TimeLog::class.java)
                logs.add(log)
            }

            recentLogs.postValue(logs)
        }

        return recentLogs
    }

    override suspend fun insert(log: WeightRepLog) {
        val userId = userId ?: return
        log.userId = userId
        val ref =  db.collection("logs")
        ref.add(log)
    }

    override suspend fun insert(log: RepLog) {
        val userId = userId ?: return
        log.userId = userId
        val ref = db.collection("logs")
        ref.add(log)
    }

    override suspend fun insert(log: TimeLog) {
        val userId = userId ?: return
        log.userId = userId

        val ref = db.collection("logs")
        ref.add(log)
    }

    override suspend fun insert(exercise: Exercise, routineId: String) {
        val ref =  db.collection("routines").document(routineId).collection("exercises")
        ref.add(exercise)
    }

    override suspend fun insert(exercises: List<Exercise>, routineId: String) {
        val ref =  db.collection("routines").document(routineId).collection("exercises")

        for (exercise in exercises) {
            ref.add(exercise)
        }
    }



    override suspend fun insert(routine: Routine): String {
        val userId = userId ?: return ""
        routine.userId = userId

        val ref =  db.collection("routines")
        val documentRef = ref.document()
        documentRef.set(routine)
        documentRef.update("id", documentRef.id)

        return documentRef.id
    }

    override fun deleteRoutine(routineId: String) {
        db.collection("routines").document(routineId).delete().addOnSuccessListener {
            Log.d("REPO", "deleted routine")
        }.addOnFailureListener {
            Log.e("REPO", "Failed to delete routine")
        }
    }

    override fun deleteLog(logId: String) {
        val userId = userId ?: return
        db.collection("logs").document(logId).delete().addOnSuccessListener {
            Log.d("REPO", "deleted log")
        }.addOnFailureListener {
            Log.e("REPO", "Failed to delete LOG")
        }
    }

    override fun getAllExercisesInRoutine(routineId: String, onSuccess: (exercises: List<Exercise>) -> Unit): LiveData<List<Exercise>> {
        val allExercisesInRoutine = MutableLiveData<List<Exercise>>()

        val query = db.collection("routines").document(routineId).collection("exercises")
        query.addSnapshotListener { value, e ->
            assert(e == null)
            
            val documents = value ?: return@addSnapshotListener
            val exercises = ArrayList<Exercise>()
            for (document in documents) {
                val exercise = document.toObject(Exercise::class.java)
                exercises.add(exercise)
            }

            allExercisesInRoutine.postValue(exercises)

            onSuccess.invoke(exercises)
        }

        return allExercisesInRoutine
    }

    override fun getAllRoutines(onSuccess: (routines: List<Routine>) -> Unit) {
        val query = db.collection("routines").whereEqualTo("userId", userId)
        query.addSnapshotListener { value, _ ->
            val documents = value ?: return@addSnapshotListener
            val routines = ArrayList<Routine>()
            for (document in documents) {
                val routine = document.toObject(Routine::class.java)
                routines.add(routine)
            }

            onSuccess.invoke(routines)

        }
    }


    override fun getAllRoutines(): LiveData<List<Routine>> {
        var result = MutableLiveData<List<Routine>>()

        val query = db.collection("routines").whereEqualTo("userId", userId)
        query.addSnapshotListener { value, _ ->
            val documents = value ?: return@addSnapshotListener
            val routines = ArrayList<Routine>()
            for (document in documents) {
                val routine = document.toObject(Routine::class.java)
                routines.add(routine)
            }

            result.postValue(routines)

        }

        return result
    }


    override fun updateRoutineName(routineId: String, name: String) {
        db.collection("routines").document(routineId).update("name", name)
    }

}
