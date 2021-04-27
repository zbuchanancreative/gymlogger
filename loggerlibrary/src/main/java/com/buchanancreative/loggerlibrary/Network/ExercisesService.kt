package com.buchanancreative.loggerlibrary.Network

import com.buchanancreative.loggerlibrary.Model.ExerciseResponse
import com.buchanancreative.loggerlibrary.Util.Constants
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

public interface GetExercisesService {
    @GET(Constants.GET_ALL_EXERCISES_ENDPOINT)
    fun getAllExercises(): Deferred<ExerciseResponse>
}