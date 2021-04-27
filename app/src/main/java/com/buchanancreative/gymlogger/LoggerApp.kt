package com.buchanancreative.gymlogger

import android.app.Application
import com.buchanancreative.loggerlibrary.Persistence.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoggerApp: Application() {

    private fun getExerciseDatabase(): ExerciseDatabase {
        return ExerciseDatabase.getInstance(this)
    }

    fun getExerciseRepository(): ExerciseRoomRepository {
        return ExerciseRoomRepository.getInstance(getExerciseDatabase())
    }


    private fun getLoggingDatabase(): LoggingDatabase {
        return LoggingDatabase.getInstance(this)
    }

    fun getLoggingRepository(): RoomLoggingRepository {
        return RoomLoggingRepository.getInstance(getLoggingDatabase())
    }

    fun getUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }
}


