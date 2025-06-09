package com.roc.projecte1

import com.google.gson.annotations.SerializedName

data class Grup(
    @SerializedName("id_grup") val idGrup: Int,
    @SerializedName("nom_grup") val nomGrup: String
)
