package com.buchanancreative.loggerlibrary.Manager

import com.buchanancreative.loggerlibrary.Model.RoomRepLog
import com.buchanancreative.loggerlibrary.Model.TimeLog
import com.buchanancreative.loggerlibrary.Persistence.LoggingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Created by Alpha on 10/22/2017.
 */

class SaveDataManager(var loggingRepository: LoggingRepository) {

    companion object {
        private val TAG = SaveDataManager::class.java.name
    }
}
