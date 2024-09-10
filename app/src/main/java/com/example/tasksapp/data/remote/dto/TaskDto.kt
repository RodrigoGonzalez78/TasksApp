package com.example.tasksapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TaskDto (
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("done") val done: Boolean,
    @SerializedName("userId") val userId: Long
)
