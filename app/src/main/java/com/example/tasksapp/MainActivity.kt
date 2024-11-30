package com.example.tasksapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tasksapp.presenter.AppNavHost
import com.example.tasksapp.presenter.login_screen.LoginScreen
import com.example.tasksapp.presenter.signup_screen.SignupScreen
import com.example.tasksapp.presenter.ui.theme.TasksAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TasksAppTheme {
              AppNavHost()
            }
        }
    }
}

