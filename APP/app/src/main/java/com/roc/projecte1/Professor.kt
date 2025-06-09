package com.roc.projecte1

import com.google.gson.annotations.SerializedName

data class Professor(
    @SerializedName("id_professor") val idProfessor: Int,
    val nom: String,
    val email: String,
    val password: String,
    @SerializedName("data_naixement") val dataNaixement: String?,
    val grups: List<Grup>,
    @SerializedName("id_targeta") val idTargeta: Int
)
