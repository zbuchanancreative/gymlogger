package com.buchanancreative.loggerlibrary.Model

import com.google.firebase.database.PropertyName
import com.google.gson.annotations.SerializedName


enum class ExerciseType(val type: String) {
    Barbell("barbell"),
    Time("time"),
    Freeweight("freeweight"),
    Machine("machine"),
    Bodyweight("bodyweight")
}

data class Exercise (val name: String = "", val type: String = "", var id: String = "")
