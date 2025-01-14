package com.notehive

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.notesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sampleNotes = listOf(
            Note(1, "Первая заметка", "Текст", "12:00 01 Jan 2025"),
            Note(2, "Вторая заметка", "Текст", "13:00 01 Jan 2025"),
            Note(3, "Третья заметка", "Текст", "14:00 01 Jan 2025")
        )

        recyclerView.adapter = NotesAdapter(sampleNotes)

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
