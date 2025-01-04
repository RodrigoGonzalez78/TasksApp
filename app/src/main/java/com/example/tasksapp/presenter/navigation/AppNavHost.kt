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
import com.example.tasksapp.presenter.task_screen.TaskScreen
import com.example.tasksapp.presenter.signup_screen.SignupScreen
import com.example.tasksapp.presenter.splash_screen.SplashScreen
import com.example.tasksapp.presenter.update_password_screen.UpdatePasswordScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Signup : Screen("signup")
    data object Login : Screen("login")
    data object Task : Screen("task_screen?taskId={taskId}") {
        fun createRoute(taskId: String) = "task_screen?taskId=$taskId"
    }
    data object UpdatePassword : Screen("update_password")
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    LaunchedEffect(isAuthenticated) {
        val route = if (isAuthenticated) Screen.Home.route else Screen.Signup.route
        navController.navigate(route) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen()
        }

        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.Signup.route) {
            SignupScreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.UpdatePassword.route) {
            UpdatePasswordScreen(navController)
        }

        composable(
            route = Screen.Task.route,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            TaskScreen(navController, taskId)
        }
    }
}