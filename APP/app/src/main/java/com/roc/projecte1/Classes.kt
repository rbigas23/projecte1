package com.roc.projecte1

import com.google.gson.annotations.SerializedName

data class Classes(
    @SerializedName("codi_uf") val codiUf: String,
    @SerializedName("desc_modul") val descModul: String
)
