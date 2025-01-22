package com.notehive.menu

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import com.notehive.R
import com.notehive.note.Note

object MenuHelper {
    fun showPopupMenu(
        context: Context,
        anchor: View,
        note: Note?,
        onPinnedToggled: (Note) -> Unit,
        onArchiveToggled: (Note) -> Unit,
        onDelete: () -> Unit
    ) {
        val popupMenu = PopupMenu(context, anchor)
        popupMenu.menuInflater.inflate(R.menu.more_menu, popupMenu.menu)

        val pinMenuItem = popupMenu.menu.findItem(R.id.action_pin)
        pinMenuItem.title = if (note?.pinned == true) {
            context.getString(R.string.unpin)
        } else {
            context.getString(R.string.pin)
        }

        val archiveMenuItem = popupMenu.menu.findItem(R.id.action_archive)
        archiveMenuItem.title = if (note?.archived == true) {
            context.getString(R.string.remove_from_archive)
        } else {
            context.getString(R.string.add_to_archive)
        }

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_pin -> {
                    note?.let {
                        val updatedNote = it.copy(pinned = !it.pinned)
                        onPinnedToggled(updatedNote)
                    }
                    true
                }
                R.id.action_archive -> {
                    note?.let {
                        val updatedNote = it.copy(
                            archived = !it.archived,
                            pinned = false
                        )
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
