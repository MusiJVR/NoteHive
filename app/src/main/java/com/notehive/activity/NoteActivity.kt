package com.notehive.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.notehive.R
import com.notehive.menu.MenuHelper
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
        val notePinned = intent.getBooleanExtra("NOTE_PINNED", false)
        val noteArchived = intent.getBooleanExtra("NOTE_ARCHIVED", false)
        val noteTitle = intent.getStringExtra("NOTE_TITLE") ?: ""
        val noteContent = intent.getStringExtra("NOTE_CONTENT") ?: ""
        val noteTimestamp = intent.getStringExtra("NOTE_TIMESTAMP") ?: LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

        originalNote = if (noteId != -1L) {
            Note(noteId, notePinned, noteArchived, noteTitle, noteContent, noteTimestamp)
        } else null

        titleView.text = noteTitle
        contentView.text = noteContent

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener { finishWithSave() }

        val moreButton: ImageButton = findViewById(R.id.moreButton)
        moreButton.setOnClickListener { view ->
            MenuHelper.showPopupMenu(
                context = this,
                anchor = view,
                note = originalNote,
                onPinnedToggled = { updatedNote ->
                    originalNote = updatedNote
                    CoroutineScope(Dispatchers.IO).launch {
                        NoteDatabase.getDatabase(this@NoteActivity).noteDao().insertOrUpdate(updatedNote)
                    }
                    invalidateOptionsMenu()
                    Toast.makeText(
                        this,
                        if (originalNote?.pinned == true) R.string.note_pinned else R.string.note_unpinned,
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onArchiveToggled = { updatedNote ->
                    originalNote = updatedNote
                    CoroutineScope(Dispatchers.IO).launch {
                        NoteDatabase.getDatabase(this@NoteActivity).noteDao().insertOrUpdate(updatedNote)
                    }
                    invalidateOptionsMenu()
                    Toast.makeText(
                        this,
                        if (originalNote?.archived == true) R.string.note_archived else R.string.note_unarchived,
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onDelete = {
                    originalNote?.let { note ->
                        CoroutineScope(Dispatchers.IO).launch {
                            NoteDatabase.getDatabase(this@NoteActivity).noteDao().delete(note)
                        }
                        Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            )
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
            title = getText(R.string.note).toString()
        }

        val oldTimestamp = originalNote?.timestamp ?: LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

        val currentNote = Note(
            id = originalNote?.id ?: 0,
            pinned = originalNote?.pinned ?: false,
            archived = originalNote?.archived ?: false,
            title = title,
            content = content,
            timestamp = if (title != originalNote?.title || content != originalNote?.content) LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) else oldTimestamp
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
}
