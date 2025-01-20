package com.notehive.activity

import android.content.Intent
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
            recyclerView.adapter = NotesAdapter(notes)
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
