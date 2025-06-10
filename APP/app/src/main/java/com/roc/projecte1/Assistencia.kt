package com.roc.projecte1

import java.util.Date
import com.google.gson.annotations.SerializedName

data class Assistencia(
    val data: Date,
    @SerializedName("tipus_assistencia") val tipusAssistencia: Char,
    val uf: String
)