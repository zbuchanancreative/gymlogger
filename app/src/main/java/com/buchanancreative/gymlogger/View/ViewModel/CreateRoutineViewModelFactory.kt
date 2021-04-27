package com.buchanancreative.gymlogger.Model.ViewModel

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.buchanancreative.gymlogger.View.ViewModel.CreateRoutineViewModel
import com.buchanancreative.gymlogger.View.ViewModel.ExerciseViewModel

/**
 * Factory for creating a [CreateRoutineViewModel] with a constructor that takes a [CreateRoutineViewModel].
 */
class CreateRoutineViewModelFactory(
        private val application: Application,
        private val routineId: String?,
        private val lifecycleOwner: LifecycleOwner
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = CreateRoutineViewModel(application, lifecycleOwner) as T
}

class ExerciseViewModelFactory(
        private val application: Application,
        private val lifecycleOwner: LifecycleOwner
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress ("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = ExerciseViewModel(application, lifecycleOwner) as T
}