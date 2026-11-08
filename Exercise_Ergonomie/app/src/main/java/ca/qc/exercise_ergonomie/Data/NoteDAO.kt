package ca.qc.exercise_ergonomie.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)
    @Update
    suspend fun update(note: Note)
    @Delete
    suspend fun delete(note: Note)
    @Query("SELECT * FROM notes")
    fun getAll(): Flow<List<Note>>
    @Query("SELECT * FROM notes WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): Flow<List<Note>>
}