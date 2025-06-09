package com.roc.projecte1

import com.google.gson.annotations.SerializedName

data class Alumne(
    @SerializedName("id_alumne") val idAlumne: Int,
    val nom: String,
    val email: String,
    val password: String,
    @SerializedName("data_naixement") val dataNaixement: String?,
    @SerializedName("data_matriculacio") val dataMatriculacio: String?,
    val grup: Grup?,
    @SerializedName("id_targeta") val idTargeta: Int
)
