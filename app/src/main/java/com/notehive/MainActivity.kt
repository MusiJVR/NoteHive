package com.notehive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
    }
}
