package com.amir.notes

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class NoteRepository(context: Context) {

    private val gson = Gson()
    private val file = File(context.filesDir, "notes.json")

    fun getNotes(): MutableList<Note> {
        if (!file.exists()) {
            return mutableListOf()
        }
        val json = file.readText()
        val type = object : TypeToken<MutableList<Note>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun saveNotes(notes: List<Note>) {
        val json = gson.toJson(notes)
        file.writeText(json)
    }

    fun addNote(title: String, content: String) {
        val notes = getNotes()
        val newId = (notes.maxOfOrNull { it.id } ?: 0) + 1
        notes.add(Note(newId, title, content))
        saveNotes(notes)
    }

    fun updateNote(note: Note) {
        val notes = getNotes()
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = note
            saveNotes(notes)
        }
    }

    fun deleteNote(noteId: Long) {
        val notes = getNotes()
        notes.removeAll { it.id == noteId }
        saveNotes(notes)
    }
}
