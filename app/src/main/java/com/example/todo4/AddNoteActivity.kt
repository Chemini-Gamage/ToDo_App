package com.example.todo4

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    private lateinit var db: NotesDatabaseHelper
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var calendarEditText: EditText
    private lateinit var calendarButton: ImageView
    private lateinit var clockEditText:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        db = NotesDatabaseHelper(this)

        titleEditText = findViewById(R.id.titleEditText)
        contentEditText = findViewById(R.id.contentEditText)
        calendarEditText = findViewById(R.id.calendarEditText)
        calendarButton = findViewById(R.id.calandarButton)
        clockEditText=findViewById(R.id.clockEditText)
        val saveButton: ImageView = findViewById(R.id.saveButton)

        calendarButton.setOnClickListener {
            showDatePickerDialog()
        }

        saveButton.setOnClickListener {
            onSaveButtonClick()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            calendarEditText.setText(dateFormat.format(selectedDate.time))
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun onSaveButtonClick() {
        val title = titleEditText.text.toString()
        val content = contentEditText.text.toString()
        val calendar = calendarEditText.text.toString()
        val clock =clockEditText.text.toString()

        val newNote = Note(
            id = 0, // Set to 0 for auto-increment in SQLite
            title = title,
            content = content,
            calendar = calendar,
            clock=clock
        )

        val insertedId = db.insertNote(newNote)
        if (insertedId != -1L) {
            setResult(Activity.RESULT_OK)
            finish() // Close activity and return to MainActivity
        } else {
            Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show()
        }
    }
}
