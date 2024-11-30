package com.example.tasksapp.presenter

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tasksapp.presenter.login_screen.LoginScreen
import com.example.tasksapp.presenter.signup_screen.SignupScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "signup") {
        composable("signup") { SignupScreen(navController) }
        composable("login") { LoginScreen(navController) }
    }
}