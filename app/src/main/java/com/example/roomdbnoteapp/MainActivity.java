package com.example.roomdbnoteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button buttonCreate;
    LinearLayout layout;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    DBHelper dbHelper;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonCreate = findViewById(R.id.buttonCreate);
        fab = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);
        layout = findViewById(R.id.linearLayout);


        dbHelper = DBHelper.getInstance(this);

        showNotes();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_box);
                // dialog box poora bada hoker screen par dikhe uske liye important code
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                EditText etTitle, etDesc;
                Button add;

                etTitle = dialog.findViewById(R.id.etTitle);
                etDesc = dialog.findViewById(R.id.etDesc);
                add = dialog.findViewById(R.id.buttonAdd);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = etTitle.getText().toString();
                        String desc = etDesc.getText().toString();
                        if (!title.isEmpty() && !desc.isEmpty()) {
                            // add data to Database
                            dbHelper.noteDao().insert(new NoteModel(title, desc));
                            // show data from DB to recyclerView
                            showNotes();
                            dialog.dismiss();
                        }else {
                            Toast.makeText(MainActivity.this, "All Fields Must be filled", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }

            
        });

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.performClick(); // jo kaam FAB button par hoga wahi create note pr hoga
            }
        });
    }

    public void showNotes() {

        ArrayList<NoteModel> listNotes = (ArrayList<NoteModel>) dbHelper.noteDao().getNotes();
        if (listNotes.size() > 0) {
            layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            //different height ke note banane ke liye staggeredgridLayout bananna bahut zaroori hai
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            //different height ke note banane ke liye staggeredgridLayout bananna bahut zaroori hai
            recyclerView.setLayoutManager(layoutManager);
            // if size of list is greater than zero then show notes
            noteAdapter = new NoteAdapter(MainActivity.this, listNotes, dbHelper);
            recyclerView.setAdapter(noteAdapter);
        } else {
            // invisible the Empty Note
            layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}