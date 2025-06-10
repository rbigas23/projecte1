package com.roc.projecte1

import com.google.gson.annotations.SerializedName

data class Alumne(
    @SerializedName("id_alumne") val idAlumne: Int? = null,
    val nom: String,
    val email: String,
    val password: String,
    @SerializedName("data_naixement") val dataNaixement: String?,
    @SerializedName("data_matriculacio") val dataMatriculacio: String? = null,
    val grup: Grup? = null,
    @SerializedName("id_targeta") val idTargeta: Int? = null,
    @SerializedName("id_grup") val idGrup: Int? = null
)
