package com.notehive.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.notehive.util.LanguageManager
import com.notehive.R
import com.notehive.menu.MenuHelper
import com.notehive.note.NoteDao
import com.notehive.note.NoteDatabase
import com.notehive.note.NotesAdapter
import com.notehive.util.ThemeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: NotesAdapter
    private lateinit var noteDao: NoteDao

    companion object {
        var instance: MainActivity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        instance = this
        LanguageManager.applyLanguage(this)
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchIcon: ImageButton = findViewById(R.id.searchIcon)
        val closeSearchIcon: ImageButton = findViewById(R.id.closeSearchIcon)
        val searchField: EditText = findViewById(R.id.searchField)
        val appTitle: TextView = findViewById(R.id.appTitle)

        val recyclerView: RecyclerView = findViewById(R.id.notesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteDao = NoteDatabase.getDatabase(this).noteDao()

        noteDao.getAllNotesNotInArchive().observe(this) { notes ->
            adapter = NotesAdapter(notes) { note, view ->
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
            recyclerView.adapter = adapter
        }

        searchIcon.setOnClickListener {
            appTitle.visibility = View.GONE
            searchField.visibility = View.VISIBLE
            closeSearchIcon.visibility = View.VISIBLE
        }

        closeSearchIcon.setOnClickListener {
            searchField.text.clear()
            appTitle.visibility = View.VISIBLE
            searchField.visibility = View.GONE
            closeSearchIcon.visibility = View.GONE
            noteDao.getAllNotesNotInArchive().observe(this) { notes ->
                adapter.submitList(notes)
            }
        }

        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isEmpty()) {
                    noteDao.getAllNotesNotInArchive().observe(this@MainActivity) { notes ->
                        adapter.submitList(notes)
                    }
                } else {
                    noteDao.getAllNotesNotInArchive().observe(this@MainActivity) { notes ->
                        val filteredNotes = notes.filter { it.title.contains(query, ignoreCase = true) }
                        adapter.submitList(filteredNotes)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

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
