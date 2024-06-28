package com.example.todo4

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.notesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        notesAdapter = NotesAdapter(emptyList(), this)
        recyclerView.adapter = notesAdapter

        val addButton: ImageView= findViewById(R.id.addButton)
        addButton.setOnClickListener {
            startActivityForResult(Intent(this, AddNoteActivity::class.java), ADD_NOTE_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Refresh notes list after adding a new note
            val newNotesList = getUpdatedNotesFromDatabase()
            notesAdapter.refreshData(newNotesList)
        }
    }

    private fun getUpdatedNotesFromDatabase(): List<Note> {
        // Fetch updated notes list from database
        return NotesDatabaseHelper(this).getAllNotes()
    }

    companion object {
        const val ADD_NOTE_REQUEST = 1
    }
}
