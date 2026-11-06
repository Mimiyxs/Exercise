package ca.qc.exercise_ergonomie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.DpSize

import ca.qc.exercise_ergonomie.ui.theme.Exercise_ErgonomieTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Exercise_ErgonomieTheme {
                //val windowSizeClass = calculateWindowSizeClass(this)
                val windowSizeClass = WindowSizeClass.calculateFromSize(
                    DpSize(1000.dp, 800.dp)
                )
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Application") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),

                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {

                        Text(
                            text = "Test 1",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Test 2",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )

                        ProfileLayout(windowSizeClass)

                    }
                }
            }
        }
    }
    @Composable
    fun ProfileLayout(windowSizeClass: WindowSizeClass) {
        // TODO: 1. Récupérez la classe de largeur.
        val widthClass = windowSizeClass.widthSizeClass
            // TODO: 2. Utilisez 'when' pour basculer entre Column etRow.
            when (widthClass) {
                WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium  -> {
                    Column(modifier = Modifier
                        .fillMaxWidth(), verticalArrangement = Arrangement.Center) {
                        Text(
                            text = "User's name",
                            style = MaterialTheme.typography.headlineSmall,
                            textDecoration = TextDecoration.Underline)
                        Text(text = "User's info")

                        Image(
                            painter = painterResource(R.drawable.person),
                            contentDescription = "Profile picture",
                            modifier = Modifier.size(80.dp)
                                .clip(CircleShape)
                        )
                    }
                }
                WindowWidthSizeClass.Expanded -> {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        Image(
                            painter = painterResource(R.drawable.person),
                            contentDescription = "Profile picture",
                            modifier = Modifier.size(40.dp)
                                .clip(CircleShape)
                        )
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "User's name",
                                style = MaterialTheme.typography.headlineSmall,
                                textDecoration = TextDecoration.Underline)
                            Text(
                                text = "User's info")
                        }

                    }

                }
                    else -> { // Compact ou Medium

                }
            }
    }
}