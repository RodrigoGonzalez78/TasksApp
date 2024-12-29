package com.example.tasksapp.presenter.home_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskCard(
    title: String,
    description: String,
    isCompleted: Boolean,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onToggleComplete: (Boolean) -> Unit
) {
    val primaryColor = Color(0xFFFF5722)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCompleted) Color.Gray else Color.Black
                )
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = onToggleComplete,
                    colors = CheckboxDefaults.colors(
                        checkedColor = primaryColor,
                        uncheckedColor = Color.Gray
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = if (isCompleted) Color.Gray else Color.Black.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onEdit,
                    colors = ButtonDefaults.textButtonColors(contentColor = primaryColor)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                    Spacer(Modifier.width(4.dp))
                    Text("Editar")
                }
                Spacer(Modifier.width(8.dp))
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    Spacer(Modifier.width(4.dp))
                    Text("Eliminar")
                }
            }
        }
    }
}