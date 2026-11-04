package ca.qc.labo2formatif2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.qc.labo2formatif2.ui.theme.Labo2Formatif2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Labo2Formatif2Theme {
                AppNav()
            }
        }
    }
}

@Composable
fun AppNav(){
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("recherche") { RecherchPage(navController) }
    }
}

@Composable
fun HomeScreen(navController: NavController){
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)
        , horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text("Bienvenue au labo2")
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = {navController.navigate("recherche")}) {
            Text("Rechercher des livres")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecherchPage(navController: NavController){
    var title by remember { mutableStateOf("") }
    var viewModel: LivreViewModel = viewModel()
    val livres by viewModel.livres.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recherche de livres") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            IconButton(onClick = {navController.popBackStack()}) {
                Icon(painter = painterResource(
                    R.drawable.retour),
                    contentDescription = "Recherche")
            }

        }
    ){padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = "Titre du livre") }
                )
                Spacer(modifier = Modifier.width(5.dp))
                Button(onClick = {}) {
                    IconButton(
                        onClick =
                            {
                                if (title.isNotEmpty())
                                    viewModel.loadLivres(title.trim())
                            }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Recherche"
                        )
                    }
                }
            }

            LazyColumn {
                items( livres){ livre ->
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.padding(vertical = 4.dp)){
                            Text("${livre.titre}", fontWeight = FontWeight.Bold)
                            Text("Auteur principal : ${livre.auteurs} ")
                            Text("Annee de publication : ${livre.anneePublication}")
                        }

                    }
                    Divider(modifier = Modifier.weight(5f))
                }
            }
        }
    }
}