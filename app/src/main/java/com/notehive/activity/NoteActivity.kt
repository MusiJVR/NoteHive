package com.notehive.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.notehive.R
import com.notehive.note.Note
import com.notehive.note.NoteDatabase
import com.notehive.util.LanguageManager
import com.notehive.util.ThemeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class NoteActivity : AppCompatActivity() {
    private lateinit var titleView: TextView
    private lateinit var contentView: TextView
    private var originalNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        LanguageManager.applyLanguage(this)
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        titleView = findViewById(R.id.noteTitle)
        contentView = findViewById(R.id.noteContent)

        val noteId = intent.getLongExtra("NOTE_ID", -1)
        val noteArchived = intent.getBooleanExtra("NOTE_ARCHIVED", false)
        val noteTitle = intent.getStringExtra("NOTE_TITLE") ?: ""
        val noteContent = intent.getStringExtra("NOTE_CONTENT") ?: ""

        originalNote = if (noteId != -1L) {
            Note(noteId, noteArchived, noteTitle, noteContent, "")
        } else null

        titleView.text = noteTitle
        contentView.text = noteContent

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener { finishWithSave() }

        val moreButton: ImageButton = findViewById(R.id.moreButton)
        moreButton.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)

            menuInflater.inflate(R.menu.more_menu, popupMenu.menu)

            val archiveMenuItem = popupMenu.menu.findItem(R.id.action_add_to_archive)
            archiveMenuItem.title = if (originalNote?.archived == true) {
                getString(R.string.remove_from_archive)
            } else {
                getString(R.string.add_to_archive)
            }

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_pin -> {
                        Toast.makeText(this, "Note pinned", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_add_to_archive -> {
                        toggleArchiveState()
                        true
                    }
                    R.id.action_delete -> {
                        deleteNoteAndFinish()
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }
    }

    private fun toggleArchiveState() {
        originalNote?.let { note ->
            val newArchivedState = !note.archived
            val updatedNote = note.copy(archived = newArchivedState)

            CoroutineScope(Dispatchers.IO).launch {
                val noteDao = NoteDatabase.getDatabase(this@NoteActivity).noteDao()
                noteDao.insertOrUpdate(updatedNote)
            }

            originalNote = updatedNote
            invalidateOptionsMenu()

            Toast.makeText(this, if (originalNote?.archived == true) "Note archived" else "Note unarchived", Toast.LENGTH_SHORT).show()
        }
    }

    private fun finishWithSave() {
        var title = titleView.text.toString().trim()
        val content = contentView.text.toString().trim()

        if (title.isEmpty()) {
            if (content.isEmpty()) {
                finish()
                return
            }
            title = "Note"
        }

        val currentNote = Note(
            id = originalNote?.id ?: 0,
            archived = originalNote?.archived ?: false,
            title = title,
            content = content,
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        )

        if (currentNote != originalNote) {
            saveNoteToDatabase(currentNote)
        }

        finish()
    }

    private fun saveNoteToDatabase(note: Note) {
        val noteDao = NoteDatabase.getDatabase(this).noteDao()
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.insertOrUpdate(note)
        }
    }

    private fun deleteNoteAndFinish() {
        originalNote?.let { note ->
            val noteDao = NoteDatabase.getDatabase(this).noteDao()
            CoroutineScope(Dispatchers.IO).launch {
                noteDao.delete(note)
            }
            Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
        }
        finish()
    }
}
