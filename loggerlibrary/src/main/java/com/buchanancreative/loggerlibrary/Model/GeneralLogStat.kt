package com.buchanancreative.loggerlibrary.Model

import com.google.firebase.firestore.DocumentId
import java.util.*

class GeneralLogStat (
        @DocumentId var id: String? = "",
        var exerciseId: String = "",
        var createdDate: Date? = null,
        var name: String? = "",
        var type: String? = ""
)
