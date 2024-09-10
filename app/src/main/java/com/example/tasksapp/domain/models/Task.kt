package com.example.tasksapp.domain.models

data class Task(
    val title: String,
    val description: String,
    val done: Boolean,
    val userId: Long
)
