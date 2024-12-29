package com.example.tasksapp.data.remote

import com.example.tasksapp.data.remote.dto.JwtModel
import com.example.tasksapp.data.remote.dto.LoginRequest
import com.example.tasksapp.data.remote.dto.TaskDto
import com.example.tasksapp.data.remote.dto.UserDto
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): JwtModel

    @POST("signup")
    suspend fun signUp(@Body request: UserDto): ResponseBody

    @GET("tasks")
    suspend fun getTasks(@Header("Authorization") token: String): List<TaskDto>

    @POST("tasks")
    suspend fun createTask(
        @Header("Authorization") token: String,
        @Body request: TaskDto
    ): ResponseBody

    @DELETE("tasks/{id}")
    suspend fun deleteTask(
        @Header("Authorization") token: String,
        @Path("id") taskId: Int
    ): ResponseBody

    companion object {
        const val BASE_URL = "http://192.168.172.237:3000"
    }
}