package com.roc.projecte1

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("type") val userType: Char,
    @SerializedName("user") val userData: Map<String, Any>
)
