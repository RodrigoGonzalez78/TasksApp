package com.example.tasksapp.presenter.home_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tasksapp.presenter.login_screen.LoginViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val primaryColor = Color(0xFFFF5722)

    val tasks by viewModel.tasks.collectAsState()
    val error by viewModel.error.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getAllTasks()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Menú",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                    label = { Text("Mi cuenta") },
                    selected = false,
                    onClick = { }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Clear, contentDescription = null) },
                    label = { Text("Cerrar Sesion") },
                    selected = false,
                    onClick = { viewModel.closeSession() }
                )

            }
        },
        content = {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Home") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(Icons.Filled.AccountCircle, contentDescription = "Menú")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = primaryColor,
                            titleContentColor = Color.White,
                            navigationIconContentColor = Color.White
                        )
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate("new_task")
                        },
                        containerColor = primaryColor,
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Agregar tarea")
                    }
                }
            ) { innerPadding ->

                if (error != null) {
                    Text("Error")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        items(tasks) { task ->
                            TaskCard(
                                title = task.title,
                                description = task.description,
                                isCompleted = task.done ?: false,
                                onDelete = { task.id?.let { viewModel.deleteTask(it) } },
                                onEdit = {},
                                onToggleComplete = {}
                            )
                        }
                    }
                }

            }
        }
    )
}
