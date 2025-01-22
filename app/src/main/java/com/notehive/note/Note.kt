package com.notehive.note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pinned: Boolean,
    val archived: Boolean,
    val title: String,
    val content: String,
    val timestamp: String
)
