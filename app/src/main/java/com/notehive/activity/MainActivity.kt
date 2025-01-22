package com.notehive.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.notehive.util.LanguageManager
import com.notehive.R
import com.notehive.menu.MenuHelper
import com.notehive.note.NoteDatabase
import com.notehive.note.NotesAdapter
import com.notehive.util.ThemeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    companion object {
        var instance: MainActivity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        instance = this
        LanguageManager.applyLanguage(this)
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.notesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val noteDao = NoteDatabase.getDatabase(this).noteDao()

        noteDao.getAllNotesNotInArchive().observe(this) { notes ->
            recyclerView.adapter = NotesAdapter(notes) { note, view ->
                MenuHelper.showPopupMenu(
                    context = this,
                    anchor = view,
                    note = note,
                    onPinnedToggled = { updatedNote ->
                        CoroutineScope(Dispatchers.IO).launch {
                            NoteDatabase.getDatabase(this@MainActivity).noteDao().insertOrUpdate(updatedNote)
                        }
                        invalidateOptionsMenu()
                        Toast.makeText(
                            this,
                            if (updatedNote.pinned) R.string.note_pinned else R.string.note_unpinned,
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onArchiveToggled = { updatedNote ->
                        CoroutineScope(Dispatchers.IO).launch {
                            NoteDatabase.getDatabase(this@MainActivity).noteDao().insertOrUpdate(updatedNote)
                        }
                        invalidateOptionsMenu()
                        Toast.makeText(
                            this,
                            if (updatedNote.archived) R.string.note_archived else R.string.note_unarchived,
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onDelete = {
                        CoroutineScope(Dispatchers.IO).launch {
                            NoteDatabase.getDatabase(this@MainActivity).noteDao().delete(note)
                        }
                        Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        val archiveButton: ImageButton = findViewById(R.id.archiveButton)
        val addNoteButton: ImageButton = findViewById(R.id.addNoteButton)
        val settingsButton: ImageButton = findViewById(R.id.settingsButton)
        archiveButton.setOnClickListener {
            val intent = Intent(this, ArchiveActivity::class.java)
            startActivity(intent)
        }
        addNoteButton.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}
