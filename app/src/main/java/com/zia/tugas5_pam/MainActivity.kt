package com.zia.tugas5_pam

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var addNoteButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        databaseHelper = DatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter()
        recyclerView.adapter = noteAdapter
        loadNotes()

        addNoteButton = findViewById(R.id.fabAddNote)
        addNoteButton.setOnClickListener {
            showAddNoteDialog()
        }
    }
    private fun loadNotes() {
        val notes = databaseHelper.getAllNotes()
        noteAdapter.submitList(notes)

    }
    private fun showAddNoteDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val titleInput = dialogView.findViewById<TextInputEditText>(R.id.inputTitle)
        val descriptionInput = dialogView.findViewById<TextInputEditText>(R.id.inputDescription)

        AlertDialog.Builder(this)
            .setTitle("Add Note")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val title = titleInput.text.toString().trim()
                val description = descriptionInput.text.toString().trim()

                if (title.isEmpty()) {
                    titleInput.error = "Title is required"
                    return@setPositiveButton
                }

                if (description.isEmpty()) {
                    descriptionInput.error = "Description is required"
                    return@setPositiveButton
                }

                databaseHelper.addNote(title, description, System.currentTimeMillis())
                loadNotes()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}