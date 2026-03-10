package com.nikiforov.todoapp

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import com.nikiforov.todoapp.ui.theme.TodoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            todoList()
        }
    }
}

data class Task( val text: String, val isDone: Boolean)

@Composable
fun todoList() {
    val tasks = remember { mutableStateListOf<Task>() }
    var state by remember { mutableStateOf("") }
    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(top = 16.dp)
    ) {
        TextField(
            value = state,
            onValueChange = { state = it },
            singleLine = true
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick =
        {
            tasks.add(Task(text = state, isDone = false))
            state = ""
        }) {
            Text("Добавить новую задачу")
        }
        Spacer(modifier = Modifier.size(16.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks.size) {
                Text(
                    text = tasks[it].text,
                    modifier = Modifier.clickable { tasks[it] = tasks[it].copy(isDone = !tasks[it].isDone) },
                    style = if (tasks[it].isDone) {
                        TextStyle(textDecoration = TextDecoration.LineThrough)
                    } else {
                        TextStyle()
                    })


            }
        }
    }
}

