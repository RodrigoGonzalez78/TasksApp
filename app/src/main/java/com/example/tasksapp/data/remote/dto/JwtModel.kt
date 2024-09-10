package com.example.tasksapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class JwtModel (
    @SerializedName("token") val token: String
)