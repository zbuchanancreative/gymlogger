package com.buchanancreative.gymlogger.Model.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.buchanancreative.gymlogger.View.ViewModel.CompleteRoutineViewModel

/**
 * Factory for creating a [CreateRoutineViewModel] with a constructor that takes a [CreateRoutineViewModel].
 */
class CompleteRoutineViewModelFactory(
        private val application: Application,
        private val routineId: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = CompleteRoutineViewModel(application, routineId) as T
}
