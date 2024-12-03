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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val primaryColor = Color(0xFFFF5722) // Color naranja/rojo del diseño anterior
    var tasks by remember { mutableStateOf(listOf("Tarea 1", "Tarea 2", "Tarea 3")) }
    var isDrawerOpen by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                    icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    label = { Text("Editar cuenta") },
                    selected = false,
                    onClick = { /* Manejar clic en Editar cuenta */ }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                    label = { Text("Cerrar sesión") },
                    selected = false,
                    onClick = { /* Manejar clic en Cerrar sesión */ }
                )
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Lista de Tareas") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menú")
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
                            tasks = tasks + "Nueva Tarea ${tasks.size + 1}"
                        },
                        containerColor = primaryColor,
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Agregar tarea")
                    }
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onDelete = { tasks = tasks - task }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun TaskItem(task: String, onDelete: () -> Unit) {
    var isChecked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFFFF5722),
                        uncheckedColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = task,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isChecked) Color.Gray else Color.Black
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Eliminar tarea",
                    tint = Color.Red
                )
            }
        }
    }
}