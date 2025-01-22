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
                            if (updatedNote.pinned) "Note pinned" else "Note unpinned",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onArchiveToggled = { updatedNote ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val noteDao = NoteDatabase.getDatabase(this@ArchiveActivity).noteDao()
                            noteDao.insertOrUpdate(updatedNote)
                        }
                        invalidateOptionsMenu()
                        Toast.makeText(
                            this,
                            if (updatedNote.archived) "Note archived" else "Note unarchived",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onDelete = {
                        CoroutineScope(Dispatchers.IO).launch {
                            val noteDao = NoteDatabase.getDatabase(this@ArchiveActivity).noteDao()
                            noteDao.delete(note)
                        }
                        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
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
