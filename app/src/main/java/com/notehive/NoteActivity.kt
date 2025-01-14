package com.notehive
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
class NoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        val noteId = intent.getLongExtra("NOTE_ID", -1)
        val noteTitle = intent.getStringExtra("NOTE_TITLE")
        val noteContent = intent.getStringExtra("NOTE_CONTENT")
        val titleView: TextView = findViewById(R.id.noteTitle)
        val contentView: TextView = findViewById(R.id.noteContent)
        if (noteId != -1L) {
            titleView.text = noteTitle
            contentView.text = noteContent
        } else {
            titleView.text = ""
            contentView.text = ""
        }
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}
