package com.example.notelistjava;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notelistjava.bean.Note;

public class AddEditNoteActivity extends AppCompatActivity {

    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;

    private EditText textTitle;
    private EditText textContent;
    private Button buttonSave;
    private Button buttonCancel;

    private Note note;
    private boolean needRefresh;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        this.textTitle = (EditText)findViewById(R.id.editTextNoteTitle);
        this.textContent = (EditText)findViewById(R.id.editTextNoteContent);

        this.buttonSave = (Button)findViewById(R.id.buttonSave);
        this.buttonCancel = (Button)findViewById(R.id.buttonCancel);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSaveClicked();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCancelClicked();
            }
        });

        Intent intent = getIntent();
        this.note = (Note) intent.getSerializableExtra("note");
        if (note == null){
            this.mode = MODE_CREATE;
        }else {
            this.mode = MODE_EDIT;
            this.textTitle.setText(note.getNoteTile());
            this.textContent.setText(note.getNoteContent());
        }
    }

    @Override
    public void finish() {
        //create Intent
        Intent data = new Intent();
        // Request MainActivity refresh its ListView (or not).
        data.putExtra("needRefresh", needRefresh);

        // Set Result
        this.setResult(Activity.RESULT_OK, data);
        super.finish();
    }

    // User Click on the Save button.
    public void buttonSaveClicked(){
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        String title = this.textTitle.getText().toString();
        String content = this.textContent.getText().toString();
        if(title.equals("") || content.equals("")){
            Toast.makeText(getApplicationContext(),
                    "Please enter title & content", Toast.LENGTH_LONG).show();
            return;
        }
        if (mode == MODE_CREATE){
            this.note = new Note(title,content);
            db.addNote(note);
        }else {
            this.note.setNoteTile(title);
            this.note.setNoteContent(content);
            db.updateNote(note);
        }
        this.needRefresh = true;
        // Back to MainActivity.
        this.onBackPressed();
    }
    // User Click on the Cancel button.
    public void buttonCancelClicked()  {
        // Do nothing, back MainActivity.
        this.onBackPressed();
    }
}