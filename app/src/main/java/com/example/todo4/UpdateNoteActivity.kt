package com.example.todo4

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var db: NotesDatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)

        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val contentEditText = findViewById<EditText>(R.id.contentEditText)
        val calendarEditText = findViewById<EditText>(R.id.calandarEditText)
        val clockEditText = findViewById<EditText>(R.id.clockEditText)
        val updateButton = findViewById<ImageView>(R.id.updateButton)

        db = NotesDatabaseHelper(this)

        noteId = intent.getIntExtra("note_id", -1)
        if (noteId != -1) {
            val note = db.getNoteById(noteId)
            titleEditText.setText(note.title)
            contentEditText.setText(note.content)
            calendarEditText.setText(note.calendar)
            clockEditText.setText(note.clock)
        }

        updateButton.setOnClickListener {
            val updatedNote = Note(
                id = noteId,
                title = titleEditText.text.toString(),
                content = contentEditText.text.toString(),
                calendar = calendarEditText.text.toString(),
                clock = clockEditText.text.toString()
            )
            db.updateNote(updatedNote)

            val resultIntent = Intent().apply {
                putExtra("updated_note", updatedNote)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
