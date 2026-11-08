package ca.qc.exercise_ergonomie

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import ca.qc.exercise_ergonomie.Data.Note
import ca.qc.exercise_ergonomie.ui.theme.Exercise_ErgonomieTheme

class codelab_exercise : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Exercise_ErgonomieTheme {
                val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1000.dp, 800.dp))
                NoteApp(windowSizeClass)
            }
        }
    }
    enum class Screen { LIST, EDIT }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NoteApp(windowSize: WindowSizeClass){

        val viewModel : NoteViewModel by viewModels()
        val notes by viewModel.notes.collectAsState()


        var screen by rememberSaveable { mutableStateOf(Screen.LIST) }
        var selectedId by rememberSaveable { mutableIntStateOf(0) }
        val selectedNote: Note?
        if (selectedId == 0) {
            selectedNote = null
        } else {
            selectedNote = notes.find { note ->
                note.uid == selectedId
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(title ={Text("NoteApp")}, actions = {
                    if (screen == Screen.LIST) {
                        IconButton(onClick = {
                            selectedId = 0
                            screen = Screen.EDIT
                        }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add new note"
                            )
                        }
                    }else{
                        if (selectedNote != null) {
                            IconButton(onClick = {
                                viewModel.deleteNote(selectedNote)
                                screen = Screen.LIST
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Note"
                                )
                            }
                        }
                    }
                })
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)){
                when (screen){
                    Screen.LIST ->{
                        val numColumns = when (windowSize.widthSizeClass) {
                            WindowWidthSizeClass.Compact -> 1
                            WindowWidthSizeClass.Medium -> 2
                            WindowWidthSizeClass.Expanded -> 4
                            else -> {1}
                        }
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(numColumns),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(notes){ note ->
                                Row(modifier = Modifier.fillMaxWidth().clickable {
                                    selectedId = note.uid
                                    screen = Screen.EDIT
                                }) {
                                    Text(
                                        text = note.subject,
                                        textDecoration = TextDecoration.Underline
                                    )
                                }
                            }
                        }
                    }
                    Screen.EDIT -> {
                        FormulaireAjoute(
                            currentNote = selectedNote ?: Note(
                                subject = "", description = "", body = ""
                            ),
                            onSave = { noteSave ->
                                viewModel.saveNote(noteSave)
                                screen = Screen.LIST
                            },
                            onCancel = {
                                screen = Screen.LIST
                            }
                        )
                    }
                }
            }
        }
    }
}
