package ca.qc.lab2exercise

import PizzaViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.qc.lab2exercise.ui.theme.Lab2ExerciseTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab2ExerciseTheme {
                AppNav()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav() {
    val navController = rememberNavController()
    val viewModel: PizzaViewModel = viewModel()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Pizza App") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.LightGray)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.LightGray
            ) {}
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "selection",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("selection") {
                PizzaSelectionScreen(viewModel = viewModel,
                    navController = navController,
                    scope = scope,
                    snackbarHostState = snackbarHostState)
            }
            composable("bill") {
                PizzaBillScreen(
                    viewModel = viewModel,
                    navController = navController,
                    scope = scope,
                    snackbarHostState = snackbarHostState
                )
            }
            composable("history") {
                HistoryPizzaScreen(viewModel = viewModel, navController = navController)
            }
        }
    }
}

@Composable
fun PizzaSelectionScreen(viewModel: PizzaViewModel, navController: NavController,
                         scope : CoroutineScope, snackbarHostState: SnackbarHostState ) {
    val menu = listOf("Pepperoni", "Cheese", "Italian Sausage", "Supreme (Everything)", "Mushroom")
    val sizes = listOf("Petite", "Moyenne", "Grande")
    val tips = listOf("0%", "10%", "15%", "20%")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Choisissez votre pizza", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        DropdownMenuBox(options = menu, selected = viewModel.pizza) {
            viewModel.pizza = it
        }

        Spacer(Modifier.height(16.dp))
        Text("Choisissez la taille de la pizza", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        DropdownMenuBox(options = sizes, selected = viewModel.size) {
            viewModel.size = it
        }

        Spacer(Modifier.height(16.dp))
        Text("Choisissez le pourboire", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        DropdownMenuBox(options = tips, selected = viewModel.tip) {
            viewModel.tip = it
        }

        Spacer(Modifier.height(30.dp))
        Button(
            onClick = {
//                viewModel.calculateTotal()
//                navController.navigate("bill")

                //Exercise using Scope
                scope.launch{
                    val result = snackbarHostState.showSnackbar(
                        message = "Confirm your order ?",
                        actionLabel = "Yes",
                        duration = SnackbarDuration.Long
                    )
                    if(result == SnackbarResult.ActionPerformed){
                        viewModel.calculateTotal()
                        navController.navigate("bill")
                    }else{
                        Log.d("Pizza App","user did not confirm the order")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voir la facture")
        }

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = { navController.navigate("history") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Historique")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(options: List<String>, selected: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            readOnly = true,
            value = selected,
            onValueChange = {},
            label = { Text("Options") },
            trailingIcon = { TrailingIcon(expanded) },
            colors = TextFieldDefaults.colors(),
            modifier = Modifier
                .menuAnchor(PrimaryNotEditable, true)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PizzaBillScreen(
    viewModel: PizzaViewModel,
    navController: NavController,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Résumé de la commande", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(24.dp))
        Text("Pizza choisie : ${viewModel.pizza}", fontSize = 18.sp)
        Text("Taille : ${viewModel.size}", fontSize = 18.sp)
        Text("Pourboire : ${viewModel.tip}", fontSize = 18.sp)
        Spacer(Modifier.height(8.dp))
        Text("Total : ${viewModel.total} $", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {
                viewModel.SaveOrder()

                // Exercise 3 : using SnackBar
                scope.launch {
                    snackbarHostState.showSnackbar("Commande enregistrée avec succès !")
                }

                //Go to page history
                navController.navigate("history")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Purchase")
        }

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = { navController.navigate("history") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("See history")
        }

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Retour")
        }
    }
}

@Composable
fun HistoryPizzaScreen(viewModel: PizzaViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Historique des commandes", fontSize = 25.sp, fontFamily = FontFamily.Monospace)
        Spacer(Modifier.height(24.dp))

        if (viewModel.history.isEmpty()) {
            Text("Aucune commande n'a été effectuée.")
        } else {
            viewModel.history.forEach { order ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${order.pizza} (${order.size}) - ${order.total}$ (Tip: ${order.tip})")
                    Divider(thickness = 1.dp)
                }
            }
        }

        Spacer(Modifier.height(32.dp))
        Button(
            onClick = { navController.navigate("selection") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Commander une autre pizza")
        }
    }
}