package com.notehive.note

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE archived = 1 ORDER BY pinned DESC, timestamp DESC")
    fun getAllNotesInArchive(): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE archived = 0 ORDER BY pinned DESC, timestamp DESC")
    fun getAllNotesNotInArchive(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(note: Note)

    @Delete
    suspend fun delete(note: Note)
}
