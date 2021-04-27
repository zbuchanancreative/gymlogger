package com.buchanancreative.gymlogger.Util

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.buchanancreative.gymlogger.Model.ViewModel.CompleteRoutineViewModelFactory
import com.buchanancreative.gymlogger.Model.ViewModel.CreateRoutineViewModelFactory
import com.buchanancreative.gymlogger.Model.ViewModel.ExerciseViewModelFactory
import com.buchanancreative.gymlogger.View.ViewModel.*

object InjectorUtil {

    fun provideWeightRepsLogViewModelFactory(
        application: Application,
        exerciseId: String
    ): WeightRepsLogViewModelFactory {
        return WeightRepsLogViewModelFactory(application, exerciseId)
    }

    fun provideTimeLogViewModelFactory(
            application: Application,
            exerciseId: String
    ): TimeLogViewModelFactory {
        return TimeLogViewModelFactory(application, exerciseId)
    }

    fun provideFreeweightLogViewModelFactory(
            application: Application,
            exerciseId: String
    ): FreeweightLogViewModelFactory {
        return FreeweightLogViewModelFactory(application, exerciseId)
    }

    fun provideBodyweightLogViewModelFactory(
            application: Application,
            exerciseId: String
    ): BodyweightLogViewModelFactory {
        return BodyweightLogViewModelFactory(application, exerciseId)
    }


    fun provideCreateRoutineViewModeFactory(
            application: Application,
            routineId: String?,
            lifecycleOwner: LifecycleOwner
    ): CreateRoutineViewModelFactory {
        return CreateRoutineViewModelFactory(application, routineId, lifecycleOwner)
    }

    fun provideCompleteRoutineViewModelFactory(
            application: Application,
            routineId: String
    ): CompleteRoutineViewModelFactory {
        return CompleteRoutineViewModelFactory(application, routineId)
    }

    fun provideLogGraphViewModelFactory(
            application: Application,
            lifecycleOwner: LifecycleOwner
    ): LogGraphViewModelFactory {
        return LogGraphViewModelFactory(application, lifecycleOwner)
    }

    fun provideExerciseViewModelFactory(
            application: Application,
            lifecycleOwner: LifecycleOwner
    ) : ExerciseViewModelFactory {
        return ExerciseViewModelFactory(application, lifecycleOwner)
    }

}