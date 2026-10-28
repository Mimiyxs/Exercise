package ca.qc.apiexercise

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.qc.apiexercise.ui.theme.APIExerciseTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            APIExerciseTheme {
                //Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    SnackBarApp(
//                        modifier = Modifier.padding(innerPadding)
//                    )
                    TodoScreen()
               // }
            }
        }
    }
}

@Composable
fun SnackBarApp(modifier: Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Button(
            modifier = Modifier.padding(padding),
            onClick = {
                scope.launch(Dispatchers.Main) {
                    val result = snackbarHostState.showSnackbar(
                        message = "Je suis vraiment cool!",
                        actionLabel = "Non",
                        duration = SnackbarDuration.Indefinite
                    )
                    when(result) {
                        SnackbarResult.ActionPerformed -> { }
                        SnackbarResult.Dismissed -> { }
                    }
                }
            }
        ) {
            Text("Show Snackbar")
        }
    }
}
@Composable
fun TodoScreen() {
    val viewModel: TodosViewModel = viewModel()
    val todos by viewModel.todos.collectAsState()
    Column(Modifier.padding(16.dp)) {
        Button(onClick = { viewModel.loadTodos() }) {
            Text("Fetch Todos")
        }
        LazyColumn {
            items(todos) { todo ->
                Text(
                    text = "${todo.titre} (${if (todo.complete) "done" else "pending"})"
                )
                Divider(Modifier.padding(top = 4.dp, bottom = 4.dp))
            }
        }
    }
}
class TodosViewModel : ViewModel() {
    val todos = MutableStateFlow<List<Todo>>(emptyList())
    fun loadTodos() {
        viewModelScope.launch {
            try {
                todos.value = ApiClient.apiService.getTodos()
            } catch (e: Exception) {
                Log.e("TodoVM"
                    , "Error: ${e.message}")
            }
        }
    }
}


