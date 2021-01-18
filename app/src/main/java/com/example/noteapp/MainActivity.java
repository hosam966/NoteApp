package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_NOTE = 132;
    private static final int EDIT_NOTE = 133;



    FloatingActionButton floatingActionButtonAddNote;
    ArrayList<Note> notes;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        notes = new ArrayList<>();
        noteAdapter = new NoteAdapter(notes);
        noteAdapter.setOnItemClickListenerNote(new NoteAdapter.OnItemClickListenerNote() {
            @Override
            public void onItemClicked(int adapterPosition, Note note) {
                Intent intent = new Intent(MainActivity.this , AddNewNoteActivity.class);
                intent.putExtra(Constants.EXTRA_NOTE_TEXT, note);
                intent.putExtra(Constants.EXTRA_POSITION, adapterPosition);
                startActivityForResult(intent, EDIT_NOTE);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        recyclerView.setAdapter(noteAdapter);

        floatingActionButtonAddNote = findViewById(R.id.floatingActionButton);
        floatingActionButtonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddNewNote();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE) {
            if (resultCode == RESULT_OK && data != null){
                Note note = data.getParcelableExtra(Constants.EXTRA_NOTE_TEXT);
                addItem(note);
            }
        } else if(requestCode == EDIT_NOTE) {
            if (resultCode == RESULT_OK && data != null) {
                Note note = data.getParcelableExtra(Constants.EXTRA_NOTE_TEXT);
                int position = data.getIntExtra(Constants.EXTRA_POSITION, -1);
                if (position != -1) {
                    System.out.println("EDIT");
                    notes.remove(position);
                    notes.add(position, note);
                    noteAdapter.notifyItemChanged(position);
                }
            }
        }
    }

        public void addItem(Note note){
            notes.add(note);
            noteAdapter.notifyItemInserted(notes.size() - 1);
        }

        public void startAddNewNote(){
            Intent intent = new Intent(MainActivity.this , AddNewNoteActivity.class);
            startActivityForResult(intent, ADD_NOTE);
        }

}