package com.notehive.menu

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.notehive.R
import com.notehive.note.Note

object MenuHelper {
    fun showPopupMenu(
        context: Context,
        anchor: View,
        note: Note?,
        onArchiveToggled: (Note) -> Unit,
        onDelete: () -> Unit
    ) {
        val popupMenu = PopupMenu(context, anchor)
        popupMenu.menuInflater.inflate(R.menu.more_menu, popupMenu.menu)

        val archiveMenuItem = popupMenu.menu.findItem(R.id.action_archive)
        archiveMenuItem.title = if (note?.archived == true) {
            context.getString(R.string.remove_from_archive)
        } else {
            context.getString(R.string.add_to_archive)
        }

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_pin -> {
                    Toast.makeText(context, "Note pinned", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_archive -> {
                    note?.let {
                        val newArchivedState = !it.archived
                        val updatedNote = it.copy(archived = newArchivedState)
                        onArchiveToggled(updatedNote)
                    }
                    true
                }
                R.id.action_delete -> {
                    onDelete()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }
}
