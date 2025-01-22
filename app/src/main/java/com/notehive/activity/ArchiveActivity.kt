package com.notehive.activity

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

class ArchiveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        LanguageManager.applyLanguage(this)
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)

        val recyclerView: RecyclerView = findViewById(R.id.notesArchiveRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val noteDao = NoteDatabase.getDatabase(this).noteDao()

        noteDao.getAllNotesInArchive().observe(this) { notes ->
            recyclerView.adapter = NotesAdapter(notes) { note, view ->
                MenuHelper.showPopupMenu(
                    context = this,
                    anchor = view,
                    note = note,
                    onPinnedToggled = { updatedNote ->
                        CoroutineScope(Dispatchers.IO).launch {
                            NoteDatabase.getDatabase(this@ArchiveActivity).noteDao().insertOrUpdate(updatedNote)
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
                            NoteDatabase.getDatabase(this@ArchiveActivity).noteDao().insertOrUpdate(updatedNote)
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
                            NoteDatabase.getDatabase(this@ArchiveActivity).noteDao().delete(note)
                        }
                        Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}
