package com.example.tasksapp.data.remote.dto

import com.example.tasksapp.domain.models.Task
import com.google.gson.annotations.SerializedName

data class TaskDto(
    @SerializedName("ID") val id: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("done") var done: Boolean? = null,
    @SerializedName("user_id") val userId: Long? = null,
    @SerializedName("CreatedAt") val createdAt: String? = null,
    @SerializedName("UpdatedAt") val updatedAt: String? = null,
    @SerializedName("DeletedAt") val deletedAt: String? = null
)


fun TaskDto.toTask(): Task {
    return Task(
        id = this.id ?: 0,
        title = this.title ?: "Sin título",
        description = this.description ?: "Sin descripción",
        done = this.done ?: false
    )
}

fun List<TaskDto>.toTaskList(): List<Task> {
    return this.map { it.toTask() }
}