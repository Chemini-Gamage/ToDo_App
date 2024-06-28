package com.example.todo4

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class NotesDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 4
        private const val TABLE_NAME = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_CALENDAR = "calendar"
        private const val COLUMN_CLOCK ="clock"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_CONTENT + " TEXT,"
                + COLUMN_CALENDAR + " TEXT,"
                + COLUMN_CLOCK+ " TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun deleteNote(noteId: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(noteId.toString()))
        db.close()
    }

    fun getAllNotes(): List<Note> {
        val noteList: MutableList<Note> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        cursor?.use {
            while (it.moveToNext()) {
                val note = Note(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE)),
                    content = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTENT)),
                    calendar = it.getString(it.getColumnIndexOrThrow(COLUMN_CALENDAR)),
                    clock =it.getString(it.getColumnIndexOrThrow(COLUMN_CLOCK))
                )
                noteList.add(note)
            }
        }
        return noteList
    }

    fun getNoteById(id: Int): Note {
        val db = readableDatabase
        val cursor = db.query(
            "notes",
            arrayOf("id", "title", "content", "calendar", "clock"),
            "id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        val note = Note(
            cursor.getInt(cursor.getColumnIndexOrThrow("id")),
            cursor.getString(cursor.getColumnIndexOrThrow("title")),
            cursor.getString(cursor.getColumnIndexOrThrow("content")),
            cursor.getString(cursor.getColumnIndexOrThrow("calendar")),
            cursor.getString(cursor.getColumnIndexOrThrow("clock"))
        )
        cursor.close()
        return note
    }

    fun updateNote(note: Note): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_CALENDAR, note.calendar)
            put(COLUMN_CLOCK,note.clock)
        }
        val rowsAffected = db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(note.id.toString()))
        db.close()
        return rowsAffected
    }

    fun insertNote(note: Note): Long {
        return try {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_TITLE, note.title)
                put(COLUMN_CONTENT, note.content)
                put(COLUMN_CALENDAR, note.calendar)
                put(COLUMN_CLOCK,note.clock)
            }
            val id = db.insert(TABLE_NAME, null, values)
            if (id == -1L) {
                Log.e("InsertNoteError", "Failed to insert note into $TABLE_NAME")
            }
            db.close()
            id
        } catch (e: Exception) {
            Log.e("InsertNoteError", "Exception occurred while inserting note", e)
            -1L // Return -1L to indicate failure
        }

    }


}

