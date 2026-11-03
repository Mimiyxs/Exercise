package ca.qc.apiexercise

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.qc.apiexercise.ui.theme.APIExerciseTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.http.POST

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

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {

        //Exercise 1
        /*
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
         */

        //Exercise 2
        val posts by viewModel.post.collectAsState()

        var userId by remember { mutableStateOf("") }

        Column(Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = userId,
                onValueChange = {userId = it},
                label = {Text("user ID")}
            )
            Button(
                onClick = {
                    if (userId.isBlank()) {
                        viewModel.loadPosts()
                    } else {
                        val id = userId.toIntOrNull() // Convert to number
                        if (id != null) {
                            viewModel.loadPostWithID(id)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Rechercher",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            LazyColumn {
                items(posts) { post ->
                    Text(
                        text = "${post.idPost} : ${post.titre}",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = post.body)
                    Divider(Modifier.padding(top = 4.dp, bottom = 4.dp))
                }
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
                Log.e("POSTVM"
                    , "Error: ${e.message}")
            }
        }
    }

    val post = MutableStateFlow<List<Post>>(emptyList())
    fun loadPosts() {
        viewModelScope.launch {
            try {
                post.value = ApiClient.apiService.getPost()
            } catch (e: Exception) {
                Log.e("POSTVM"
                    , "Error: ${e.message}")
            }
        }
    }
    fun loadPostWithID(id: Int){
        viewModelScope.launch {
            try {
                post.value = listOf(ApiClient.apiService.getPostByID(id))
            } catch (e: Exception) {
                Log.e("POSTVM"
                    , "Error: ${e.message}")
            }
        }
    }
}


