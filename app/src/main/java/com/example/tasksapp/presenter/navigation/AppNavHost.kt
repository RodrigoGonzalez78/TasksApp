package com.example.tasksapp.presenter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tasksapp.presenter.home_screen.HomeScreen
import com.example.tasksapp.presenter.login_screen.LoginScreen
import com.example.tasksapp.presenter.new_task_screen.NewTaskScreen
import com.example.tasksapp.presenter.signup_screen.SignupScreen
import com.example.tasksapp.presenter.splash_screen.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: NavigationViewModel = hiltViewModel()
) {

    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            navController.navigate("home") {
                popUpTo(0) { inclusive = true }
            }
        } else {
            navController.navigate("signup") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen() }
        composable("home") { HomeScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable(
            route = "task_screen",
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            NewTaskScreen(navController, taskId)
        }
    }
}

fun taskScreenRoute(taskId: String) = "task_screen/$taskId"