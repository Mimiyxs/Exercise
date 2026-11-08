package ca.qc.exercise_ergonomie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ca.qc.exercise_ergonomie.Data.Note

@Composable
fun FormulaireAjoute(
    currentNote: Note = Note(
        subject = "",
        description = "",
        body = ""
    ),
    onSave: (Note) -> Unit,
    onCancel: () -> Unit
){
    var subject by rememberSaveable { mutableStateOf(currentNote.subject) }
    var description by rememberSaveable { mutableStateOf(currentNote.description) }
    var body by rememberSaveable { mutableStateOf(currentNote.body) }

    var hasSubmitted by rememberSaveable { mutableStateOf(false) }
    val isSubjectValide = subject.isNotBlank()

    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
        Column(modifier = Modifier.fillMaxSize().padding(14.dp)) {
            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Subject of the note") },
                isError = hasSubmitted,
                supportingText = {
                    if (!isSubjectValide && hasSubmitted) {
                        Text(text = "Fill in the subject", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description of the note") },
            )

            OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                label = { Text("Body of the note") },
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp),) {
                Button(onClick = {
                    hasSubmitted = true

                    if (isSubjectValide) {
                        val updateNote = currentNote.copy(
                            subject = subject.trim(),
                            description = description.trim(),
                            body = body.trim()
                        )
                        onSave(updateNote)
                    }
                }) {
                    Text("Save")
                }
                Button(onClick = onCancel) {
                    Text("Cancel")
                }
            }
        }
    }
}