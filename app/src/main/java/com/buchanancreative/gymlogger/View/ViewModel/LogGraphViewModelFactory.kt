package com.buchanancreative.gymlogger.View.ViewModel

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LogGraphViewModelFactory(
        private val application: Application,
        private val lifecycleOwner: LifecycleOwner
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = LogGraphViewModel(application, lifecycleOwner) as T
}