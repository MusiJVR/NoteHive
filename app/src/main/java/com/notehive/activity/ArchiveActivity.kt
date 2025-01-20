package com.notehive.activity

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.notehive.util.LanguageManager
import com.notehive.R
import com.notehive.note.NoteDatabase
import com.notehive.note.NotesAdapter
import com.notehive.util.ThemeManager

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
            recyclerView.adapter = NotesAdapter(notes)
        }

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}
