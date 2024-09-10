package com.example.tasksapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto (
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)