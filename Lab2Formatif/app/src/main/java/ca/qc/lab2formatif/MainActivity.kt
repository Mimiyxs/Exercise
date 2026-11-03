package ca.qc.lab2formatif

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.qc.lab2formatif.Data.Livre
import ca.qc.lab2formatif.ui.theme.Lab2FormatifTheme
import com.example.labo2f.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab2FormatifTheme {
               AppNav()
            }
        }
    }
}

enum class Screen { LIST, HOME }
@Composable
fun AppNav() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") { homePage(navController) }
        composable("Recherche") { showCase(navController) }
    }
}

@Composable
fun homePage(navController: NavController){
    Column(modifier = Modifier.fillMaxSize()
        , horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ){
        Text(text = "Bienvenue au labo 2 formatif!", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = { navController.navigate("Recherche")}) {
            Text("Recherche des livres")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun showCase(navController: NavController) {

    var title by remember { mutableStateOf("") }
    val viewModel: LivreViewModel = viewModel()

    val livres: List<Livre> by viewModel.livres.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recherche un livre")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { navController.popBackStack()}){
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = "Retour"
                    )}
                }
            )

        }
    ) { padding ->
        // Contenu principal
        Column  (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.weight(1f),
                    label = { Text(text = "Titre du livre") }
                )
                IconButton(onClick = {
                    if (title.isNotEmpty()) {
                        viewModel.loaLivre(title.trim())
                    }
                }) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Recherche"
                    )
                }

            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(livres) { livre ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(
                            text = livre.titre,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Auteur(s) : ${livre.auteurs?.joinToString(", ") ?: "Non spécifié"}"
                        )
                        Text(
                            text = "Année : ${livre.anneePublication ?: "N/A"}"
                        )
                        Divider(Modifier.padding(top = 4.dp, bottom = 4.dp))
                    }
                }
            }
        }
    }
}


class LivreViewModel : ViewModel() {
    val livres = MutableStateFlow<List<Livre>>(emptyList())
    fun loaLivre(title : String) {
        viewModelScope.launch {
            try {
                livres.value = (ApiClient.apiService.rechercherLivres(title).livres)
            } catch (e: Exception) {
                Log.e("Livres"
                    , "Error: ${e.message}")
            }
        }
    }
}



