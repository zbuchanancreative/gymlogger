package com.buchanancreative.loggerlibrary.Model

import com.google.firebase.firestore.DocumentId


data class Routine (
        var id: String? = "",
        val name: String = "",
        var userId: String? = null
)
