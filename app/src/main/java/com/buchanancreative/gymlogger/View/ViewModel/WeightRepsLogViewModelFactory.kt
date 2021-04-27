package com.buchanancreative.gymlogger.View.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * Factory for creating a [WeightRepsLogViewModel] with a constructor that takes a [WeightRepsLogViewModel].
 */
class WeightRepsLogViewModelFactory(
        private val application: Application,
        private val exerciseId: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = WeightRepsLogViewModel(application, exerciseId) as T
}


class TimeLogViewModelFactory(
        private val application: Application,
        private val exerciseId: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = TimeLogViewModel(application, exerciseId) as T
}



class FreeweightLogViewModelFactory(
        private val application: Application,
        private val exerciseId: String
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = FreeweightLogViewModel(application, exerciseId) as T
}

class BodyweightLogViewModelFactory(
        private val application: Application,
        private val exerciseId: String
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = BodyweightLogViewModel(application, exerciseId) as T
}