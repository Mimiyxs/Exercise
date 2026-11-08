package ca.qc.exercise_ergonomie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ca.qc.exercise_ergonomie.Data.AppDatabase
import ca.qc.exercise_ergonomie.Data.Note
import ca.qc.exercise_ergonomie.Data.NoteDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: NoteDAO =
        AppDatabase.getDatabase(application.applicationContext).noteDao()
    val notes: StateFlow<List<Note>> = dao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteNote(note : Note) = viewModelScope.launch(Dispatchers.IO) {
        dao.delete(note)
    }

    fun saveNote(note : Note) {
        viewModelScope.launch(Dispatchers.IO) {
            if (note.uid == 0) {
                dao.insert(note)
            } else {
                dao.update(note)
            }
        }
    }
}