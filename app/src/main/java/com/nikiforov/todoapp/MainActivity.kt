package com.nikiforov.todoapp

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nikiforov.todoapp.ui.theme.TodoAppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.get
import kotlin.text.set

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main") {
                composable("main") { todoList() }
                composable("history") {HistoryScreen() }
            }
        }
    }
}


data class Task( val text: String, val isDone: Boolean, val date: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun todoList() {
    val tasks = remember { mutableStateListOf<Task>() }
    var state by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    val formattedDate by remember { derivedStateOf {
        DateTimeFormatter
            .ofPattern("MMM dd yyyy")
            .format(pickedDate)
    } }
    val datePickerState = rememberDatePickerState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {showBottomSheet = true}) {
                Icon(Icons.Filled.Add, contentDescription = "Добавить")
            }
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks.size) { index ->
                    Divider(thickness = 1.dp)
                    Row() {
                        Checkbox(
                            checked = tasks[index].isDone,
                            onCheckedChange = { tasks.removeAt(index) }
                        )
                        Text(
                            text = tasks[index].text,
                            modifier = Modifier.clickable { tasks[index] = tasks[index].copy(isDone = !tasks[index].isDone) },
                            style = if (tasks[index].isDone) {
                                TextStyle(textDecoration = TextDecoration.LineThrough)
                            } else {
                                TextStyle()
                            })
                        Text(text = tasks[index].date)
                    }
                }
            }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {showBottomSheet = false}
        ) {
            TextField(
                value = state,
                onValueChange = { state = it },
                singleLine = true
            )
            Spacer(modifier = Modifier.size(16.dp))
            DatePicker(state = datePickerState)
            Button(onClick =
                {
                    datePickerState.selectedDateMillis?.let {millis ->
                        pickedDate = LocalDate.ofEpochDay(millis / 86400000)
                    }
                    tasks.add(Task(text = state, isDone = false, date = formattedDate))
                    state = ""
                    showBottomSheet = false
                }) {
                Text("Добавить новую задачу")
            }

        }
    }
}

@Composable
fun HistoryScreen() {
    Text("История")
}

