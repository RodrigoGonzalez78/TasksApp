package com.example.tasksapp.data.remote.dto

import com.example.tasksapp.domain.models.Task
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TaskDto(
    @SerializedName("ID") val id: Int?,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("done") val done: Boolean?,
    @SerializedName("user_id") val userId: Long?,
    @SerializedName("CreatedAt") val createdAt: String?,
    @SerializedName("UpdatedAt") val updatedAt: String?,
    @SerializedName("DeletedAt") val deletedAt: String?
)


fun TaskDto.toDomainModel(): Task {
    return Task(
        id = this.id ?: 0,
        title = this.title,
        description = this.description,
        done = this.done ?: false
    )
}