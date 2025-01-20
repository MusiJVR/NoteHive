package com.notehive.note

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.notehive.R
import com.notehive.activity.NoteActivity
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class NotesAdapter(private val notes: List<Note>) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.noteTitle)
        val content: TextView = itemView.findViewById(R.id.noteContentPreview)
        val timestamp: TextView = itemView.findViewById(R.id.noteTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.title.text = note.title
        holder.content.text = note.content

        val formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")
        val formattedTimestamp = LocalDateTime.parse(note.timestamp).format(formatter)
        holder.timestamp.text = formattedTimestamp

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, NoteActivity::class.java).apply {
                putExtra("NOTE_ID", note.id)
                putExtra("NOTE_ARCHIVED", note.archived)
                putExtra("NOTE_TITLE", note.title)
                putExtra("NOTE_CONTENT", note.content)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = notes.size
}
