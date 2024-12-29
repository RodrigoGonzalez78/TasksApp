package com.example.tasksapp.domain.models

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val done: Boolean,
)
