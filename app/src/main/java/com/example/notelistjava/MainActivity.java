package com.example.notelistjava;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.notelistjava.bean.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;

    private static final int MY_REQUEST_CODE = 1000;

    private List<Note> noteList = new ArrayList<Note>();
    private ArrayAdapter<Note> listViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //anh xa
        listView = (ListView)findViewById(R.id.listViewMain);
        //du lieu
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        db.createDefaultNotesIfNeed();
        List<Note> list = db.getAllNotes();
        this.noteList.addAll(list);
        //Adapter
        this.listViewAdapter = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1,this.noteList);
        // Assign adapter to ListView
        this.listView.setAdapter(this.listViewAdapter);
        // Register the ListView for Context menu
        registerForContextMenu(this.listView);
    }
    //tao menu context

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        //add groupId, itemId, order, title
        menu.add(0,MENU_ITEM_VIEW,1,"View Note");
        menu.add(0, MENU_ITEM_CREATE , 2, "Create Note");
        menu.add(0, MENU_ITEM_EDIT , 3, "Edit Note");
        menu.add(0, MENU_ITEM_DELETE, 4, "Delete Note");
    }

    //xu ly su kien khi view long click

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Note selectedNote = (Note)this.listView.getItemAtPosition(info.position);

        switch (item.getItemId()){
            case MENU_ITEM_VIEW:
                Toast.makeText(getApplicationContext(), selectedNote.getNoteContent(), Toast.LENGTH_LONG).show();
                break;
            case MENU_ITEM_CREATE:
                Intent intent = new Intent(this, AddEditNoteActivity.class);
                this.startActivityForResult(intent,MY_REQUEST_CODE);
                break;
            case MENU_ITEM_EDIT:
                Intent intent1 = new Intent(this,AddEditNoteActivity.class);
                intent1.putExtra("Note",selectedNote);
                this.startActivityForResult(intent1,MY_REQUEST_CODE);
            case MENU_ITEM_DELETE:
                new AlertDialog.Builder(this)
                        .setMessage(selectedNote.getNoteTile()+". Are you sure you want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteNote(selectedNote);
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE){
            boolean needRefresh = data.getBooleanExtra("needRefresh", true);
            // Refresh ListView
            if (needRefresh) {
                this.noteList.clear();
                MyDatabaseHelper db = new MyDatabaseHelper(this);
                List<Note> list = db.getAllNotes();
                this.noteList.addAll(list);


                // Notify the data change (To refresh the ListView).
                this.listViewAdapter.notifyDataSetChanged();
            }
        }
    }

    private void deleteNote(Note note){
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        db.deleteNote(note);
        this.noteList.remove(note);

        this.listViewAdapter.notifyDataSetChanged();
    }
}