package ca.qc.exercise_ergonomie.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val subject: String,
    val description: String,
    val body: String
)
